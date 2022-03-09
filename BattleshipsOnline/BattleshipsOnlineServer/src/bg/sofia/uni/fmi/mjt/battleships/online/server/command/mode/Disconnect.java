package bg.sofia.uni.fmi.mjt.battleships.online.server.command.mode;

import bg.sofia.uni.fmi.mjt.battleships.online.server.command.mode.playing.SaveGame;
import bg.sofia.uni.fmi.mjt.battleships.online.server.command.response.CommandResponseDTO;
import bg.sofia.uni.fmi.mjt.battleships.online.server.game.Game;
import bg.sofia.uni.fmi.mjt.battleships.online.server.game.GameStatus;
import bg.sofia.uni.fmi.mjt.battleships.online.server.game.SavedGame;
import bg.sofia.uni.fmi.mjt.battleships.online.server.game.player.Player;
import bg.sofia.uni.fmi.mjt.battleships.online.server.game.player.PlayerStatus;
import bg.sofia.uni.fmi.mjt.battleships.online.server.game.player.board.Board;
import bg.sofia.uni.fmi.mjt.battleships.online.server.storage.Storage;
import bg.sofia.uni.fmi.mjt.battleships.online.server.storage.UserIn;
import com.google.gson.Gson;

import java.nio.channels.SocketChannel;
import java.util.Map;

import static bg.sofia.uni.fmi.mjt.battleships.online.server.command.manager.CommandExecutor.BLUE;
import static bg.sofia.uni.fmi.mjt.battleships.online.server.command.manager.CommandExecutor.DEFAULT;
import static bg.sofia.uni.fmi.mjt.battleships.online.server.file.manager.FileManager.getSavePath;
import static bg.sofia.uni.fmi.mjt.battleships.online.server.file.manager.FileManager.writeSavedGames;

public class Disconnect {
    private static final String HOST_DISCONNECT = "%s%s%s exit the game. Waiting for another player to join...";

    public static CommandResponseDTO execute(Storage storage, String id, Gson gson) {
        String apiKey = storage.getApiKey(id);

        if (apiKey == null) {
            storage.removeUserIn(id);
            return new CommandResponseDTO(null);
        }

        UserIn user = storage.getUserIn(id);
        Game currGame = user.getCurrGame();

        if (currGame == null || currGame.getStatus() == GameStatus.PENDING) {
            storage.removeUserIn(id);

            String savePath = getSavePath(apiKey);
            Map<String, SavedGame> gamesToSave = storage.getGamesToSave(apiKey);
            String gamesJson = gson.toJson(gamesToSave);

            writeSavedGames(apiKey, gamesJson, savePath);

            return new CommandResponseDTO(null);
        }

        SaveGame.save(storage, id, gson);
        currGame.setStatus(GameStatus.PENDING);

        Player hostPlayer = currGame.getHostPlayer();
        Player guestPlayer = currGame.getGuestPlayer();

        Board secondBoard;
        SocketChannel otherSocketChannel;

        if (hostPlayer.id().equals(id)) {
            otherSocketChannel = guestPlayer.socketChannel();

            currGame.setHostPlayer(guestPlayer);

            storage.getUserIn(guestPlayer.id()).setStatus(PlayerStatus.HOST);

            secondBoard = hostPlayer.board();

            if (currGame.getHostTurn()) {
                currGame.flipTurn();
            }
        } else {
            secondBoard = guestPlayer.board();
            otherSocketChannel = currGame.getHostPlayer().socketChannel();
        }

        currGame.setGuestPlayer(null);

        currGame.setSavedBoardSecondPlayer(secondBoard);

        String savePath = getSavePath(apiKey);
        Map<String, SavedGame> gamesToSave = storage.getGamesToSave(apiKey);
        String gamesJson = gson.toJson(gamesToSave);

        writeSavedGames(apiKey, gamesJson, savePath);

        storage.getGamesToSave(apiKey).clear();
        storage.removeUserIn(id);

        String thisUsername = user.getUsername();

        return new CommandResponseDTO(
                null,
                String.format(HOST_DISCONNECT, BLUE, thisUsername, DEFAULT),
                otherSocketChannel
        );
    }
}
