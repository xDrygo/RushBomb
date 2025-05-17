package org.eldrygo.GoldenBomb.Handlers;

import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.eldrygo.GoldenBomb.Game.Managers.GameManager;
import org.eldrygo.GoldenBomb.Managers.ConfigManager;
import org.eldrygo.GoldenBomb.Utils.ChatUtils;
import org.eldrygo.GoldenBomb.Utils.LoadUtils;
import org.jetbrains.annotations.NotNull;

import java.util.List;

public class GoldenBombCommand implements CommandExecutor {


    private final ChatUtils chatUtils;
    private final ConfigManager configManager;
    private final GameManager gameManager;
    private final LoadUtils loadUtils;

    public GoldenBombCommand(ChatUtils chatUtils, ConfigManager configManager, GameManager gameManager, LoadUtils loadUtils) {
        this.chatUtils = chatUtils;
        this.configManager = configManager;
        this.gameManager = gameManager;
        this.loadUtils = loadUtils;
    }

    @Override
    public boolean onCommand(CommandSender sender, @NotNull Command command, @NotNull String label, String[] args) {
        if (!sender.hasPermission("goldenbomb.admin") && !sender.isOp()) {
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
            default -> {
                sender.sendMessage(chatUtils.getMessage("command.usage", null));
            }
        }
        return true;
    }
}
