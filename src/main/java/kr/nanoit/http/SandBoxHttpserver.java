package kr.nanoit.http;

import com.sun.net.httpserver.HttpServer;

import java.io.IOException;
import java.net.InetSocketAddress;

public class SandBoxHttpserver {
    private final HttpServer httpServer;

    public SandBoxHttpserver(String host, int port) throws IOException {
        this.httpServer = HttpServer.create(new InetSocketAddress(host, port), 0);
        httpServer.createContext("/", new HttpServerHandler());
    }

    public void start() {
        httpServer.start();
    }

    public void stop(int delay) {
        httpServer.stop(delay);
    }
}