package org.eldrygo.GoldenBomb.Utils;

import org.bukkit.Bukkit;
import org.bukkit.scoreboard.Team;
import org.eldrygo.GoldenBomb.GoldenBomb;

public class OtherUtils {
    private static GoldenBomb plugin;

    public OtherUtils(GoldenBomb plugin) {
        OtherUtils.plugin = plugin;
    }

    public static Team getBombTeam() {
        String teamName = plugin.getConfig().getString("settings.bomb_team");
        assert teamName != null;
        return Bukkit.getScoreboardManager().getMainScoreboard().getTeam(teamName);
    }

    public static boolean validInt(String value) {
        try {
            int number = Integer.parseInt(value);
            return number >= 0;
        } catch (NumberFormatException e) {
            return false;
        }
    }
}
