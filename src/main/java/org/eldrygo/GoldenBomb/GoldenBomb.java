package org.eldrygo.GoldenBomb;

import org.bukkit.plugin.java.JavaPlugin;
import org.eldrygo.GoldenBomb.Game.Managers.BombManager;
import org.eldrygo.GoldenBomb.Game.Managers.GameManager;
import org.eldrygo.GoldenBomb.Game.Managers.ItemManager;
import org.eldrygo.GoldenBomb.Game.Managers.RunnableManager;
import org.eldrygo.GoldenBomb.Lib.Time.Managers.TimeManager;
import org.eldrygo.GoldenBomb.Plugin.Managers.ConfigManager;
import org.eldrygo.GoldenBomb.Plugin.Utils.ChatUtils;
import org.eldrygo.GoldenBomb.Plugin.Utils.LoadUtils;
import org.eldrygo.GoldenBomb.Plugin.Utils.LogsUtils;
import org.eldrygo.GoldenBomb.Plugin.Utils.OtherUtils;

public class GoldenBomb extends JavaPlugin {
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
        TimeManager timeManager = new TimeManager(this);
        this.gameManager = new GameManager(bombManager, runnableManager, timeManager, this, configManager);
        ChatUtils chatUtils = new ChatUtils(configManager, this);
        LoadUtils loadUtils = new LoadUtils(this, chatUtils, configManager, gameManager, bombManager, timeManager);
        OtherUtils.setPlugin(this);
        loadUtils.loadFeatures();

        logsUtils.sendStartupMessage();
    }

    @Override
    public void onDisable() {
        if (gameManager.getCurrentState() == GameManager.GameState.STOPPED) gameManager.stopGame();
        runnableManager.stopTask();

        logsUtils.sendShutdownMessage();
    }
}
