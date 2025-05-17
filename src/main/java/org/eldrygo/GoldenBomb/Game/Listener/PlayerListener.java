package org.eldrygo.GoldenBomb.Game.Listener;

import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.event.player.PlayerJoinEvent;
import org.eldrygo.GoldenBomb.Game.Managers.BombManager;
import org.eldrygo.GoldenBomb.Game.Managers.GameManager;
import org.eldrygo.GoldenBomb.GoldenBomb;

public class PlayerListener implements Listener {

    private final GameManager gameManager;
    private final BombManager bombManager;
    private final GoldenBomb plugin;

    public PlayerListener(GameManager gameManager, BombManager bombManager, GoldenBomb plugin) {
        this.gameManager = gameManager;
        this.bombManager = bombManager;
        this.plugin = plugin;
    }

    @EventHandler
    public void onPlayerJoin(PlayerJoinEvent event) {
        Player player = event.getPlayer();
        plugin.getLogger().info("[DEBUG] PlayerJoinEvent: " + player.getName() + " se ha conectado.");

        if (gameManager.getCurrentState() != GameManager.GameState.STOPPED) {
            plugin.getLogger().info("[DEBUG] El estado actual del juego no es STOPPED: " + gameManager.getCurrentState());
            return;
        }

        if (!bombManager.getPlayersWithBomb().contains(player)) {
            plugin.getLogger().info("[DEBUG] El jugador " + player.getName() + " no tiene la bomba al unirse.");
            return;
        }

        plugin.getLogger().info("[DEBUG] El jugador " + player.getName() + " tenía la bomba. Se procede a quitársela.");
        bombManager.takeBomb(player);
    }

    @EventHandler
    public void stealBomb(EntityDamageByEntityEvent event) {
        if (!(event.getEntity() instanceof Player victim)) return;
        if (!(event.getDamager() instanceof Player damager)) return;

        plugin.getLogger().info("[DEBUG] EntityDamageByEntityEvent: " + damager.getName() + " golpeó a " + victim.getName());

        if (gameManager.getCurrentState() != GameManager.GameState.RUNNING) {
            plugin.getLogger().info("[DEBUG] El estado actual del juego no es RUNNING: " + gameManager.getCurrentState());
            return;
        }

        if (!bombManager.getPlayersWithBomb().contains(victim)) {
            plugin.getLogger().info("[DEBUG] La víctima " + victim.getName() + " no tiene la bomba.");
            return;
        }

        if (bombManager.getPlayersWithBomb().contains(damager)) {
            plugin.getLogger().info("[DEBUG] El agresor " + damager.getName() + " ya tiene una bomba.");
            return;
        }

        plugin.getLogger().info("[DEBUG] Robo de bomba: " + damager.getName() + " roba la bomba de " + victim.getName());

        bombManager.takeBomb(victim);
        bombManager.addBomb(damager);

        plugin.getLogger().warning("El jugador " + damager.getName() + " robó la Golden Bomb de " + victim.getName() + "!");
    }
    /*
    @EventHandler
    public void onPlayerJoin (PlayerJoinEvent event) {
        Player player = event.getPlayer();
        if (gameManager.getCurrentState() != GameManager.GameState.STOPPED) return;
        if (!bombManager.getPlayersWithBomb().contains(player)) return;

        bombManager.takeBomb(player);
    }

    @EventHandler
    public void stealBomb (EntityDamageByEntityEvent event) {
        if (!(event.getEntity() instanceof Player victim)) return;
        if (!(event.getDamager() instanceof Player damager)) return;

        if (gameManager.getCurrentState() != GameManager.GameState.RUNNING) return;

        if (!bombManager.getPlayersWithBomb().contains(victim)) return;
        if (bombManager.getPlayersWithBomb().contains(damager)) return;

        bombManager.takeBomb(victim);
        bombManager.addBomb(damager);

        plugin.getLogger().warning("El jugador " + damager.getName() + " robó la Golden Bomb de " + victim.getName() + "!");
    }
     */
}
