package bg.sofia.uni.fmi.mjt.battleships.online.server.command.mode.surfing;

import bg.sofia.uni.fmi.mjt.battleships.online.server.command.response.CommandResponseDTO;
import bg.sofia.uni.fmi.mjt.battleships.online.server.game.Game;
import bg.sofia.uni.fmi.mjt.battleships.online.server.game.SavedGame;
import bg.sofia.uni.fmi.mjt.battleships.online.server.game.player.Player;
import bg.sofia.uni.fmi.mjt.battleships.online.server.game.player.PlayerStatus;
import bg.sofia.uni.fmi.mjt.battleships.online.server.game.player.board.Board;
import bg.sofia.uni.fmi.mjt.battleships.online.server.game.player.board.field.ship.Ship;
import bg.sofia.uni.fmi.mjt.battleships.online.server.game.player.board.field.ship.coordinate.Coordinate;
import bg.sofia.uni.fmi.mjt.battleships.online.server.storage.Storage;

import java.nio.channels.SocketChannel;
import java.util.List;
import java.util.Map;

public class LoadGame {
    private static final String LOAD_GAME_USAGE = "Requires one argument. Usage: load-game <game-name>";
    private static final String ALREADY_IN_A_GAME = "You are already in a game";
    private static final String NO_SUCH_GAME = "There is no such game";
    private static final String GAME_ALREADY_EXISTS = "Game with that name already exists. Choose another game-name";
    private static final String LOGIN_OR_REGISTER = "You must login or register first";
    private static final String GAME_LOADED = "Loaded game %s";

    public static CommandResponseDTO execute(Storage storage, String id, SocketChannel socketChannel, String... args) {
        if (args.length == 0) {
            return new CommandResponseDTO(LOAD_GAME_USAGE);
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

        if (!storage.containsGameName(apiKey, gameName)) {
            return new CommandResponseDTO(NO_SUCH_GAME);
        }

        if (storage.checkForExistingGameName(gameName)) {
            return new CommandResponseDTO(GAME_ALREADY_EXISTS);
        }

        SavedGame loadedGame = storage.getSavedGame(apiKey, gameName);

        // adjust boards (fields covered from one ship should point to the same ship)

        adjustBoard(loadedGame.getHostBoard());
        adjustBoard(loadedGame.getGuestBoard());

        // done adjusting boards

        String username = storage.getUsername(id);

        Board creatorBoard;
        Board joinerBoard;

        boolean flipTurns = false;

        if (loadedGame.getWasThisPlayerHost()) {
            creatorBoard = loadedGame.getHostBoard();
            joinerBoard = loadedGame.getGuestBoard();

            if (!loadedGame.getHostTurn()) {
                flipTurns = true;
            }
        } else {
            if (loadedGame.getHostTurn()) {
                flipTurns = true;
            }

            joinerBoard = loadedGame.getHostBoard();
            creatorBoard = loadedGame.getGuestBoard();
        }

        Player creator = new Player(username, id, creatorBoard, socketChannel, apiKey);

        Game currGame = new Game(loadedGame.getName(), creator, joinerBoard);

        if (flipTurns) {
            currGame.flipTurn();
        }

        storage.setCurrentGame(id, currGame);
        storage.setPlayerStatus(id, PlayerStatus.HOST);
        storage.removeSavedGame(apiKey, loadedGame.getName());

        return new CommandResponseDTO(String.format(GAME_LOADED, gameName));
    }

    private static void adjustBoard(Board board) {
        Map<Integer, List<Ship>> ships = board.getAllShips();

        for (Map.Entry<Integer, List<Ship>> entry : ships.entrySet()) {
            for (final Ship ship : entry.getValue()) {
                for (Coordinate coord : ship.getLocation()) {
                    board.getBoard()[coord.getC()][coord.getI()].setShip(ship);
                }
            }
        }
    }
}
