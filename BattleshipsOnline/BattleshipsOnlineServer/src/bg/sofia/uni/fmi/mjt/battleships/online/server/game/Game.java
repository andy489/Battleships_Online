package bg.sofia.uni.fmi.mjt.battleships.online.server.game;

import bg.sofia.uni.fmi.mjt.battleships.online.server.game.player.Player;
import bg.sofia.uni.fmi.mjt.battleships.online.server.game.player.board.Board;

import java.nio.channels.SocketChannel;
import java.util.Objects;

public class Game {
    private final String name;

    private Player host;
    private Player guest;

    private boolean hostReady;
    private boolean guestReady;

    private GameStatus status;

    private boolean hostTurn;

    private Board savedBoardSecondPlayer;

    public Game(String name, Player creator) {
        this(name, creator, null);
    }

    public Game(String name, Player creator, Board savedBoardSecondPlayer) {
        this.name = name;
        this.host = creator;

        status = GameStatus.PENDING;
        hostTurn = true;

        hostReady = false;
        guestReady = false;

        this.savedBoardSecondPlayer = savedBoardSecondPlayer;
    }

    public void setStatus(GameStatus status) {
        this.status = status;
    }

    public String getGameName() {
        return name;
    }

    public String getHostName() {
        return host.username();
    }

    public GameStatus getStatus() {
        return status;
    }

    public int numPlayers() {
        int numPlayers = 0;

        if (host != null) numPlayers++;
        if (guest != null) numPlayers++;

        return numPlayers;
    }

    public void join(String username, String id, SocketChannel socketChannel, String apiKey) {
        Player joined;

        joined = new Player(
                username, id,
                Objects.requireNonNullElseGet(savedBoardSecondPlayer, Board::new),
                socketChannel,
                apiKey)
        ;

        guest = joined;

        status = GameStatus.IN_PROGRESS;
    }

    public Player getHostPlayer() {
        return host;
    }

    public Player getGuestPlayer() {
        return guest;
    }

    public boolean getHostTurn() {
        return hostTurn;
    }

    public void flipTurn() {
        hostTurn = !hostTurn;
    }

    public void setHostReady() {
        hostReady = true;
    }

    public void setGuestReady() {
        guestReady = true;
    }

    public boolean notReadyToStart() {
        return !hostReady || !guestReady;
    }

    public void setHostPlayer(Player player) {
        host = player;
    }

    public void setGuestPlayer(Player player) {
        guest = player;
    }

    public void setSavedBoardSecondPlayer(Board board) {
        savedBoardSecondPlayer = board;
    }
}
