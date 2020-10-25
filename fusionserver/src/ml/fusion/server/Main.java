package ml.fusion.server;

import ml.fusion.server.Delegate.Timer;
import ml.fusion.server.MainHandling.ClientForkLauncher;
import ml.fusion.server.Support.GlobalDeps;
import ml.fusion.server.Support.OF;

public class Main {
    public static void main(String[] args) throws Exception {
        // get the system-dependent constants
        GlobalDeps.init();
        OF.println("Running init...");
        try {
            ClientForkLauncher.getInstance().init();
            Timer.getTimer().init();
        } catch(Exception e) {
            OF.println(e.getMessage());
        }

        OF.println("Launching run loop...");
        while(true) {
            try {
                Timer.getTimer().scheduleAndRun();
            } catch(Exception e) { OF.println(e.getMessage()); }
        }
    }
}
