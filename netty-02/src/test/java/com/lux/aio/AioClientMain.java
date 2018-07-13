package com.lux.aio;

public class AioClientMain {
    public static void main(String[] args){
        new Thread(new AioClient("127.0.0.1", 8080),"AIO-01").start();
    }

}