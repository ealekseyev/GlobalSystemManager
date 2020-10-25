package ml.fusion.server.Support;

import ml.fusion.server.Support.GlobalDeps;

import java.util.ArrayList;

public final class BundledArrayList extends ArrayList<GlobalDeps.ClientBundle> {
    public int containsName(String name) {
        for(int i = 0; i < super.size(); i++) {
            if(super.get(i).name.equals(name))
                return i;
        }
        return -1;
    }
    public int containsTime(int time) {
        for(int i = 0; i < super.size(); i++) {
            if(super.get(i).unixtime == time)
                return i;
        }
        return -1;
    }
    public int containsSecret(int secret) {
        for(int i = 0; i < super.size(); i++) {
            if(super.get(i).secret == secret)
                return i;
        }
        return -1;
    }
}
