package com.lux.aio;

import java.net.InetSocketAddress;
import java.nio.ByteBuffer;
import java.nio.channels.AsynchronousSocketChannel;
import java.nio.channels.CompletionHandler;
import java.util.concurrent.CountDownLatch;

public class AioClient implements CompletionHandler<Void, AioClient>, Runnable{

    private AsynchronousSocketChannel client;
    private String host;
    private int port;
    private CountDownLatch latch;

    public AioClient(String host, int port) {
        this.host = host;
        this.port = port;
        try {
            client = AsynchronousSocketChannel.open();
        } catch (Exception e){
            e.printStackTrace();
        }
    }

    @Override
    public void completed(Void result, AioClient attachment) {
        byte[] req = "QUERY TIME ORDER".getBytes();
        ByteBuffer writeBuffer = ByteBuffer.allocate(req.length);
        writeBuffer.put(req);
        writeBuffer.flip();
        client.write(writeBuffer, writeBuffer, new CompletionHandler<Integer, ByteBuffer>() {
            @Override
            public void completed(Integer result, ByteBuffer buffer) {
                if(buffer.hasRemaining()){
                    client.write(buffer, buffer, this);
                } else {
                    ByteBuffer readbuffer = ByteBuffer.allocate(1024);
                    client.read(readbuffer, readbuffer,
                            new CompletionHandler<Integer, ByteBuffer>() {
                                @Override
                                public void completed(Integer result, ByteBuffer attachment) {
                                    attachment.flip();
                                    byte[] bytes = new byte[attachment.remaining()];
                                    attachment.get(bytes);
                                    String body;
                                    try{
                                        body = new String(bytes, "UTF-8");
                                        System.out.println("now is " + body);
                                        latch.countDown();
                                    } catch (Exception e){
                                        e.printStackTrace();
                                    }
                                }

                                @Override
                                public void failed(Throwable exc, ByteBuffer attachment) {
                                    try{
                                        client.close();
                                        latch.countDown();
                                    } catch (Exception e){
                                        e.printStackTrace();
                                    }
                                }
                            });
                }
            }

            @Override
            public void failed(Throwable exc, ByteBuffer attachment) {
                try {
                    client.close();
                    latch.countDown();
                } catch (Exception e){
                    e.printStackTrace();
                }
            }
        });
    }

    @Override
    public void failed(Throwable exc, AioClient attachment) {
        exc.printStackTrace();
        try {
            client.close();
            latch.countDown();
        } catch (Exception e){
            e.printStackTrace();
        }
    }

    @Override
    public void run() {
        latch = new CountDownLatch(1);
        client.connect(new InetSocketAddress("127.0.0.1", 8080), this, this);
        try{
            latch.await();
            client.close();
        } catch (Exception e){
            e.printStackTrace();
        }
    }
}