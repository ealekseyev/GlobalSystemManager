package ml.fusion.server.MainHandling;

import ml.fusion.server.Support.BundledArrayList;
import ml.fusion.server.Comms.ServerSocketWrapper;
import ml.fusion.server.Comms.SocketWrapper;
import ml.fusion.server.Delegate.Timer;
import ml.fusion.server.Delegate.TimerBlueprint;
import ml.fusion.server.Support.GlobalDeps;
import ml.fusion.server.Support.OF;

import java.security.spec.ECField;

// this class is registered with timer @ init() to run as often as possible.
//

public class ClientForkLauncher extends TimerBlueprint {
    // -----Singleton-----
    private static ClientForkLauncher instance;
    public static ClientForkLauncher getInstance() {
        if(instance == null) {
            instance = new ClientForkLauncher();
        }
        return instance;
    }

    // -----Main part of class-----
    private ServerSocketWrapper server;

    // runs once at launch
    public void init() throws Exception {
        OF.println("Finished setup");
        server = new ServerSocketWrapper(GlobalDeps.port);
        Timer.getTimer().register(this);
    }
    // runs once every {Constants.refreshRate} ms
    // launches a new ClientHandler Thread when a new client connects
    @Override
    public void run() {
        try {
            GlobalDeps.commands =
                    (BundledArrayList) OF.deserialize(GlobalDeps.commandsObjFile);
            Timer.getTimer().register(new ClientFork(getClient()));
        } catch (Exception e) {
        }
        // do stuffs here
        //proc.join();
        //return GlobalDeps.StatusCode.ProceedOK;
    }

    public SocketWrapper getClient() throws Exception {
        return server.accept();
    }

    // runs on a separate Thread from Main, launches and finds clients
    /*public static void launch() {
        GlobalDeps.forkThread = new Thread(new Runnable() {
            @Override
            public void run() {
                while (GlobalDeps.isRunning) {
                    try {
                        ClientForkLauncher.getInstance().run(ClientForkLauncher.getInstance().getClient());
                    } catch (Exception e) {

                    }
                }
            }
        });
        GlobalDeps.forkThread.start();
    }*/
    @Override
    public boolean isVital()              // if true, task will run every loop cycle. Overrides getRefreshRate
    {
        return false;
    }
    @Override
    public boolean isRepeating()          // does this task repeat or run just once?
    {
        return true;
    }
    @Override
    public int getThreadPriority()       // get importance level, 1-3 least to most important
    {
        return 3;
    }
}