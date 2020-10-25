package ml.fusion.client;

import java.net.InetAddress;

public class Constants {
    public static final int port = 5000;
    public static final String host = "169.60.224.3";
    public static final long refreshRate = 1000;
    public static final int sockConnTOut = 1000;

    public static int serverSecret;
    public static String SID;
    enum StatusCode {
        SockTOutE,
        UnknownE,
        ProceedOK
    }

    public static void init() {
    }
}
