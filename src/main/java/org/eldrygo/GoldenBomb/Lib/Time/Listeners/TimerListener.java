package org.eldrygo.GoldenBomb.Lib.Time.Listeners;

import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.eldrygo.GoldenBomb.Game.Managers.GameManager;
import org.eldrygo.GoldenBomb.Lib.Time.Events.TimerEndEvent;

public class TimerListener implements Listener {
    private final GameManager gameManager;

    public TimerListener(GameManager gameManager) {
        this.gameManager = gameManager;
    }

    @EventHandler
    public void onTimerEnd(TimerEndEvent event) {
        gameManager.stopGame();
    }
}