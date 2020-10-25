package ml.fusion.server.Comms;

import java.io.*;
import java.net.ServerSocket;
import java.net.InetSocketAddress;
import java.net.SocketAddress;

public class ServerSocketWrapper {
    private Integer port;
    private Integer timeout;
    private ServerSocket servsock;

    public ServerSocketWrapper(int port) throws Exception {
        servsock = new ServerSocket(port);
        this.port = port;
    }

    public void bind(SocketAddress bindpoint) throws IOException {
        servsock.bind(bindpoint);
    }

    public SocketWrapper accept() throws Exception {
        return new SocketWrapper(servsock.accept());
    }

    // getters and setters
    public int getPort() {
        return this.port;
    }
    public void setPort(int port) {
        this.port = port;
    }
}
