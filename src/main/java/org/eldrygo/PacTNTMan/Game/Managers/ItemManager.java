package org.eldrygo.PacTNTMan.Game.Managers;

import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.PlayerInventory;
import org.bukkit.inventory.meta.ItemMeta;
import org.eldrygo.PacTNTMan.PacTNTMan;
import org.eldrygo.PacTNTMan.Plugin.Utils.ChatUtils;

public class ItemManager {
    private final PacTNTMan plugin;

    public ItemManager(PacTNTMan plugin) {
        this.plugin = plugin;
    }

    public void setBombHead(Player player) {
        PlayerInventory playerInv = player.getInventory();
        playerInv.setHelmet(getBombItem());
    }

    public void removeBombHead(Player player) {
        PlayerInventory playerInv = player.getInventory();
        playerInv.setHelmet(new ItemStack(Material.AIR));
    }

    public ItemStack getBombItem() {
        ItemStack item = new ItemStack(Material.GOLD_INGOT);

        ItemMeta meta = item.getItemMeta();

        if (meta != null) {
            String rawName = plugin.getConfig().getString("settings.item.name");
            int model = plugin.getConfig().getInt("settings.item.custom_model_data");

            meta.setDisplayName(ChatUtils.formatColor(rawName));
            meta.setCustomModelData(model);

            item.setItemMeta(meta);
        }

        return item;
    }
}
