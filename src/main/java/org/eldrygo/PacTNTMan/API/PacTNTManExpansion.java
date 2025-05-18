package org.eldrygo.PacTNTMan.API;

import me.clip.placeholderapi.expansion.PlaceholderExpansion;
import org.bukkit.OfflinePlayer;
import org.eldrygo.PacTNTMan.Lib.Time.Managers.TimerManager;
import org.eldrygo.PacTNTMan.PacTNTMan;
import org.eldrygo.PacTNTMan.Plugin.Managers.ConfigManager;
import org.jetbrains.annotations.NotNull;

public class PacTNTManExpansion extends PlaceholderExpansion {
    private final PacTNTMan plugin;
    private final TimerManager timerManager;
    private final ConfigManager configManager;

    public PacTNTManExpansion(PacTNTMan plugin, TimerManager timerManager, ConfigManager configManager) {
        this.plugin = plugin;
        this.timerManager = timerManager;
        this.configManager = configManager;
    }

    @Override
    public @NotNull String getIdentifier() {
        return "pactntman";
    }

    @Override
    public @NotNull String getAuthor() {
        return "Drygo, Turtle Club";
    }

    @Override
    public @NotNull String getVersion() {
        return plugin.getDescription().getVersion();
    }

    @Override
    public String onRequest(OfflinePlayer player, String params) {
        if (params.equals("timer")) {
            return timerManager.getFormattedTime("game");
        }
        if (params.equals("bombtimer")) {
            if (player == null) {
                return "N/A";
            }
            if (timerManager.isRunning(player.getName())) {
                return configManager.getMessageConfig().getString("bossbar.bombtimer")
                        .replace("%time%", timerManager.getFormattedTime(player.getName()));
            }

            return " ";
        }
        return null;
    }
}
