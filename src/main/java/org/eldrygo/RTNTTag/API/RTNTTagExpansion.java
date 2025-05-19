package org.eldrygo.RTNTTag.API;

import me.clip.placeholderapi.expansion.PlaceholderExpansion;
import org.bukkit.OfflinePlayer;
import org.eldrygo.RTNTTag.Lib.Time.Managers.TimerManager;
import org.eldrygo.RTNTTag.RTNTTag;
import org.jetbrains.annotations.NotNull;

public class RTNTTagExpansion extends PlaceholderExpansion {
    private final RTNTTag plugin;
    private final TimerManager timerManager;

    public RTNTTagExpansion(RTNTTag plugin, TimerManager timeManager) {
        this.plugin = plugin;
        this.timerManager = timeManager;
    }

    @Override
    public @NotNull String getIdentifier() {
        return "rtnttag";
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
        return null;
    }
}
