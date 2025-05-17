package org.eldrygo.GoldenBomb.Game.Managers;

public class GameManager {
    private final BombManager bombManager;
    private final RunnableManager runnableManager;

    public GameManager(BombManager bombManager, RunnableManager runnableManager) {
        this.bombManager = bombManager;
        this.runnableManager = runnableManager;
    }

    public enum GameState {
        RUNNING,
        STOPPED
    }

    private GameState currentState = GameState.STOPPED;

    public void startGame() {
        currentState = GameState.RUNNING;
        runnableManager.startTask();
    }

    public void stopGame() {
        currentState = GameState.STOPPED;
        runnableManager.stopTask();
    }

    public GameState getCurrentState() {
        return currentState;
    }

    public void resetAll() {
        stopGame();
        bombManager.resetBombs();
    }
}
