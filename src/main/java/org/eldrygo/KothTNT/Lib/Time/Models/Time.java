package org.eldrygo.KothTNT.Lib.Time.Models;

public class Time {
    private long startTime;
    private boolean running;
    private boolean paused;
    private long countdownTime; // en segundos

    public Time(long countdownTimeInSeconds) {
        this.countdownTime = countdownTimeInSeconds;
        this.startTime = 0;
        this.running = false;
        this.paused = false;
    }

    public void start() {
        if (running) {
            return;
        }
        this.startTime = System.currentTimeMillis();
        this.running = true;
        this.paused = false;
    }

    public void stop() {
        this.running = false;
        this.paused = false;
    }

    public boolean isRunning() {
        return running && !paused;
    }

    public long getRemainingTime() {
        if (!running) return countdownTime;

        long elapsedSeconds = (System.currentTimeMillis() - startTime) / 1000;
        long remaining = countdownTime - elapsedSeconds;

        return Math.max(remaining, 0);
    }

    public String formatTime(long seconds) {
        long hours = seconds / 3600;
        long minutes = (seconds % 3600) / 60;
        long secs = seconds % 60;

        if (hours > 0) {
            return String.format("%02d:%02d:%02d", hours, minutes, secs);
        } else {
            return String.format("%02d:%02d", minutes, secs);
        }
    }
}
