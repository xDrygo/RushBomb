package org.eldrygo.PacTNTMan.Plugin.Utils;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Color;
import org.bukkit.scoreboard.Team;
import org.eldrygo.PacTNTMan.PacTNTMan;

public class OtherUtils {
    private static PacTNTMan plugin;

    public static Team getBombTeam() {
        String teamName = plugin.getConfig().getString("settings.team");
        if (teamName == null) {
            teamName = "PacTNTManTeam";
        }
        Team team = Bukkit.getScoreboardManager().getMainScoreboard().getTeam(teamName);
        if (team == null) {
            Bukkit.getScoreboardManager().getMainScoreboard().registerNewTeam(teamName);
            team = Bukkit.getScoreboardManager().getMainScoreboard().getTeam(teamName);
        }
        if (team.getColor() != ChatColor.RED) {
            team.setColor(ChatColor.RED);
        }
        return team;
    }
    public static Team getPacManTeam() {
        String teamName = plugin.getConfig().getString("settings.pacman_team");
        if (teamName == null) {
            teamName = "PacManTeam";
        }
        Team team = Bukkit.getScoreboardManager().getMainScoreboard().getTeam(teamName);
        if (team == null) {
            Bukkit.getScoreboardManager().getMainScoreboard().registerNewTeam(teamName);
            team = Bukkit.getScoreboardManager().getMainScoreboard().getTeam(teamName);
        }

        if (team.getColor() != ChatColor.GOLD) {
            team.setColor(ChatColor.GOLD);
        }

        return team;
    }

    public static void setPlugin(PacTNTMan plugin) {
        OtherUtils.plugin = plugin;
    }
}
