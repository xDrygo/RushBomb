package org.eldrygo.KothTNT.Plugin.Utils;

import org.bukkit.Bukkit;
import org.eldrygo.KothTNT.API.KothTNTExpansion;
import org.eldrygo.KothTNT.Game.Listeners.PlayerListener;
import org.eldrygo.KothTNT.Game.Managers.BombManager;
import org.eldrygo.KothTNT.Game.Managers.GameManager;
import org.eldrygo.KothTNT.KothTNT;
import org.eldrygo.KothTNT.Lib.Points.Managers.PointsManager;
import org.eldrygo.KothTNT.Lib.Time.Listeners.TimerListener;
import org.eldrygo.KothTNT.Lib.Time.Managers.TimerManager;
import org.eldrygo.KothTNT.Plugin.Handlers.KothTNTCommand;
import org.eldrygo.KothTNT.Plugin.Handlers.KothTNTTabCompleter;
import org.eldrygo.KothTNT.Plugin.Managers.ConfigManager;

public class LoadUtils {
    private final KothTNT plugin;
    private final ChatUtils chatUtils;
    private final ConfigManager configManager;
    private final GameManager gameManager;
    private final BombManager bombManager;
    private final TimerManager timerManager;
    private final PointsManager pointsManager;

    public LoadUtils(KothTNT plugin, ChatUtils chatUtils, ConfigManager configManager, GameManager gameManager, BombManager bombManager, TimerManager timerManager, PointsManager pointsManager) {
        this.plugin = plugin;
        this.chatUtils = chatUtils;
        this.configManager = configManager;
        this.gameManager = gameManager;
        this.bombManager = bombManager;
        this.timerManager = timerManager;
        this.pointsManager = pointsManager;
    }

    public void loadFeatures() {
        loadConfigFiles();
        registerCommand();
        registerListeners();
        loadPlaceholderAPI();
        loadXBossBar();
        updateTimersTask();

        bombManager.setBombTeam(OtherUtils.getBombTeam());
    }

    public void loadConfigFiles() {
        configManager.loadConfig();
        configManager.reloadMessages();
        configManager.setPrefix(ChatUtils.formatColor(configManager.getMessageConfig().getString("prefix", "#ff38a4&lKoth&r&lTNT &cDefault Prefix &8»&r")));
    }
    private void registerCommand() {
        if (plugin.getCommand("kothtnt") == null) {
            plugin.getLogger().severe("❌ Error: /kothtnt command is not registered in plugin.yml");
        } else {
            plugin.getCommand("kothtnt").setExecutor(new KothTNTCommand(chatUtils, gameManager, bombManager, configManager, this, pointsManager));
            plugin.getCommand("kothtnt").setTabCompleter(new KothTNTTabCompleter());
            plugin.getLogger().info("✅ /kothtnt command was successfully loaded.");
        }
    }
    private void registerListeners() {
        Bukkit.getServer().getPluginManager().registerEvents(new PlayerListener(gameManager, bombManager), plugin);
        Bukkit.getServer().getPluginManager().registerEvents(new TimerListener(gameManager), plugin);
    }
    private void loadPlaceholderAPI() {
        if (Bukkit.getPluginManager().isPluginEnabled("PlaceholderAPI")) {
            new KothTNTExpansion(plugin, timerManager, configManager, pointsManager).register();
            plugin.getLogger().info("✅ PlaceholderAPI detected. PAPI dependency successfully loaded.");
        } else {
            plugin.getLogger().warning("⚠  PlaceholderAPI not detected. PAPI placeholders will not work.");
        }
    }
    private void loadXBossBar() {
        if (Bukkit.getPluginManager().isPluginEnabled("xBossBar")) {
            plugin.getLogger().info("✅ xBossBar detected. xBossBar dependency successfully loaded.");
        } else {
            plugin.getLogger().warning("⚠  xBossBar not detected. Disabling plugin...");
            plugin.getServer().getPluginManager().disablePlugin(plugin);
        }
    }
    private void updateTimersTask() {
        Bukkit.getScheduler().runTaskTimer(plugin, task -> {
            timerManager.updateTimers();
        }, 0L, 20L);
    }
}
