package org.eldrygo.RTNTTag.Plugin.Utils;

import org.bukkit.Bukkit;
import org.eldrygo.RTNTTag.API.RTNTTagExpansion;
import org.eldrygo.RTNTTag.Game.Listeners.PlayerListener;
import org.eldrygo.RTNTTag.Game.Managers.BombManager;
import org.eldrygo.RTNTTag.Game.Managers.GameManager;
import org.eldrygo.RTNTTag.Lib.Time.Listeners.TimerListener;
import org.eldrygo.RTNTTag.Lib.Time.Managers.TimerManager;
import org.eldrygo.RTNTTag.Plugin.Handlers.RTNTTagCommand;
import org.eldrygo.RTNTTag.Plugin.Handlers.RTNTTagTabCompleter;
import org.eldrygo.RTNTTag.Plugin.Managers.ConfigManager;
import org.eldrygo.RTNTTag.RTNTTag;

public class LoadUtils {
    private final RTNTTag plugin;
    private final ChatUtils chatUtils;
    private final ConfigManager configManager;
    private final GameManager gameManager;
    private final BombManager bombManager;
    private final TimerManager timerManager;

    public LoadUtils(RTNTTag plugin, ChatUtils chatUtils, ConfigManager configManager, GameManager gameManager, BombManager bombManager, TimerManager timerManager) {
        this.plugin = plugin;
        this.chatUtils = chatUtils;
        this.configManager = configManager;
        this.gameManager = gameManager;
        this.bombManager = bombManager;
        this.timerManager = timerManager;
    }

    public void loadFeatures() {
        loadConfigFiles();
        registerCommand();
        registerListeners();
        loadPlaceholderAPI();
        loadXBossBar();
        updateTimersTask();
        setBombTeam();
    }
    private void setBombTeam() {
        bombManager.setBombTeam(OtherUtils.getBombTeam());
    }

    public void loadConfigFiles() {
        configManager.loadConfig();
        configManager.reloadMessages();
        configManager.setPrefix(ChatUtils.formatColor(configManager.getMessageConfig().getString("prefix", "#499deb&lr&r&f&lTNTTag &cDefault Prefix &8»&r")));
    }
    private void registerCommand() {
        if (plugin.getCommand("rtnttag") == null) {
            plugin.getLogger().severe("❌ Error: /rtnttag command is not registered in plugin.yml");
        } else {
            plugin.getCommand("rtnttag").setExecutor(new RTNTTagCommand(chatUtils, configManager, gameManager, this, bombManager));
            plugin.getCommand("rtnttag").setTabCompleter(new RTNTTagTabCompleter());
            plugin.getLogger().info("✅ /rtnttag command was successfully loaded.");
        }
    }
    private void loadPlaceholderAPI() {
        if (Bukkit.getPluginManager().isPluginEnabled("PlaceholderAPI")) {
            new RTNTTagExpansion(plugin, timerManager).register();
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

    private void registerListeners() {
        Bukkit.getServer().getPluginManager().registerEvents(new PlayerListener(gameManager, bombManager), plugin);
        Bukkit.getServer().getPluginManager().registerEvents(new TimerListener(gameManager), plugin);
    }

    private void updateTimersTask() {
        Bukkit.getScheduler().runTaskTimer(plugin, task -> {
            timerManager.updateTimers();
        }, 0L, 20L);
    }
}
