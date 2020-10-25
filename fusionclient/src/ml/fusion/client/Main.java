package ml.fusion.client;

import java.net.Socket;
import ml.fusion.client.Comms.*;

public class Main {
    public static void main(String[] args) {
        // get the system-dependent constants
        Constants.init();
        OF.println("Running init...");
        try { TaskScheduler.getInstance().init(); } catch(Exception e) { OF.println(e.getMessage()); }
        OF.println("Launching run loop...");
        while(true) {
            try {
                Constants.StatusCode code = TaskScheduler.getInstance().run();
                switch(code) {
                    case SockTOutE:
                        OF.println("Socket timeout error");
                        break;
                    case UnknownE:
                        OF.println("Unkown error");
                        break;
                    case ProceedOK:
                        break;
                }
            } catch(Exception e) { OF.println(e.getMessage()); }
            try { Thread.sleep(Constants.refreshRate); } catch(Exception e) { OF.println(e.getMessage()); }
        }
    }
}
