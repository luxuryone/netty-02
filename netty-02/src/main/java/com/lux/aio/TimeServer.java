package com.lux.aio;
public class TimeServer {

    public static void main(String[] args){
        int port = 8080;
        AsyncTimeServerHandler timeServer = new AsyncTimeServerHandler(port);
        new Thread(timeServer,"AIO-AsyncTimeServer-001").start();
    }
}