package org.eldrygo.RTNTTag.Game.Managers;

import net.md_5.bungee.api.ChatMessageType;
import net.md_5.bungee.api.chat.TextComponent;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.scheduler.BukkitRunnable;
import org.eldrygo.RTNTTag.Plugin.Managers.ConfigManager;
import org.eldrygo.RTNTTag.Plugin.Utils.ChatUtils;
import org.eldrygo.RTNTTag.RTNTTag;

public class HUDManager {
    private final ConfigManager configManager;
    private final BombManager bombManager;
    private final RTNTTag plugin;
    private GameManager gameManager;

    private BukkitRunnable task;

    public HUDManager(ConfigManager configManager, BombManager bombManager, RTNTTag plugin, GameManager gameManager) {
        this.configManager = configManager;
        this.bombManager = bombManager;
        this.plugin = plugin;
        this.gameManager = gameManager;
    }

    public void startHUDTask() {
        plugin.getLogger().info("HUD runnable started.");
        task = new BukkitRunnable() {
            @Override
            public void run() {
                if (gameManager.getCurrentState() == GameManager.GameState.STOPPED) return;
                for (Player p : Bukkit.getOnlinePlayers()) {
                    if (!p.isOp()) {
                        String actionMessage;

                        if (bombManager.getPlayersWithBomb().contains(p)) {
                            actionMessage = configManager.getMessageConfig().getString("actionbar.with_bomb");
                        } else {
                            actionMessage = configManager.getMessageConfig().getString("actionbar.without_bomb");
                        }

                        p.spigot().sendMessage(
                                ChatMessageType.ACTION_BAR,
                                new TextComponent(ChatUtils.formatColor(actionMessage))
                        );
                    }
                }
            }
        };

        task.runTaskTimer(plugin, 10L, 20L); // cada 2 segundos
    }
    public void stopHUDTask() {
        if (task != null) {
            task.cancel();
            plugin.getLogger().info("HUD runnable stopped.");
        }
    }
    public void setGameManager(GameManager gameManager) {
        this.gameManager = gameManager;
    }
}

