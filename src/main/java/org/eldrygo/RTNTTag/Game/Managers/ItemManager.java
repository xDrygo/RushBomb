package org.eldrygo.RTNTTag.Game.Managers;

import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.PlayerInventory;
import org.bukkit.inventory.meta.ItemMeta;
import org.eldrygo.RTNTTag.Plugin.Utils.ChatUtils;
import org.eldrygo.RTNTTag.RTNTTag;

import java.util.ArrayList;
import java.util.List;

public class ItemManager {

    private final RTNTTag plugin;

    public ItemManager(RTNTTag plugin) {
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
        ItemStack item = new ItemStack(Material.QUARTZ);

        ItemMeta meta = item.getItemMeta();

        if (meta != null) {
            String rawName = plugin.getConfig().getString("settings.item.name");
            List<String> rawLore = plugin.getConfig().getStringList("settings.item.lore");
            int model = plugin.getConfig().getInt("settings.item.custom_model_data");


            List<String> lore = new ArrayList<>();
            for (String line : rawLore) {
                lore.add(ChatUtils.formatColor(line));
            }

            meta.setDisplayName(ChatUtils.formatColor(rawName));
            meta.setLore(lore);
            meta.setCustomModelData(model);

            item.setItemMeta(meta);
        }

        return item;
    }
}
