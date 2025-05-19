package org.eldrygo.RTNTTag.Lib.Time.Listeners;

import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.eldrygo.RTNTTag.Game.Managers.GameManager;
import org.eldrygo.RTNTTag.Lib.Time.Event.TimerEndEvent;

public class TimerListener implements Listener {
    private final GameManager gameManager;

    public TimerListener(GameManager gameManager) {
        this.gameManager = gameManager;
    }

    @EventHandler
    public void onTimerEnd(TimerEndEvent event) {
        if (gameManager.getCurrentState() != GameManager.GameState.RUNNING) return;
        String timer = event.getTimerId();

        if (timer.equals("game")) {
            gameManager.stopGame();
        }
    }
}
