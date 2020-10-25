package ml.fusion.client;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.File;
import java.io.FileWriter;
import java.net.InetSocketAddress;
import java.net.Socket;

public class DepManager {
    // -----Singleton-----
    private static DepManager instance;
    public static DepManager getInstance() {
        if(instance == null) {
            instance = new DepManager();
        }
        return instance;
    }

    public boolean checkDeps() {
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
    }

    public Constants.StatusCode restoreDeps() {
        try {
            // restore BID
            Socket server = new Socket();
            server.connect(new InetSocketAddress(Constants.host, Constants.port), Constants.sockConnTOut);

            DataOutputStream sockOut = new DataOutputStream(server.getOutputStream());
            DataInputStream sockIn = new DataInputStream(server.getInputStream());
            String SID = OF.getHostname();
            new File("/opt/fusion").mkdir();
            if(SID != "__error__") {
                sockOut.writeUTF("+?" + SID);  // conduct query - does this SID already exist?
                String response = sockIn.readUTF();
                FileWriter sidini = new FileWriter("/opt/fusion/SID.ini");
                Constants.SID = sockIn.readUTF();
            } else {
                return Constants.StatusCode.UnknownE;
            }
            return Constants.StatusCode.ProceedOK;
        } catch (Exception e) {
            return Constants.StatusCode.UnknownE;
        }
    }
}
