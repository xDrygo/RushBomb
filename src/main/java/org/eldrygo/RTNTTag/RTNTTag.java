package org.eldrygo.RTNTTag;

import org.bukkit.plugin.java.JavaPlugin;
import org.eldrygo.RTNTTag.Game.Managers.*;
import org.eldrygo.RTNTTag.Lib.Time.Managers.TimerManager;
import org.eldrygo.RTNTTag.Plugin.Managers.ConfigManager;
import org.eldrygo.RTNTTag.Plugin.Utils.ChatUtils;
import org.eldrygo.RTNTTag.Plugin.Utils.LoadUtils;
import org.eldrygo.RTNTTag.Plugin.Utils.LogsUtils;
import org.eldrygo.RTNTTag.Plugin.Utils.OtherUtils;

public class RTNTTag extends JavaPlugin {
    public String prefix;
    public String version;
    private LogsUtils logsUtils;
    private GameManager gameManager;
    private RunnableManager runnableManager;

    @Override
    public void onEnable() {
        version = getDescription().getVersion();
        this.logsUtils = new LogsUtils(this);
        ItemManager itemManager = new ItemManager(this);
        BombManager bombManager = new BombManager(this, itemManager);
        this.runnableManager = new RunnableManager(this, bombManager);
        ConfigManager configManager = new ConfigManager(this);
        TimerManager timerManager = new TimerManager(this, null);
        HUDManager hudManager = new HUDManager(configManager, bombManager, this, null);
        this.gameManager = new GameManager(bombManager, runnableManager, timerManager, this, configManager, hudManager);
        ChatUtils chatUtils = new ChatUtils(configManager, this);
        LoadUtils loadUtils = new LoadUtils(this, chatUtils, configManager, gameManager, bombManager, timerManager);
        OtherUtils.setPlugin(this);
        setInstances(hudManager, timerManager);
        loadUtils.loadFeatures();

        logsUtils.sendStartupMessage();
    }
    private void setInstances(HUDManager hudManager, TimerManager timerManager) {
        timerManager.setGameManager(gameManager);
        hudManager.setGameManager(gameManager);
    }

    @Override
    public void onDisable() {
        if (gameManager.getCurrentState() != GameManager.GameState.STOPPED) gameManager.stopGame();
        if (runnableManager.taskExists()) runnableManager.stopTask();

        logsUtils.sendShutdownMessage();
    }
}
