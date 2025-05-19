package org.eldrygo.KothTNT.API;

import me.clip.placeholderapi.expansion.PlaceholderExpansion;
import org.bukkit.OfflinePlayer;
import org.eldrygo.KothTNT.KothTNT;
import org.eldrygo.KothTNT.Lib.Points.Managers.PointsManager;
import org.eldrygo.KothTNT.Lib.Time.Managers.TimerManager;
import org.eldrygo.KothTNT.Plugin.Managers.ConfigManager;
import org.jetbrains.annotations.NotNull;

public class KothTNTExpansion extends PlaceholderExpansion {
    private final KothTNT plugin;
    private final TimerManager timerManager;
    private final ConfigManager configManager;
    private final PointsManager pointsManager;

    public KothTNTExpansion(KothTNT plugin, TimerManager timeManager, ConfigManager configManager, PointsManager pointsManager) {
        this.plugin = plugin;
        this.timerManager = timeManager;
        this.configManager = configManager;
        this.pointsManager = pointsManager;
    }

    @Override
    public @NotNull String getIdentifier() {
        return "kothtnt";
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
        if (params.equals("points")) {
            if (player == null || !player.isOnline()) {
                return "N/A";
            }
            return configManager.getMessageConfig().getString("bossbar.points")
                    .replace("%points%", String.valueOf(pointsManager.getPoints(player.getPlayer())));
        }
        return null;
    }
}

