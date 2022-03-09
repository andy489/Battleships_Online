package bg.sofia.uni.fmi.mjt.battleships.online.server.game.player;

import bg.sofia.uni.fmi.mjt.battleships.online.server.game.player.board.Board;

import java.nio.channels.SocketChannel;

public class Player {
    private final String username;
    private final String id;
    private final String apiKey;

    private final Board board;
    private final SocketChannel socketChannel;

    public Player(String username, String id, Board board, SocketChannel socketChannel, String apiKey) {
        this.username = username;
        this.id = id;
        this.board = board;
        this.socketChannel = socketChannel;
        this.apiKey = apiKey;
    }

    public String username() {
        return username;
    }

    public String id() {
        return id;
    }

    public Board board() {
        return board;
    }

    public SocketChannel socketChannel() {
        return socketChannel;
    }

    public String apiKey() {
        return apiKey;
    }
}
