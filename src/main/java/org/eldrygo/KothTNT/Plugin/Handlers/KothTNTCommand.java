package org.eldrygo.KothTNT.Plugin.Handlers;

import org.bukkit.Bukkit;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.eldrygo.KothTNT.Game.Managers.BombManager;
import org.eldrygo.KothTNT.Game.Managers.GameManager;
import org.eldrygo.KothTNT.Lib.Points.Managers.PointsManager;
import org.eldrygo.KothTNT.Plugin.Managers.ConfigManager;
import org.eldrygo.KothTNT.Plugin.Utils.ChatUtils;
import org.eldrygo.KothTNT.Plugin.Utils.LoadUtils;
import org.eldrygo.KothTNT.Plugin.Utils.OtherUtils;
import org.jetbrains.annotations.NotNull;

import java.util.Arrays;
import java.util.List;

public class KothTNTCommand implements CommandExecutor {
    private final ChatUtils chatUtils;
    private final GameManager gameManager;
    private final BombManager bombManager;
    private final ConfigManager configManager;
    private final LoadUtils loadUtils;
    private final PointsManager pointsManager;

    public KothTNTCommand(ChatUtils chatUtils, GameManager gameManager, BombManager bombManager, ConfigManager configManager, LoadUtils loadUtils, PointsManager pointsManager) {
        this.chatUtils = chatUtils;
        this.gameManager = gameManager;
        this.bombManager = bombManager;
        this.configManager = configManager;
        this.loadUtils = loadUtils;
        this.pointsManager = pointsManager;
    }

