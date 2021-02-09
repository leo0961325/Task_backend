package com.link8.tw.socket;

import com.corundumstudio.socketio.SocketIOServer;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.core.annotation.Order;
import org.springframework.stereotype.Component;

@Component
@Order(value = 1)
public class SocketCommandLineRunner implements CommandLineRunner {

    private final SocketIOServer server;


    @Autowired
    public SocketCommandLineRunner(SocketIOServer server) {
        this.server = server;
    }

    @Override
    public void run(String... args) throws Exception {
        server.start();
        System.out.println("socket.io啟動成功！");
    }
}
