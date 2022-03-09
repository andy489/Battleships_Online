package bg.sofia.uni.fmi.mjt.battleships.online.server.game;

import bg.sofia.uni.fmi.mjt.battleships.online.server.game.player.board.Board;

public class SavedGame {
    private final String name;

    private final Board hostBoard;
    private final Board guestBoard;

    private final boolean hostTurn;
    private final boolean wasThisPlayerHost;

    public SavedGame(
            String name,
            Board hostBoard,
            Board guestBoard,
            boolean hostTurn,
            boolean wasThisPlayerHost
    ) {
        this.name = name;
        this.hostBoard = hostBoard;
        this.guestBoard = guestBoard;
        this.hostTurn = hostTurn;
        this.wasThisPlayerHost = wasThisPlayerHost;
    }

    public String getName() {
        return name;
    }

    public Board getHostBoard() {
        return hostBoard;
    }

    public Board getGuestBoard() {
        return guestBoard;
    }

    public boolean getHostTurn() {
        return hostTurn;
    }

    public boolean getWasThisPlayerHost() {
        return wasThisPlayerHost;
    }
}