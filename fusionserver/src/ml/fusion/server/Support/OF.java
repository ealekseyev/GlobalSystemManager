package ml.fusion.server.Support;

import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.InetAddress;

// Other Functions
public final class OF {
    // easily turn off all prints by commenting out one line
    public static void print(String str) {
        System.out.print(str);
    }
    public static void println(String str) {
        System.out.println(str);
    }
    public static <T> void printf(String str, T... args) {
        System.out.printf(str, args);
    }

    public static String getHostname() {
        try {
            return InetAddress.getLocalHost().getHostName();
        } catch (Exception e) {
            return "__error__";
        }
    }
    public static long getUnixTime() {
        return System.currentTimeMillis() / 1000;
    }
    public static long getUnixTimeMillis() {
        return System.currentTimeMillis();
    }

    public static <T> void serialize(T o, String name) throws Exception {
        ObjectOutputStream objout = new ObjectOutputStream(new FileOutputStream(name + ".obj"));
        objout.writeObject(o);
        objout.close();
    }

    public static <T> T deserialize(String name) throws Exception {
        return (T) new ObjectInputStream(new FileInputStream(name + ".obj")).readObject();
    }
}
