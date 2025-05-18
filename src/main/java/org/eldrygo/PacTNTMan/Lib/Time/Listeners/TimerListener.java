package org.eldrygo.PacTNTMan.Lib.Time.Listeners;

import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.eldrygo.PacTNTMan.Game.Managers.BombManager;
import org.eldrygo.PacTNTMan.Game.Managers.GameManager;
import org.eldrygo.PacTNTMan.Lib.Time.Events.TimerEndEvent;

public class TimerListener implements Listener {
    private final GameManager gameManager;
    private final BombManager bombManager;

    public TimerListener(GameManager gameManager, BombManager bombManager) {
        this.gameManager = gameManager;
        this.bombManager = bombManager;
    }

    @EventHandler
    public void onTimerEnd(TimerEndEvent event) {
        if (gameManager.getCurrentState() != GameManager.GameState.RUNNING) return;
        String timer = event.getTimerId();

        if (timer.equals("game")) {
            gameManager.stopGame();
        }

        for (Player p : Bukkit.getOnlinePlayers()) {
            String pName = p.getName();

            if (timer.equals(pName)) {
                bombManager.bombExplode(p);
            }
        }
    }
}
