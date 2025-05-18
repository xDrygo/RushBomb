package org.eldrygo.PacTNTMan.Game.Listeners;

import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.event.player.PlayerJoinEvent;
import org.eldrygo.PacTNTMan.Game.Managers.BombManager;
import org.eldrygo.PacTNTMan.Game.Managers.GameManager;
import org.eldrygo.PacTNTMan.Game.Managers.GroupManager;
import org.eldrygo.PacTNTMan.PacTNTMan;

public class PlayerListener implements Listener {

    private final GameManager gameManager;
    private final BombManager bombManager;
    private final GroupManager groupManager;

    public PlayerListener(GameManager gameManager, BombManager bombManager, GroupManager groupManager) {
        this.gameManager = gameManager;
        this.bombManager = bombManager;
        this.groupManager = groupManager;
    }

    @EventHandler
    public void onPlayerJoin(PlayerJoinEvent event) {
        Player player = event.getPlayer();

        if (gameManager.getCurrentState() != GameManager.GameState.STOPPED) return;

        if (!bombManager.getPlayersWithBomb().contains(player)) return;

        bombManager.takeBomb(player);
    }

    @EventHandler
    public void giveBomb(EntityDamageByEntityEvent event) {
        if (!(event.getEntity() instanceof Player victim)) return;
        if (!(event.getDamager() instanceof Player damager)) return;
        if (gameManager.getCurrentState() != GameManager.GameState.RUNNING) return;

        if (!bombManager.getPlayersWithBomb().contains(damager)) return;
        if (bombManager.getPlayersWithBomb().contains(victim)) return;

        if (groupManager.getPacMan() != null && groupManager.getPacMan().equals(victim)) return;

        bombManager.addBomb(victim);
        bombManager.takeBomb(damager);
    }
}