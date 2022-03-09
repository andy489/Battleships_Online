package bg.sofia.uni.fmi.mjt.battleships.online.server.game;

import static bg.sofia.uni.fmi.mjt.battleships.online.server.command.manager.CommandExecutor.DEFAULT;
import static bg.sofia.uni.fmi.mjt.battleships.online.server.command.manager.CommandExecutor.RED;

public enum GameStatus {
    PENDING("pending", "pending".length()),
    IN_PROGRESS(RED + "in" + DEFAULT + " progress", "in".length() + " progress".length());

    private final String status;
    private final int len;

    GameStatus(String status, int len) {
        this.status = status;
        this.len = len;
    }

    public int len() {
        return len;
    }

    @Override
    public final String toString() {
        return status;
    }
}
