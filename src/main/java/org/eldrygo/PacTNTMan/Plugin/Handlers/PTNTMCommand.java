package org.eldrygo.PacTNTMan.Plugin.Handlers;

import org.bukkit.Bukkit;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.eldrygo.PacTNTMan.Game.Managers.BombManager;
import org.eldrygo.PacTNTMan.Game.Managers.GameManager;
import org.eldrygo.PacTNTMan.Game.Managers.GroupManager;
import org.eldrygo.PacTNTMan.Plugin.Managers.ConfigManager;
import org.eldrygo.PacTNTMan.Plugin.Utils.ChatUtils;
import org.eldrygo.PacTNTMan.Plugin.Utils.LoadUtils;
import org.jetbrains.annotations.NotNull;

import java.util.List;

public class PTNTMCommand implements CommandExecutor {
    private final ChatUtils chatUtils;
    private final GameManager gameManager;
    private final LoadUtils loadUtils;
    private final ConfigManager configManager;
    private final GroupManager groupManager;
    private final BombManager bombManager;

    public PTNTMCommand(ChatUtils chatUtils, GameManager gameManager, LoadUtils loadUtils, ConfigManager configManager, GroupManager groupManager, BombManager bombManager) {
        this.chatUtils = chatUtils;
        this.gameManager = gameManager;
        this.loadUtils = loadUtils;
        this.configManager = configManager;
        this.groupManager = groupManager;
        this.bombManager = bombManager;
    }

    @Override
    public boolean onCommand(CommandSender sender, @NotNull Command command, @NotNull String label, String[] args) {
        if (!sender.hasPermission("pactntman.admin") && !sender.isOp()) {
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
                    if (groupManager.getPacMan() == null) {
                        sender.sendMessage(chatUtils.getMessage("command.start.no_pacman", null));
                        return false;
                    }
                    if (groupManager.getPlayersInt() < Bukkit.getOnlinePlayers().size()/2) {
                        sender.sendMessage(chatUtils.getMessage("command.start.insufficient_players", null));
                        return false;
                    }
                    gameManager.startGame();
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
            case "players" -> {
                handlePlayers(sender, args);
            }
            case "pacman" -> {
                handlePacMan(sender, args);
            }
            case "givebomb" -> {
                if (gameManager.getCurrentState() == GameManager.GameState.STOPPED) {
                    sender.sendMessage(chatUtils.getMessage("command.adminbomb.game_stopped", null));
                    return false;
                }
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
                if (gameManager.getCurrentState() == GameManager.GameState.STOPPED) {
                    sender.sendMessage(chatUtils.getMessage("command.adminbomb.game_stopped", null));
                    return false;
                }
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
        return false;
    }

    public void handlePlayers(CommandSender sender, String[] args) {
        if (args.length < 2) {
            sender.sendMessage(chatUtils.getMessage("command.players.no_arg", null));
            return;
        }

        String action = args[1];

        switch (action) {
            case "clear" -> {
                sender.sendMessage(chatUtils.getMessage("command.players.clear.success", null));
                groupManager.clearPlayers();
            }
            case "sync" -> {
                groupManager.syncPlayers();
                sender.sendMessage(chatUtils.getMessage("command.players.sync.success", null)
                        .replace("%int%", String.valueOf(groupManager.getPlayersInt())));
            }
            case "help" -> {
                List<String> helpMessage = configManager.getMessageConfig().getStringList("command.players.help");

                for (String line : helpMessage) {
                    line = ChatUtils.formatColor(line);
                    sender.sendMessage(line);
                }
            }
            default -> sender.sendMessage(chatUtils.getMessage("command.players.usage", null));
        }
    }
    public void handlePacMan(CommandSender sender, String[] args) {
        if (args.length < 2) {
            sender.sendMessage(chatUtils.getMessage("command.pacman.no_arg", null));
            return;
        }

        String action = args[1];

        switch (action) {
            case "clear" -> {
                sender.sendMessage(chatUtils.getMessage("command.pacman.clear.success", null));
                groupManager.clearPacMan();
            }
            case "set" -> {
                if (args.length < 3) {
                    sender.sendMessage(chatUtils.getMessage("command.pacman.set.no_arg", null));
                    return;
                }

                String targetName = args[2];
                Player target = Bukkit.getPlayerExact(targetName);

                if (target == null) {
                    sender.sendMessage(chatUtils.getMessage("command.pacman.set.player_not_found", null).replace("%arg%", targetName));
                    return;
                }

                if (target.equals(groupManager.getPacMan())) {
                    sender.sendMessage(chatUtils.getMessage("command.pacman.set.already", null).replace("%arg%", targetName));
                    return;
                }

                groupManager.setPacMan(target);
                sender.sendMessage(chatUtils.getMessage("command.pacman.set.success", null).replace("%target%", targetName));
            }
            case "help" -> {
                List<String> helpMessage = configManager.getMessageConfig().getStringList("command.pacman.help");

                for (String line : helpMessage) {
                    line = ChatUtils.formatColor(line);
                    sender.sendMessage(line);
                }
            }
            default -> sender.sendMessage(chatUtils.getMessage("command.pacman.usage", null));
        }
    }
}
