package org.eldrygo.GoldenBomb.Utils;

import org.eldrygo.GoldenBomb.Game.Managers.GameManager;
import org.eldrygo.GoldenBomb.GoldenBomb;
import org.eldrygo.GoldenBomb.Handlers.GoldenBombCommand;
import org.eldrygo.GoldenBomb.Handlers.GoldenBombTabCompleter;
import org.eldrygo.GoldenBomb.Managers.ConfigManager;

public class LoadUtils {
    private final GoldenBomb plugin;
    private final ChatUtils chatUtils;
    private final ConfigManager configManager;
    private final GameManager gameManager;

    public LoadUtils(GoldenBomb plugin, ChatUtils chatUtils, ConfigManager configManager, GameManager gameManager) {
        this.plugin = plugin;
        this.chatUtils = chatUtils;
        this.configManager = configManager;
        this.gameManager = gameManager;
    }

    public void loadFeatures() {
        loadConfigFiles();
        registerCommand();
    }

    public void loadConfigFiles() {
        configManager.loadConfig();
        configManager.reloadMessages();
        configManager.setPrefix(ChatUtils.formatColor(configManager.getMessageConfig().getString("prefix", "#ffc34d&lGolden&r&lBomb &cDefault Prefix &8»&r")));
    }
    private void registerCommand() {
        if (plugin.getCommand("goldenbomb") == null) {
            plugin.getLogger().severe("❌ Error: /goldenbomb command is not registered in plugin.yml");
        } else {
            plugin.getCommand("goldenbomb").setExecutor(new GoldenBombCommand(chatUtils, configManager, gameManager, this));
            plugin.getCommand("goldenbomb").setTabCompleter(new GoldenBombTabCompleter());
            plugin.getLogger().info("✅ /goldenbomb command was successfully loaded.");
        }
    }
}
