package pl.symentis;

public class SharedObject {
    public long counter = 0;
    public volatile int turn = 0;
    public volatile boolean[] wants_to_enter = new boolean[2];
}
