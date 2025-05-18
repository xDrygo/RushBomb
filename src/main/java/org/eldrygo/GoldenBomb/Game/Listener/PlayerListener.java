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

    public PlayerListener(GameManager gameManager, BombManager bombManager) {
        this.gameManager = gameManager;
        this.bombManager = bombManager;
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
    }

    @EventHandler
    public void onInventoryClick(InventoryClickEvent event) {
        if (!(event.getWhoClicked() instanceof Player player)) return;
        if (player.isOp()) return;

        if (gameManager.getCurrentState() != GameManager.GameState.RUNNING) {
            return;
        }

        // Slot 39 es la cabeza
        if (event.getSlot() == 39) {
            event.setCancelled(true);
        }

        // Protección adicional por tipo de slot (ARMOR) y raw slot
        if (event.getSlotType() == InventoryType.SlotType.ARMOR && event.getRawSlot() == 5) {
            event.setCancelled(true);
        }
    }

    @EventHandler
    public void onInventoryDrag(InventoryDragEvent event) {
        if (!(event.getWhoClicked() instanceof Player player)) return;
        if (player.isOp()) return;

        if (gameManager.getCurrentState() != GameManager.GameState.RUNNING) {
            return;
        }

        if (event.getRawSlots().contains(39)) {
            event.setCancelled(true);
        }
    }
}
