package org.eldrygo.PacTNTMan;

import org.bukkit.plugin.java.JavaPlugin;
import org.eldrygo.PacTNTMan.Game.Managers.*;
import org.eldrygo.PacTNTMan.Lib.Time.Managers.TimerManager;
import org.eldrygo.PacTNTMan.Plugin.Managers.ConfigManager;
import org.eldrygo.PacTNTMan.Plugin.Utils.ChatUtils;
import org.eldrygo.PacTNTMan.Plugin.Utils.LoadUtils;
import org.eldrygo.PacTNTMan.Plugin.Utils.LogsUtils;
import org.eldrygo.PacTNTMan.Plugin.Utils.OtherUtils;

public class PacTNTMan extends JavaPlugin {
    public String prefix;
    public String version;
    private LogsUtils logsUtils;

    @Override
    public void onEnable() {
        version = getDescription().getVersion();
        ConfigManager configManager = new ConfigManager(this);
        ChatUtils chatUtils = new ChatUtils(this, configManager);
        ItemManager itemManager = new ItemManager(this);
        GroupManager groupManager = new GroupManager();
        TimerManager timerManager = new TimerManager(this, null);
        BombManager bombManager = new BombManager(itemManager, this, timerManager);
        RunnableManager runnableManager = new RunnableManager(bombManager, this, groupManager);
        HUDManager hudManager = new HUDManager(groupManager, configManager, bombManager, this, null);
        GameManager gameManager = new GameManager(runnableManager, bombManager, configManager, this, timerManager, hudManager);
        LoadUtils loadUtils = new LoadUtils(this, configManager, bombManager, gameManager, chatUtils, groupManager, timerManager);
        this.logsUtils = new LogsUtils(this);

        setInstances(hudManager, gameManager, timerManager);
        OtherUtils.setPlugin(this);

        loadUtils.loadFeatures();
        logsUtils.sendStartupMessage();
    }

    @Override
    public void onDisable() {
        logsUtils.sendShutdownMessage();
    }

    private void setInstances(HUDManager hudManager, GameManager gameManager, TimerManager timerManager) {
        hudManager.setGameManager(gameManager);
        timerManager.setGameManager(gameManager);
    }
}
