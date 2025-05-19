package org.eldrygo.KothTNT.Plugin.Handlers;

import org.bukkit.Bukkit;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.command.TabCompleter;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

public class KothTNTTabCompleter implements TabCompleter {

    private static final List<String> MAIN_COMMANDS = Arrays.asList(
            "start", "stop", "status", "randombomb", "givebomb", "takebomb", "resetall", "help", "reload", "points"
    );

    private static final List<String> POINTS_SUBCOMMANDS = Arrays.asList(
            "add", "remove", "set", "get", "resetall", "help"
    );

    @Override
    public @Nullable List<String> onTabComplete(@NotNull CommandSender sender,
                                                @NotNull Command command,
                                                @NotNull String alias,
                                                @NotNull String[] args) {
        if (!sender.hasPermission("kothtnt.admin") && !sender.isOp()) return Collections.emptyList();

        if (args.length == 1) {
            return partial(args[0], MAIN_COMMANDS);
        }

        // Handle `/kothtnt points ...`
        if (args[0].equalsIgnoreCase("points")) {
            if (args.length == 2) {
                return partial(args[1], POINTS_SUBCOMMANDS);
            }

            String sub = args[1].toLowerCase();
            if (Arrays.asList("add", "remove", "set", "get").contains(sub)) {
                if (args.length == 3) {
                    return partial(args[2], getOnlinePlayerNames());
                }
                if (args.length == 4 && (args[1].equals("add") || args[1].equals("remove") || args[1].equals("set"))) {
                    return Collections.singletonList("amount");

                }
            }
        }

        // Handle givebomb/takebomb
        if ((args[0].equalsIgnoreCase("givebomb") || args[0].equalsIgnoreCase("takebomb")) && args.length == 2) {
            return partial(args[1], getOnlinePlayerNames());
        } else if (args[0].equalsIgnoreCase("randombomb") && args.length == 2) {
            return Collections.singletonList("amount");
        } else if (args[0].equalsIgnoreCase("start") && args.length == 2) {
            return Collections.singletonList("players");
        }

        return Collections.emptyList();
    }

    private List<String> getOnlinePlayerNames() {
        List<String> names = new ArrayList<>();
        Bukkit.getOnlinePlayers().forEach(player -> names.add(player.getName()));
        return names;
    }

    private List<String> partial(String input, List<String> options) {
        List<String> result = new ArrayList<>();
        for (String option : options) {
            if (option.toLowerCase().startsWith(input.toLowerCase())) {
                result.add(option);
            }
        }
        return result;
    }
}
