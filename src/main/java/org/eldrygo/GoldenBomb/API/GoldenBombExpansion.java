package org.eldrygo.GoldenBomb.API;

import me.clip.placeholderapi.expansion.PlaceholderExpansion;
import org.bukkit.OfflinePlayer;
import org.eldrygo.GoldenBomb.GoldenBomb;
import org.eldrygo.GoldenBomb.Lib.Time.Managers.TimeManager;
import org.jetbrains.annotations.NotNull;

public class GoldenBombExpansion extends PlaceholderExpansion {
    private final GoldenBomb plugin;
    private final TimeManager timeManager;

    public GoldenBombExpansion(GoldenBomb plugin, TimeManager timeManager) {
        this.plugin = plugin;
        this.timeManager = timeManager;
    }

    @Override
    public @NotNull String getIdentifier() {
        return "goldenbomb";
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
            return timeManager.getFormattedTime("game");
        }
        return null;
    }
}
