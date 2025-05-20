package org.eldrygo.KothTNT.Plugin.Utils;

import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.scoreboard.Team;
import org.eldrygo.KothTNT.KothTNT;

import java.util.ArrayList;
import java.util.List;

public class OtherUtils {
    private static KothTNT plugin;

    public OtherUtils(KothTNT plugin) {
    OtherUtils.plugin = plugin;
}

    public static Team getBombTeam() {
        String teamName = plugin.getConfig().getString("settings.team");

        if (teamName == null) {
            teamName = "GoldenBomb";
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

    public static List<Player> getGamePlayers() {
        List<Player> players = new ArrayList<>();

        for (Player p : Bukkit.getOnlinePlayers()) {
            if (p.isOp()) continue;
            players.add(p);
        }

        return players;
    }

    public static void setPlugin(KothTNT plugin) {
        OtherUtils.plugin = plugin;
    }
}
