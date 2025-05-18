package org.eldrygo.GoldenBomb.Plugin.Handlers;

import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.command.TabCompleter;
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
            completions.add("givebombs");
            completions.add("resetall");
            completions.add("status");
            completions.add("reload");
            completions.add("help");
        }
        if (args.length == 2) {
            completions.add("number");
        }
        return completions;
    }
}

