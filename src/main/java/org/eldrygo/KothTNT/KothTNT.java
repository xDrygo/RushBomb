package org.eldrygo.KothTNT;

import org.bukkit.plugin.java.JavaPlugin;
import org.eldrygo.KothTNT.Game.Managers.*;
import org.eldrygo.KothTNT.Lib.Points.Managers.PointsManager;
import org.eldrygo.KothTNT.Lib.Time.Managers.TimerManager;
import org.eldrygo.KothTNT.Plugin.Managers.ConfigManager;
import org.eldrygo.KothTNT.Plugin.Utils.ChatUtils;
import org.eldrygo.KothTNT.Plugin.Utils.LoadUtils;
import org.eldrygo.KothTNT.Plugin.Utils.LogsUtils;
import org.eldrygo.KothTNT.Plugin.Utils.OtherUtils;

public class KothTNT extends JavaPlugin {
    public String prefix;
    public String version;
    private LogsUtils logsUtils;
    private GameManager gameManager;
    private RunnableManager runnableManager;

    @Override
    public void onEnable() {
        version = getDescription().getVersion();
        ConfigManager configManager = new ConfigManager(this);
        ChatUtils chatUtils = new ChatUtils(configManager, this);
        ItemManager itemManager = new ItemManager(this);
        BombManager bombManager = new BombManager(this, itemManager);
        PointsManager pointsManager = new PointsManager();
        this.runnableManager = new RunnableManager(this, bombManager, pointsManager);
        TimerManager timerManager = new TimerManager(this, null);
        HUDManager hudManager = new HUDManager(configManager, bombManager, this, null);
        this.gameManager = new GameManager(bombManager, runnableManager, timerManager, this, configManager, hudManager, pointsManager);
        LoadUtils loadUtils = new LoadUtils(this, chatUtils, configManager, gameManager, bombManager, timerManager, pointsManager);
        this.logsUtils = new LogsUtils(this);
        OtherUtils.setPlugin(this);
        setInstances(hudManager, gameManager, timerManager);
        loadUtils.loadFeatures();
        logsUtils.sendStartupMessage();
    }

    @Override
    public void onDisable() {
        if (gameManager.getCurrentState() != GameManager.GameState.STOPPED) gameManager.stopGame();
        runnableManager.stopTask();
        logsUtils.sendShutdownMessage();
    }
    private void setInstances(HUDManager hudManager, GameManager gameManager, TimerManager timerManager) {
        hudManager.setGameManager(gameManager);
        timerManager.setGameManager(gameManager);
    }
}
