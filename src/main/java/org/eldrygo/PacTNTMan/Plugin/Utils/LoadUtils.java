package org.eldrygo.PacTNTMan.Plugin.Utils;

import org.bukkit.Bukkit;
import org.eldrygo.PacTNTMan.API.PacTNTManExpansion;
import org.eldrygo.PacTNTMan.Game.Listeners.PacManListener;
import org.eldrygo.PacTNTMan.Game.Listeners.PlayerListener;
import org.eldrygo.PacTNTMan.Game.Managers.BombManager;
import org.eldrygo.PacTNTMan.Game.Managers.GameManager;
import org.eldrygo.PacTNTMan.Game.Managers.GroupManager;
import org.eldrygo.PacTNTMan.Lib.Time.Listeners.TimerListener;
import org.eldrygo.PacTNTMan.Lib.Time.Managers.TimerManager;
import org.eldrygo.PacTNTMan.PacTNTMan;
import org.eldrygo.PacTNTMan.Plugin.Handlers.PTNTMCommand;
import org.eldrygo.PacTNTMan.Plugin.Handlers.PTNTMTabCompleter;
import org.eldrygo.PacTNTMan.Plugin.Managers.ConfigManager;

public class LoadUtils {
    private final PacTNTMan plugin;
    private final ConfigManager configManager;
    private final BombManager bombManager;
    private final GameManager gameManager;
    private final ChatUtils chatUtils;
    private final GroupManager groupManager;
    private final TimerManager timerManager;

    public LoadUtils(PacTNTMan plugin, ConfigManager configManager, BombManager bombManager, GameManager gameManager, ChatUtils chatUtils, GroupManager groupManager, TimerManager timerManager) {
        this.plugin = plugin;
        this.configManager = configManager;
        this.bombManager = bombManager;
        this.gameManager = gameManager;
        this.chatUtils = chatUtils;
        this.groupManager = groupManager;
        this.timerManager = timerManager;
    }

    public void loadFeatures() {
        loadConfigFiles();
        setBombTeam();
        loadListeners();
        loadCommand();
        loadPlaceholderAPI();
        loadXBossBar();
        updateTimersTask();
        bombManager.setBombTeam(OtherUtils.getBombTeam());
    }

    public void loadConfigFiles() {
        configManager.loadConfig();
        configManager.reloadMessages();
        configManager.setPrefix(ChatUtils.formatColor(configManager.getMessageConfig().getString(
                "prefix", "#ffaf1c&lP#ff1f1f&lT&r&f&lN#ff1f1f&lT#ffaf1c&lM &cDefault Prefix &8»&r")));
    }

    private void setBombTeam() {
        bombManager.setBombTeam(OtherUtils.getBombTeam());
    }

    private void loadListeners() {
        Bukkit.getServer().getPluginManager().registerEvents(new TimerListener(gameManager, bombManager), plugin);
        Bukkit.getServer().getPluginManager().registerEvents(new PlayerListener(gameManager, bombManager, groupManager), plugin);
        Bukkit.getServer().getPluginManager().registerEvents(new PacManListener(gameManager, groupManager, bombManager), plugin);
    }

    private void loadCommand() {
        if (plugin.getCommand("ptntm") == null) {
            plugin.getLogger().severe("❌ Error: /ptntm command is not registered in plugin.yml");
        } else {
            plugin.getCommand("ptntm").setExecutor(new PTNTMCommand(chatUtils, gameManager, this, configManager, groupManager, bombManager));
            plugin.getCommand("ptntm").setTabCompleter(new PTNTMTabCompleter());

            plugin.getLogger().info("✅ /ptntm command was successfully loaded.");
        }
    }
    private void loadPlaceholderAPI() {
        if (Bukkit.getPluginManager().isPluginEnabled("PlaceholderAPI")) {
            new PacTNTManExpansion(plugin, timerManager, configManager).register();
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
