package com.urbanairship.tags;

import com.sun.net.httpserver.HttpServer;

import java.io.IOException;
import java.net.InetSocketAddress;

public class Main {
    public static void main( String[] args ) throws IOException {

        int port = (args == null) ? Integer.parseInt(args[0]) : 1917;

        // Create HTTP server
        HttpServer server = HttpServer.create(new InetSocketAddress(port), 0);
        server.createContext("/api/tags", new PostHandler());
        server.setExecutor(null); // this means that the created by the start method will be used
        server.start();

        Runtime.getRuntime().addShutdownHook(new Thread(() -> System.out.println("Running Shutdown Hook")));
        // This starts a thread that starts running at termination
        // Should I be performing some clean up tasks here?
    }
}
