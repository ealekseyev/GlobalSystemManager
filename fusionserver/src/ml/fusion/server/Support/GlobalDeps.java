package ml.fusion.server.Support;

import ml.fusion.server.Delegate.TimerBlueprint;

import java.util.ArrayList;

public class GlobalDeps {
    public static final int port = 5000;
    public static final long refreshRate = 1000;
    public static final int sockConnTOut = 1000;
    public static final String commandsObjFile = "commands";
    public static final int commandsRefreshRate = 500; // ms

    public static volatile BundledArrayList onlinesystems; // name, unixtime, secret
    public static volatile BundledArrayList registeredsystems; // same format, serialized/deserialized at shutdown and boot
    public static volatile BundledArrayList commands; // will contain commands, to be refreshed every (commandRefreshRate) ms

    public static boolean isRunning = true;
    public static TimerBlueprint forkThread = null;

    public enum StatusCode {
        SockTOutE,
        UnknownE,
        ProceedOK
    }

    public static class ClientBundle {
        public ClientBundle(String name, Integer unixtime, Integer secret) {
            this.name = name;
            this.unixtime = unixtime;
            this.secret = secret;
        }
        public String name;
        public Integer unixtime;
        public Integer secret;
        public ArrayList<String> commands;
    }

    public static void init() throws Exception {
        registeredsystems = OF.deserialize("registeredsystems");
    }

    /*public boolean checkDeps() {
        String[] depnames = {
                "/opt/fusion",
                "/opt/fusion/SID.ini"
        };
        for(String dependency: depnames) {
            if (!new File(dependency).exists()) {
                return false;
            }
        }
        return true;
    }*/

    /*public static GlobalDeps.StatusCode restoreDeps() {
        try {
            // restore BID
            Socket server = new Socket();
            server.connect(new InetSocketAddress(GlobalDeps.host, GlobalDeps.port), GlobalDeps.sockConnTOut);

            DataOutputStream sockOut = new DataOutputStream(server.getOutputStream());
            DataInputStream sockIn = new DataInputStream(server.getInputStream());
            String SID = OF.getHostname();
            new File("/opt/fusion").mkdir();
            if(SID != "__error__") {
                sockOut.writeUTF("+?" + SID);  // conduct query - does this SID already exist?
                String response = sockIn.readUTF();
                FileWriter sidini = new FileWriter("/opt/fusion/SID.ini");
                GlobalDeps.SID = sockIn.readUTF();
            } else {
                return GlobalDeps.StatusCode.UnknownE;
            }
            return GlobalDeps.StatusCode.ProceedOK;
        } catch (Exception e) {
            return GlobalDeps.StatusCode.UnknownE;
        }
    }*/
}
