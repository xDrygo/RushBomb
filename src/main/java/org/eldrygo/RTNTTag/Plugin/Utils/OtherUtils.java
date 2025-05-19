package org.eldrygo.RTNTTag.Plugin.Utils;

import org.bukkit.Bukkit;
import org.bukkit.scoreboard.Team;
import org.eldrygo.RTNTTag.RTNTTag;

public class OtherUtils {

    private static RTNTTag plugin;

    public OtherUtils(RTNTTag plugin) {
        OtherUtils.plugin = plugin;
    }

    public static Team getBombTeam() {
        String teamName = plugin.getConfig().getString("settings.team");

        if (teamName == null) {
            teamName = "rTNTTag";
        }

        Team team = Bukkit.getScoreboardManager().getMainScoreboard().getTeam(teamName);

        if (team == null) {
            Bukkit.getScoreboardManager().getMainScoreboard().registerNewTeam(teamName);
            team = Bukkit.getScoreboardManager().getMainScoreboard().getTeam(teamName);
        }

        return team;
    }

    public static boolean validInt(String value) {
        try {
            int number = Integer.parseInt(value);
            return number >= 0;
        } catch (NumberFormatException e) {
            return false;
        }
    }
    public static void setPlugin(RTNTTag plugin) {
        OtherUtils.plugin = plugin;
    }
}

