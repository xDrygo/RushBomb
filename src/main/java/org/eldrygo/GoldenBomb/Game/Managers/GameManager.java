package org.eldrygo.GoldenBomb.Game.Managers;

import org.bukkit.Bukkit;
import org.bukkit.boss.BarColor;
import org.bukkit.boss.BarStyle;
import org.bukkit.entity.Player;
import org.eldrygo.GoldenBomb.GoldenBomb;
import org.eldrygo.GoldenBomb.Lib.Time.Managers.TimeManager;
import org.eldrygo.GoldenBomb.Plugin.Managers.ConfigManager;
import org.eldrygo.XBossBar.API.XBossBarAPI;

import java.util.ArrayList;
import java.util.List;

public class GameManager {
    private final BombManager bombManager;
    private final RunnableManager runnableManager;
    private final TimeManager timeManager;
    private final GoldenBomb plugin;
    private final ConfigManager configManager;

    public GameManager(BombManager bombManager, RunnableManager runnableManager, TimeManager timeManager, GoldenBomb plugin, ConfigManager configManager) {
        this.bombManager = bombManager;
        this.runnableManager = runnableManager;
        this.timeManager = timeManager;
        this.plugin = plugin;
        this.configManager = configManager;
    }

    public enum GameState {
        RUNNING,
        STOPPED
    }

    private GameState currentState = GameState.STOPPED;

    public void startGame() {
        currentState = GameState.RUNNING;
        runnableManager.startTask();

        int gameDuration = plugin.getConfig().getInt("settings.game_duration");
        timeManager.createTimer("game", gameDuration);

        String bossbar = configManager.getMessageConfig().getString("bossbar.game");
        XBossBarAPI.createBossBar("goldenbomb", bossbar, BarColor.YELLOW, BarStyle.SOLID, false);
        for (Player p : Bukkit.getOnlinePlayers()) {
            XBossBarAPI.addPlayerToBossBar("goldenbomb", p);
        }
    }

    public void stopGame() {
        currentState = GameState.STOPPED;
        runnableManager.stopTask();
        killLosers();
        timeManager.stopTimer("game");
        XBossBarAPI.removeBossBar("goldenbomb");
    }

    public GameState getCurrentState() {
        return currentState;
    }

    public void resetAll() {
        stopGame();
        bombManager.resetBombs();
    }

    public void killLosers() {
        for (Player p : Bukkit.getOnlinePlayers()) {
            if (!bombManager.getPlayersWithBomb().contains(p)) {
                p.damage(100000.0);
            }
        }
    }
}