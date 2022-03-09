package bg.sofia.uni.fmi.mjt.battleships.online.server.command.mode.playing;

import bg.sofia.uni.fmi.mjt.battleships.online.server.command.response.CommandResponseDTO;
import bg.sofia.uni.fmi.mjt.battleships.online.server.command.response.SaveResponseDTO;
import bg.sofia.uni.fmi.mjt.battleships.online.server.game.Game;
import bg.sofia.uni.fmi.mjt.battleships.online.server.game.GameStatus;
import bg.sofia.uni.fmi.mjt.battleships.online.server.game.SavedGame;
import bg.sofia.uni.fmi.mjt.battleships.online.server.game.player.PlayerStatus;
import bg.sofia.uni.fmi.mjt.battleships.online.server.game.player.board.Board;
import bg.sofia.uni.fmi.mjt.battleships.online.server.storage.Storage;
import com.google.gson.Gson;

public class SaveGame {
    private static final String LOGIN_OR_REGISTER = "You must login or register first";
    private static final String NOT_IN_GAME = "No current game";
    private static final String SAVE_GAME_EXISTING_NAME = "Cannot save game. Game with name \"%s\" already saved";
    private static final String GAME_SAVED = "Game \"%s\" saved";
    private static final String PENDING_GAME_SAVE = "Cannot save pending game";

    public static CommandResponseDTO execute(Storage storage, String id, Gson gson) {
        return new CommandResponseDTO(save(storage, id, gson).saveMsg());
    }

    public static SaveResponseDTO save(Storage storage, String id, Gson gson) {
        if (storage.getApiKey(id) == null) {
            return new SaveResponseDTO(LOGIN_OR_REGISTER, false);
        }

        if (storage.getPlayerStatus(id) == PlayerStatus.ONLINE) {
            return new SaveResponseDTO(NOT_IN_GAME, false);
        }

        String apiKey = storage.getApiKey(id);

        Game currGame = storage.getCurrentGame(id);

        if (currGame.getStatus() == GameStatus.PENDING) {
            return new SaveResponseDTO(PENDING_GAME_SAVE, false);
        }

        String currGameName = currGame.getGameName();

        if (storage.containsGameName(apiKey, currGameName)) {
            return new SaveResponseDTO(String.format(SAVE_GAME_EXISTING_NAME, currGameName), false);
        }

        String hostBoard = gson.toJson(currGame.getHostPlayer().board());
        String guestBoard = gson.toJson(currGame.getGuestPlayer().board());

        Board copyHostBoard = gson.fromJson(hostBoard, Board.class);
        Board copyGuestBoard = gson.fromJson(guestBoard, Board.class);

        SavedGame savedGame = new SavedGame(
                currGameName,
                copyHostBoard,
                copyGuestBoard,
                currGame.getHostTurn(),
                currGame.getHostPlayer().id().equals(id)
        );

        storage.addSavedGame(apiKey, savedGame);

        return new SaveResponseDTO(String.format(GAME_SAVED, currGameName), true);
    }
}
