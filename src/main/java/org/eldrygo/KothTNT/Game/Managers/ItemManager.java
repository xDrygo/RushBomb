package org.eldrygo.KothTNT.Game.Managers;

import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.PlayerInventory;
import org.bukkit.inventory.meta.ItemMeta;
import org.eldrygo.KothTNT.KothTNT;
import org.eldrygo.KothTNT.Plugin.Utils.ChatUtils;

import java.util.ArrayList;
import java.util.List;

public class ItemManager {

    private final KothTNT plugin;

    public ItemManager(KothTNT plugin) {
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
        ItemStack item = new ItemStack(Material.HEART_OF_THE_SEA);

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
