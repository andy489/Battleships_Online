package bg.sofia.uni.fmi.mjt.battleships.online.server.command.mode.surfing;

import bg.sofia.uni.fmi.mjt.battleships.online.server.command.response.CommandResponseDTO;
import bg.sofia.uni.fmi.mjt.battleships.online.server.game.Game;
import bg.sofia.uni.fmi.mjt.battleships.online.server.game.player.Player;
import bg.sofia.uni.fmi.mjt.battleships.online.server.game.player.PlayerStatus;
import bg.sofia.uni.fmi.mjt.battleships.online.server.game.player.board.Board;
import bg.sofia.uni.fmi.mjt.battleships.online.server.storage.Storage;

import java.nio.channels.SocketChannel;

public class CreateGame {
    private static final String CREATE_GAME_USAGE = "Requires one argument. Usage: create-game <game-name>";
    private static final String LOGIN_OR_REGISTER = "You must login or register first";
    private static final String ALREADY_IN_A_GAME = "You are already in a game";
    private static final String GAME_ALREADY_EXISTS = "Game \"%s\" already exists. Choose another game-name";
    private static final String GAME_CREATED = "Created game \"%s\", players 1/2";

    public static CommandResponseDTO execute(Storage storage, String id, SocketChannel socketChannel, String... args) {
        if (args.length == 0) {
            return new CommandResponseDTO(CREATE_GAME_USAGE);
        }

        String apiKey = storage.getApiKey(id);

        if (apiKey == null) {
            return new CommandResponseDTO(LOGIN_OR_REGISTER);
        }

        PlayerStatus currStatus = storage.getPlayerStatus(id);

        if (currStatus != PlayerStatus.ONLINE) {
            return new CommandResponseDTO(ALREADY_IN_A_GAME);
        }

        String gameName = args[0];

        if (storage.checkForExistingGameName(gameName)) {
            return new CommandResponseDTO(String.format(GAME_ALREADY_EXISTS, gameName));
        }

        String userName = storage.getUsername(id);
        Player player = new Player(userName, id, new Board(), socketChannel, apiKey);
        Game currentGame = new Game(gameName, player);

        storage.setCurrentGame(id, currentGame);
        storage.setPlayerStatus(id, PlayerStatus.HOST);

        return new CommandResponseDTO(String.format(GAME_CREATED, gameName));
    }
}
