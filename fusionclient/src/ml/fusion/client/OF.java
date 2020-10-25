package ml.fusion.client;

import java.net.InetAddress;

// Other Functions
public class OF {
    // easily turn off prints by commenting out one line
    public static void println(String str) {
        System.out.println(str);
    }
    public static String getHostname() {
        try {
            return InetAddress.getLocalHost().getHostName();
        } catch (Exception e) {
            return "__error__";
        }
    }
}
