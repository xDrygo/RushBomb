package org.eldrygo.GoldenBomb.Plugin.Handlers;

import org.bukkit.Bukkit;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.command.TabCompleter;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.List;

public class GoldenBombTabCompleter implements TabCompleter {
    @Override
    public List<String> onTabComplete(@NotNull CommandSender sender, @NotNull Command command, @NotNull String alias, String[] args) {
        List<String> completions = new ArrayList<>();

        if (args.length == 1) {
            completions.add("start");
            completions.add("stop");
            completions.add("randombomb");
            completions.add("givebomb");
            completions.add("takebomb");
            completions.add("resetall");
            completions.add("status");
            completions.add("reload");
            completions.add("help");
        }
        if (args.length == 2 && args[0].equals("randombomb")) {
            completions.add("number");
        } else if (args.length == 2 && (args[0].equals("givebomb") || args[0].equals("takebomb"))) {
            completions.addAll(getPlayers());
        }
        return completions;
    }

    private List<String> getPlayers() {
        List<String> playerNames = new ArrayList<>();
        for (Player p : Bukkit.getOnlinePlayers()) {
            String pName = p.getName();
            playerNames.add(pName);
        }
        return playerNames;
    }
}

