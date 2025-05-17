package org.eldrygo.GoldenBomb;

import org.bukkit.plugin.java.JavaPlugin;
import org.eldrygo.GoldenBomb.Game.Managers.BombManager;
import org.eldrygo.GoldenBomb.Game.Managers.GameManager;
import org.eldrygo.GoldenBomb.Managers.ConfigManager;
import org.eldrygo.GoldenBomb.Utils.ChatUtils;
import org.eldrygo.GoldenBomb.Utils.LoadUtils;
import org.eldrygo.GoldenBomb.Utils.LogsUtils;

public class GoldenBomb extends JavaPlugin {
    public String prefix;
    public String version;
    private LogsUtils logsUtils;
    private GameManager gameManager;

    @Override
    public void onEnable() {
        version = getDescription().getVersion();

        this.logsUtils = new LogsUtils(this);
        this.gameManager = new GameManager();

        ConfigManager configManager = new ConfigManager(this);
        ChatUtils chatUtils = new ChatUtils(configManager, this);
        BombManager bombManager = new BombManager(this);
        LoadUtils loadUtils = new LoadUtils(this, chatUtils, configManager, gameManager, bombManager);

        loadUtils.loadFeatures();

        logsUtils.sendStartupMessage();
    }

    @Override
    public void onDisable() {
        if (gameManager.getCurrentState() == GameManager.GameState.STOPPED) gameManager.stopGame();

        logsUtils.sendShutdownMessage();
    }
}
