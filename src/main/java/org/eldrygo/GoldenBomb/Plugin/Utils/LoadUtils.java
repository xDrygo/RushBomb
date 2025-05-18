package org.eldrygo.GoldenBomb.Plugin.Utils;

import org.bukkit.Bukkit;
import org.eldrygo.GoldenBomb.API.GoldenBombExpansion;
import org.eldrygo.GoldenBomb.Game.Listener.PlayerListener;
import org.eldrygo.GoldenBomb.Game.Managers.BombManager;
import org.eldrygo.GoldenBomb.Game.Managers.GameManager;
import org.eldrygo.GoldenBomb.GoldenBomb;
import org.eldrygo.GoldenBomb.Lib.Time.Managers.TimeManager;
import org.eldrygo.GoldenBomb.Plugin.Handlers.GoldenBombCommand;
import org.eldrygo.GoldenBomb.Plugin.Handlers.GoldenBombTabCompleter;
import org.eldrygo.GoldenBomb.Plugin.Managers.ConfigManager;

public class LoadUtils {
    private final GoldenBomb plugin;
    private final ChatUtils chatUtils;
    private final ConfigManager configManager;
    private final GameManager gameManager;
    private final BombManager bombManager;
    private final TimeManager timeManager;

    public LoadUtils(GoldenBomb plugin, ChatUtils chatUtils, ConfigManager configManager, GameManager gameManager, BombManager bombManager, TimeManager timeManager) {
        this.plugin = plugin;
        this.chatUtils = chatUtils;
        this.configManager = configManager;
        this.gameManager = gameManager;
        this.bombManager = bombManager;
        this.timeManager = timeManager;
    }

    public void loadFeatures() {
        loadConfigFiles();
        registerCommand();
        registerListeners();
        loadPlaceholderAPI();

        bombManager.setBombTeam(OtherUtils.getBombTeam());
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
            plugin.getCommand("goldenbomb").setExecutor(new GoldenBombCommand(chatUtils, configManager, gameManager, this, bombManager));
            plugin.getCommand("goldenbomb").setTabCompleter(new GoldenBombTabCompleter());
            plugin.getLogger().info("✅ /goldenbomb command was successfully loaded.");
        }
    }
    private void loadPlaceholderAPI() {
        if (Bukkit.getPluginManager().isPluginEnabled("PlaceholderAPI")) {
            new GoldenBombExpansion(plugin, timeManager).register();
            plugin.getLogger().info("✅ PlaceholderAPI detected. PAPI dependency successfully loaded.");
        } else {
            plugin.getLogger().warning("⚠  PlaceholderAPI not detected. PAPI placeholders will not work.");
        }
    }

    private void registerListeners() {
        Bukkit.getServer().getPluginManager().registerEvents(new PlayerListener(gameManager, bombManager, plugin), plugin);
    }
}