    @Override
    public boolean onCommand(CommandSender sender, @NotNull Command command, @NotNull String label, String[] args) {
        if (!sender.hasPermission("kothtnt.admin") && !sender.isOp()) {
            sender.sendMessage(chatUtils.getMessage("error.no_permission", null));
            return true;
        }
        if (args.length == 0) {
            sender.sendMessage(chatUtils.getMessage("command.usage", null));
            return true;
        }

        switch (args[0].toLowerCase()) {
            case "start" -> {
                if (gameManager.getCurrentState() == GameManager.GameState.RUNNING) {
                    sender.sendMessage(chatUtils.getMessage("command.start.already", null));
                } else {
                    if (args.length < 2) {
                        sender.sendMessage(chatUtils.getMessage("command.start.no_args", null));
                        return false;
                    }

                    String playersToKillStr = args[1];

                    if (!OtherUtils.validInt(playersToKillStr)) {
                        sender.sendMessage(chatUtils.getMessage("command.start.invalid_int", null));
                        return false;
                    }

                    int playersToKill = Integer.parseInt(playersToKillStr);

                    gameManager.startGame(playersToKill);
                    sender.sendMessage(chatUtils.getMessage("command.start.success", null));
                }
            }


            case "stop" -> {
                if (gameManager.getCurrentState() == GameManager.GameState.STOPPED) {
                    sender.sendMessage(chatUtils.getMessage("command.stop.already", null));
                } else {
                    gameManager.stopGame();
                    sender.sendMessage(chatUtils.getMessage("command.stop.success", null));
                }
            }
            case "status" -> {
                String status = "INVALID";

                if (gameManager.getCurrentState() == GameManager.GameState.STOPPED) {
                    status = "STOPPED";
                } else if (gameManager.getCurrentState() == GameManager.GameState.RUNNING) {
                    status = "RUNNING";
                }
                sender.sendMessage(chatUtils.getMessage("command.status.success", null).replace("%status%", status));
            }
            case "randombomb" -> {
                if (args.length < 2) {
                    sender.sendMessage(chatUtils.getMessage("command.randombomb.no_args", null));
                    return false;
                }

                String playersIntStr = args[1];

                if (!OtherUtils.validInt(playersIntStr)) {
                    sender.sendMessage(chatUtils.getMessage("command.randombomb.invalid_int", null));
                    return false;
                }

                int playersInt = Integer.parseInt(playersIntStr);

                if (playersInt > Bukkit.getOnlinePlayers().size()) {
                    sender.sendMessage(chatUtils.getMessage("command.randombomb.int_is_more_than_players", null));
                    return false;
                }

                bombManager.giveRandomBombs(playersInt);
                sender.sendMessage(chatUtils.getMessage("command.randombomb.success", null).replace("%int%", playersIntStr));
            }
            case "givebomb" -> {
                if (args.length < 2) {
                    sender.sendMessage(chatUtils.getMessage("command.adminbomb.no_args", null));
                    return false;
                }
                String targetName = args[1];
                Player target = Bukkit.getPlayerExact(targetName);

                if (target == null) {
                    sender.sendMessage(chatUtils.getMessage("command.adminbomb.player_not_found", null));
                    return false;
                }
                bombManager.addBomb(target);
                sender.sendMessage(chatUtils.getMessage("command.adminbomb.give", null).replace("%target%", target.getName()));
            }
            case "takebomb" -> {
                if (args.length < 2) {
                    sender.sendMessage(chatUtils.getMessage("command.adminbomb.no_args", null));
                    return false;
                }
                String targetName = args[1];
                Player target = Bukkit.getPlayerExact(targetName);

                if (target == null) {
                    sender.sendMessage(chatUtils.getMessage("command.adminbomb.player_not_found", null).replace("%arg%", targetName));
                    return false;
                }
                bombManager.takeBomb(target);
                sender.sendMessage(chatUtils.getMessage("command.adminbomb.take", null).replace("%target%", target.getName()));
            }
            case "resetall" -> {
                gameManager.resetAll();
                sender.sendMessage(chatUtils.getMessage("command.resetall.success", null));
            }
            case "points" -> {
                handlePoints(sender, args);
            }
            case "help" -> {
                List<String> helpMessage = configManager.getMessageConfig().getStringList("command.help");

                for (String line : helpMessage) {
                    line = ChatUtils.formatColor(line);
                    sender.sendMessage(line);
                }
            }
            case "reload" -> {
                Player target = (sender instanceof Player) ? (Player) sender : null;
                try {
                    loadUtils.loadConfigFiles();
                } catch (Exception e) {
                    sender.sendMessage(chatUtils.getMessage("command.reload.error", target));
                    return false;
                }
                sender.sendMessage(chatUtils.getMessage("command.reload.success", target));
            }
            default -> sender.sendMessage(chatUtils.getMessage("command.usage", null));
        }
        return true;
    }
    private void handlePoints(CommandSender sender, String[] args) {
        if (args.length < 2) {
            sender.sendMessage(chatUtils.getMessage("command.points.usage", null));
            return;
        }

        String sub = args[1].toLowerCase();

        switch (sub) {
            case "add" -> {
                if (args.length < 4) {
                    sender.sendMessage(chatUtils.getMessage("command.points.add.usage", null));
                    return;
                }

                Player player = Bukkit.getPlayerExact(args[2]);
                if (player == null) {
                    sender.sendMessage(chatUtils.getMessage("command.points.invalid_player", null)
                            .replace("%p%", args[2]));
                    return;
                }

                int amount;
                try {
                    amount = Integer.parseInt(args[3]);
                } catch (NumberFormatException e) {
                    sender.sendMessage(chatUtils.getMessage("command.points.invalid_number", null));
                    return;
                }

                pointsManager.addPoints(player, amount);
                int totalPoints = pointsManager.getPoints(player);
                sender.sendMessage(chatUtils.getMessage("command.points.add.success", null)
                        .replace("%p%", player.getName())
                        .replace("%points%", String.valueOf(totalPoints)));
            }

            case "remove" -> {
                if (args.length < 4) {
                    sender.sendMessage(chatUtils.getMessage("command.points.remove.usage", null));
                    return;
                }

                Player player = Bukkit.getPlayerExact(args[2]);
                if (player == null) {
                    sender.sendMessage(chatUtils.getMessage("command.points.invalid_player", null)
                            .replace("%p%", args[2]));
                    return;
                }

                int amount;
                try {
                    amount = Integer.parseInt(args[3]);
                } catch (NumberFormatException e) {
                    sender.sendMessage(chatUtils.getMessage("command.points.invalid_number", null));
                    return;
                }

                pointsManager.removePoints(player, amount);
                int totalPoints = pointsManager.getPoints(player);
                sender.sendMessage(chatUtils.getMessage("command.points.remove.success", null)
                        .replace("%p%", player.getName())
                        .replace("%points%", String.valueOf(totalPoints)));
            }

            case "set" -> {
                if (args.length < 4) {
                    sender.sendMessage(chatUtils.getMessage("command.points.set.usage", null));
                    return;
                }

                Player player = Bukkit.getPlayerExact(args[2]);
                if (player == null) {
                    sender.sendMessage(chatUtils.getMessage("command.points.invalid_player", null)
                            .replace("%p%", args[2]));
                    return;
                }

                int amount;
                try {
                    amount = Integer.parseInt(args[3]);
                } catch (NumberFormatException e) {
                    sender.sendMessage(chatUtils.getMessage("command.points.invalid_number", null));
                    return;
                }

                pointsManager.setPoints(player, amount);
                sender.sendMessage(chatUtils.getMessage("command.points.set.success", null)
                        .replace("%p%", player.getName())
                        .replace("%points%", String.valueOf(amount)));
            }

            case "get" -> {
                if (args.length < 3) {
                    sender.sendMessage(chatUtils.getMessage("command.points.get.usage", null));
                    return;
                }

                Player player = Bukkit.getPlayerExact(args[2]);
                if (player == null) {
                    sender.sendMessage(chatUtils.getMessage("command.points.invalid_player", null)
                            .replace("%p%", args[2]));
                    return;
                }

                int totalPoints = pointsManager.getPoints(player);
                sender.sendMessage(chatUtils.getMessage("command.points.get.success", null)
                        .replace("%p%", player.getName())
                        .replace("%points%", String.valueOf(totalPoints)));
            }

            case "resetall" -> {
                for (Player p : Bukkit.getOnlinePlayers()) {
                    pointsManager.resetPoints(p);
                }
                sender.sendMessage(chatUtils.getMessage("command.points.reset.success", null));
            }

            case "help" -> {
                List<String> helpMessage = configManager.getMessageConfig().getStringList("command.points.help");
                for (String line : helpMessage) {
                    sender.sendMessage(ChatUtils.formatColor(line));
                }
            }

            default -> {
                sender.sendMessage(chatUtils.getMessage("command.points.usage", null));
            }
        }
    }
}
