package ui.game;

/**
 * Created by sandstranger on 14.10.16.
 */

public class GameState {
    private static boolean isGameStarted = false;

    public static boolean getGameState() {
        return isGameStarted;
    }

    public static void setGameState(boolean isGameStarted) {
        GameState.isGameStarted = isGameStarted;
    }
}
