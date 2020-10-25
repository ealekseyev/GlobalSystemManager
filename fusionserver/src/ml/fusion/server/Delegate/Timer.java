package ml.fusion.server.Delegate;
import ml.fusion.server.Support.OF;

import java.util.ArrayList;

// runs registered tasks

public class Timer {
    // ~~ Singleton ~~
    private static volatile Timer timerInstance;
    public static Timer getTimer() {
        if(timerInstance == null)
            timerInstance = new Timer();
        return timerInstance;
    }

    // ~~ Rest of the class ~~
    private volatile ArrayList<TimerBlueprint> timers;
    private volatile ArrayList<Long> timersLastRun;
    private volatile ArrayList<Thread> timerThreads;
    private long startTime;

    public void init() {
        this.startTime = System.currentTimeMillis();
    }

    public void register(TimerBlueprint timertoberegistered) {
        // if it only has to be run once, do it now
        if(!timertoberegistered.isRepeating()) {
            new Thread(timertoberegistered).start();// run it if it only needs to be run once
        } else {
            timers.add(timertoberegistered);        // add object to list of threads
            timersLastRun.add((long) startTime);    // this hasn't yet been run
            timerThreads.add(null);                 // no thread object (yet); reserve a slot in the list
        }
    }

    public void scheduleAndRun() {
        for(int i = 0; i < timers.size(); i++) {
            // if it's time to run
            TimerBlueprint cTimer = timers.get(i); // current timer object; not the thread
            long cTimerLastRun = timersLastRun.get(i);
            // if the timer is a vital timer and last thread is terminated
            // or if it's time to run the task and the previous thread is finished (and the task is not vital)
            if((cTimer.isVital() && !timerThreads.get(i).isAlive()) ||
                    (OF.getUnixTimeMillis() - cTimerLastRun > cTimer.getRefreshRate() && !cTimer.isVital() && !timerThreads.get(i).isAlive())) {
                timerThreads.set(i, new Thread(cTimer));
                timerThreads.get(i).setPriority(cTimer.getThreadPriority() < 10 ? cTimer.getThreadPriority() : 10); // ensure the value is less than 10
                timerThreads.get(i).start();
            }
        }
    }
}