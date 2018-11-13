package domain;

import javax.swing.*;

public class TimeController {

    private long startTime;
    private boolean running;

    public TimeController(){
        running = false;
    }

    public void startTimer(){
        running = true;
        startTime = System.currentTimeMillis();
    }
    public void stopTimer(){
        running = false;
    }
    public boolean isRunning(){
        return  running;
    }
    public long getRunningTimeInSeconds(){
       return (System.currentTimeMillis() - startTime) / 1000;
    }
    public long getRunningTimeInMiliseconds() { return System.currentTimeMillis()- startTime;}
}
