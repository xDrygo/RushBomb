package org.eldrygo.KothTNT.Lib.Time.Managers;

import org.bukkit.Bukkit;
import org.eldrygo.KothTNT.Game.Managers.GameManager;
import org.eldrygo.KothTNT.KothTNT;
import org.eldrygo.KothTNT.Lib.Time.Events.TimerEndEvent;
import org.eldrygo.KothTNT.Lib.Time.Models.Time;

import java.util.HashMap;
import java.util.Map;

public class TimerManager {
    private final Map<String, Time> timers = new HashMap<>();
    private final KothTNT plugin;

    private GameManager gameManager;

    public TimerManager(KothTNT plugin, GameManager gameManager) {
        this.plugin = plugin;
        this.gameManager = gameManager;
    }

    public void createTimer(String id, long durationInSeconds) {
        if (timers.containsKey(id)) return;

        Time time = new Time(durationInSeconds);
        time.start();
        timers.put(id, time);
    }

    public void removeTimer(String id) {
        timers.remove(id);
    }

    public long getRemainingTime(String id) {
        Time time = timers.get(id);
        if (time == null) return -1;
        return time.getRemainingTime();
    }

    public String getFormattedTime(String id) {
        Time time = timers.get(id);
        if (time == null) return "--:--";
        return time.formatTime(time.getRemainingTime());
    }

    public boolean isRunning(String id) {
        Time time = timers.get(id);
        return time != null && time.isRunning();
    }

    public boolean isFinished(String id) {
        Time time = timers.get(id);
        return time != null && time.getRemainingTime() <= 0;
    }

    public void stopTimer(String id) {
        Time time = timers.get(id);
        if (time != null) time.stop();
    }

    public void clearAll() {
        timers.clear();
    }

    // Método que se debe llamar periódicamente para actualizar timers
    public void updateTimers() {
        timers.entrySet().removeIf(entry -> {
            String id = entry.getKey();
            Time time = entry.getValue();

            if (gameManager.getCurrentState() != GameManager.GameState.RUNNING) return false;
            if (!time.isRunning()) return false;

            if (time.getRemainingTime() <= 0) {
                time.stop();
                Bukkit.getScheduler().runTask(plugin, () ->
                        Bukkit.getPluginManager().callEvent(new TimerEndEvent(id)));
                return true;
            }

            return false;
        });
    }
    public void setGameManager(GameManager gameManager) {
        this.gameManager = gameManager;
    }
}
