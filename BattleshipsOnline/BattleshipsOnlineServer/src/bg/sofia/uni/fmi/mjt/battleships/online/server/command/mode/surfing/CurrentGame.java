package bg.sofia.uni.fmi.mjt.battleships.online.server.command.mode.surfing;

import bg.sofia.uni.fmi.mjt.battleships.online.server.command.response.CommandResponseDTO;
import bg.sofia.uni.fmi.mjt.battleships.online.server.storage.Storage;
import bg.sofia.uni.fmi.mjt.battleships.online.server.storage.UserIn;

public class CurrentGame {
    private static final String NOT_IN_GAME = "No current game";
    private static final String CURRENT_GAME_MSG = "You are currently %s in %s";

    public static CommandResponseDTO execute(Storage storage, String id) {
        if (storage.getApiKey(id) == null) {
            return new CommandResponseDTO(NOT_IN_GAME);
        }

        UserIn userIn = storage.getUserIn(id);

        if (userIn.getCurrGame() == null) {
            return new CommandResponseDTO(NOT_IN_GAME);
        }

        return new CommandResponseDTO(
                String.format(CURRENT_GAME_MSG, userIn.getStatus(), userIn.getCurrGame().getGameName())
        );
    }
}
