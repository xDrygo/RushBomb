package org.eldrygo.GoldenBomb.Handlers;

import org.bukkit.Bukkit;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.eldrygo.GoldenBomb.Game.Managers.BombManager;
import org.eldrygo.GoldenBomb.Game.Managers.GameManager;
import org.eldrygo.GoldenBomb.Managers.ConfigManager;
import org.eldrygo.GoldenBomb.Utils.ChatUtils;
import org.eldrygo.GoldenBomb.Utils.LoadUtils;
import org.eldrygo.GoldenBomb.Utils.OtherUtils;
import org.jetbrains.annotations.NotNull;

import java.util.List;

public class GoldenBombCommand implements CommandExecutor {


    private final ChatUtils chatUtils;
    private final ConfigManager configManager;
    private final GameManager gameManager;
    private final LoadUtils loadUtils;
    private final BombManager bombManager;

    public GoldenBombCommand(ChatUtils chatUtils, ConfigManager configManager, GameManager gameManager, LoadUtils loadUtils, BombManager bombManager) {
        this.chatUtils = chatUtils;
        this.configManager = configManager;
        this.gameManager = gameManager;
        this.loadUtils = loadUtils;
        this.bombManager = bombManager;
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
            case "givebombs" -> {
                if (gameManager.getCurrentState() == GameManager.GameState.STOPPED) {
                    sender.sendMessage(chatUtils.getMessage("command.givebombs.game_stopped", null));
                    return false;
                }

                if (args.length < 2) {
                    sender.sendMessage(chatUtils.getMessage("command.givebombs.no_args", null));
                    return false;
                }

                String playersIntStr = args[1];

                if (!OtherUtils.validInt(playersIntStr)) {
                    sender.sendMessage(chatUtils.getMessage("command.givebombs.invalid_int", null));
                    return false;
                }

                int playersInt = Integer.parseInt(playersIntStr);

                if (playersInt > Bukkit.getOnlinePlayers().size()) {
                    sender.sendMessage(chatUtils.getMessage("command.givebombs.int_is_more_than_players", null));
                    return false;
                }

                bombManager.giveRandomBombs(playersInt);
                sender.sendMessage(chatUtils.getMessage("command.givebombs.success", null).replace("%int%", playersIntStr));
            }
            case "resetall" -> {
                gameManager.resetAll();
                sender.sendMessage(chatUtils.getMessage("command.resetall.success", null));
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
