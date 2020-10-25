package ml.fusion.client.Comms;

import ml.fusion.client.Constants;

import java.io.*;
import java.net.Socket;
import java.net.InetSocketAddress;
import java.net.SocketAddress;

public class SocketWrapper extends Socket {
    private String host;
    private Integer port;
    private Integer timeout;
    private InetSocketAddress addr;

    public SocketWrapper(String host, int port) throws Exception {
        super(host, port);
        this.host = host;
        this.port = port;
    }

    public SocketWrapper() throws Exception {
        super();
    }

    @Override
    public void connect(SocketAddress endpoint) throws IOException {
        super.connect(endpoint);
        if(host == null && port == null) {
            this.host = ((InetSocketAddress) endpoint).getHostString();
            this.port = ((InetSocketAddress) endpoint).getPort();
        }
    }

    @Override
    public void connect(SocketAddress endpoint, int timeout) throws IOException {
        super.connect(endpoint, timeout);
        if(host == null && port == null) {
            this.host = ((InetSocketAddress) endpoint).getHostString();
            this.port = ((InetSocketAddress) endpoint).getPort();
        }
        this.timeout = timeout;
    }

    public void connect() throws Exception {
        if(host == null && port == null) {
            throw new IllegalArgumentException();
        } else {
            super.connect(new InetSocketAddress(host, port));
        }
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
