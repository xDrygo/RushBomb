package org.eldrygo.PacTNTMan.Game.Listeners;

import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.eldrygo.PacTNTMan.Game.Managers.BombManager;
import org.eldrygo.PacTNTMan.Game.Managers.GameManager;
import org.eldrygo.PacTNTMan.Game.Managers.GroupManager;

public class PacManListener implements Listener {
    private final GameManager gameManager;
    private final GroupManager groupManager;
    private final BombManager bombManager;

    public PacManListener(GameManager gameManager, GroupManager groupManager, BombManager bombManager) {
        this.gameManager = gameManager;
        this.groupManager = groupManager;
        this.bombManager = bombManager;
    }

    @EventHandler
    public void giveBomb(EntityDamageByEntityEvent event) {
        if (!(event.getEntity() instanceof Player victim)) return;
        if (!(event.getDamager() instanceof Player damager)) return;
        if (gameManager.getCurrentState() != GameManager.GameState.RUNNING) return;

        if (groupManager.getPacMan() != null && !groupManager.getPacMan().equals(damager)) return;
        if (bombManager.getPlayersWithBomb().contains(victim)) return;

        bombManager.addBomb(victim);
    }
}