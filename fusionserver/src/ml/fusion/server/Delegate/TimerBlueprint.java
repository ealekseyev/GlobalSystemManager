package ml.fusion.server.Delegate;

// blueprint for timer: TODO:class or interface?
public abstract class TimerBlueprint implements Runnable {
    public abstract void run(); // code to run
    public long getRefreshRate()          // rerun every XX ms
    {
        return 0;
    }
    public boolean isVital()              // if true, task will run every loop cycle. Overrides getRefreshRate
    {
        return false;
    }
    public boolean isRepeating()          // does this task repeat or run just once?
    {
        return false;
    }
    public int getThreadPriority()       // get importance level, 1-3 least to most important
    {
        return 0;
    }
}
