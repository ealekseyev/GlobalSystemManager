package ml.fusion.server.MainHandling;

import ml.fusion.server.Comms.SocketWrapper;
import ml.fusion.server.Delegate.TimerBlueprint;
import ml.fusion.server.Support.GlobalDeps;
import ml.fusion.server.Support.OF;

import java.io.DataInputStream;
import java.io.DataOutputStream;

// all the communication happens here

class ClientFork extends TimerBlueprint {
    private SocketWrapper socket;
    public ClientFork(SocketWrapper socket) {
        this.socket = socket;
    }

    @Override
    public void run() {
        try {
            DataInputStream sockIn = new DataInputStream(socket.getInputStream());
            DataOutputStream sockOut = new DataOutputStream(socket.getOutputStream());
            String systemid = sockIn.readUTF();
            switch(systemid.charAt(0)) {
                case '?':
                    break;
                case '@':
                    systemid = systemid.substring(1);
                    int secret = Integer.getInteger(systemid.substring(systemid.indexOf('#') + 1)); // secret number
                    systemid = systemid.substring(0, systemid.indexOf('#'));                        // hostname

                    // --- update online/registered systems list - retains registration order ---
                    int index = GlobalDeps.registeredsystems.containsName(systemid);
                    if(index > 0) GlobalDeps.registeredsystems.set(index, new GlobalDeps.ClientBundle(systemid, (int) OF.getUnixTime(), -1));
                    else GlobalDeps.registeredsystems.set(index, new GlobalDeps.ClientBundle(systemid, (int)OF.getUnixTime(), -1));

                    index = GlobalDeps.onlinesystems.containsName(systemid);
                    if(index > 0) GlobalDeps.onlinesystems.set(index, new GlobalDeps.ClientBundle(systemid, (int)OF.getUnixTime(), -1));
                    else GlobalDeps.onlinesystems.set(index, new GlobalDeps.ClientBundle(systemid, (int)OF.getUnixTime(), -1));

                    OF.serialize(GlobalDeps.registeredsystems, "registeredsystems");
                    // get commands
                    for(GlobalDeps.ClientBundle command: GlobalDeps.commands) {
                        // check if this is a match for one of the commands
                        if(command.name.equals(systemid) && command.secret.equals(secret)) {
                            for(String formattedCommand: command.commands)
                                sockOut.writeUTF(formattedCommand);
                        }
                    }
                    break;
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    public boolean isRepeating() {
        return false;
    }
    @Override
    public boolean isVital() {
        return false;
    }
    @Override
    public long getRefreshRate() {
        return 0;
    }
    // doesn't do anything yet
    @Override
    public int getThreadPriority() {
        return 0;
    }
}

