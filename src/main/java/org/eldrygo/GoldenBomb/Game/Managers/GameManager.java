package org.eldrygo.GoldenBomb.Game.Managers;

public class GameManager {
    public enum GameState {
        RUNNING,
        STOPPED
    }

    private GameState currentState = GameState.STOPPED;

    public void startGame() {
        currentState = GameState.RUNNING;
    }

    public void stopGame() {
        currentState = GameState.STOPPED;
    }

    public GameState getCurrentState() {
        return currentState;
    }
}
