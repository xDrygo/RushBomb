package org.eldrygo.GoldenBomb.Game.Listener;

import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.inventory.InventoryDragEvent;
import org.bukkit.event.inventory.InventoryType;
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

        if (gameManager.getCurrentState() != GameManager.GameState.STOPPED) return;

        if (!bombManager.getPlayersWithBomb().contains(player)) return;

        bombManager.takeBomb(player);
    }

    @EventHandler
    public void stealBomb(EntityDamageByEntityEvent event) {
        if (!(event.getEntity() instanceof Player victim)) return;
        if (!(event.getDamager() instanceof Player damager)) return;
        if (gameManager.getCurrentState() != GameManager.GameState.RUNNING) return;

        if (!bombManager.getPlayersWithBomb().contains(victim)) return;
        if (bombManager.getPlayersWithBomb().contains(damager)) return;


        bombManager.takeBomb(victim);
        bombManager.addBomb(damager);

        plugin.getLogger().warning("El jugador " + damager.getName() + " robó la Golden Bomb de " + victim.getName() + "!");
    }

    @EventHandler
    public void onInventoryClick(InventoryClickEvent event) {
        if (!(event.getWhoClicked() instanceof Player player)) return;
        if (player.isOp()) return;

        if (gameManager.getCurrentState() != GameManager.GameState.RUNNING) {
            plugin.getLogger().info("[DEBUG] El estado actual del juego no es RUNNING: " + gameManager.getCurrentState());
            return;
        }

        // Slot 39 es la cabeza
        if (event.getSlot() == 39) {
            event.setCancelled(true);
            plugin.getLogger().info("[DEBUG] " + player.getName() + " intentó quitarse el casco con click.");
        }

        // Protección adicional por tipo de slot (ARMOR) y raw slot
        if (event.getSlotType() == InventoryType.SlotType.ARMOR && event.getRawSlot() == 5) {
            event.setCancelled(true);
            plugin.getLogger().info("[DEBUG] " + player.getName() + " intentó modificar el slot de la cabeza.");
        }
    }

    @EventHandler
    public void onInventoryDrag(InventoryDragEvent event) {
        if (!(event.getWhoClicked() instanceof Player player)) return;
        if (player.isOp()) return;

        if (gameManager.getCurrentState() != GameManager.GameState.RUNNING) {
            plugin.getLogger().info("[DEBUG] El estado actual del juego no es RUNNING: " + gameManager.getCurrentState());
            return;
        }

        if (event.getRawSlots().contains(39)) {
            event.setCancelled(true);
            plugin.getLogger().info("[DEBUG] " + player.getName() + " intentó arrastrar un ítem al slot de la cabeza.");
        }
    }
}
