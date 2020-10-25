package ml.fusion.server.Comms;

import java.io.*;
import java.net.Socket;
import java.net.InetSocketAddress;
import java.net.SocketAddress;

public class SocketWrapper {
    private String host;
    private Integer port;
    private Integer timeout;
    private InetSocketAddress addr;
    private Socket socket;

    public SocketWrapper(String host, int port) throws Exception {
        socket = new Socket(host, port);
        this.host = host;
        this.port = port;
    }

    public SocketWrapper() throws Exception {
        socket = new Socket();
    }

    public SocketWrapper(Socket socket) throws Exception {
        this.socket = socket;
        this.host = socket.getInetAddress().getHostAddress();
        this.port = socket.getPort();
        this.socket = socket;
    }

    public void connect(SocketAddress endpoint) throws IOException {
        socket.connect(endpoint);
        if(host == null && port == null) {
            this.host = ((InetSocketAddress) endpoint).getHostString();
            this.port = ((InetSocketAddress) endpoint).getPort();
        }
    }

    public void connect(SocketAddress endpoint, int timeout) throws IOException {
        socket.connect(endpoint, timeout);
        if(host == null && port == null) {
            this.host = ((InetSocketAddress) endpoint).getHostString();
            this.port = ((InetSocketAddress) endpoint).getPort();
        }
        this.timeout = timeout;
    }

    public void connect() throws Exception {
        if(host == null && port == null) {
            throw new IllegalArgumentException("Unknown socket endpoint");
        } else {
            socket.connect(new InetSocketAddress(host, port));
        }
    }

    public OutputStream getOutputStream() throws IOException {
        return socket.getOutputStream();
    }

    public InputStream getInputStream() throws IOException {
        return socket.getInputStream();
    }

    public void close() throws Exception {
        socket.close();
    }

    public SocketAddress getRemoteSocketAddress() {
        return socket.getRemoteSocketAddress();
    }

    // getters and setters
    public String getHost() {
        return this.host;
    }
    public void setHost(String host) {
        this.host = host;
    }
    public int getPort() {
        return this.port;
    }
    public void setPort(int port) {
        this.port = port;
    }
}
