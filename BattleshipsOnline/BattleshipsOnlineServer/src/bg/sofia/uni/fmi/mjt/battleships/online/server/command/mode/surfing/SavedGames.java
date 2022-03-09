package bg.sofia.uni.fmi.mjt.battleships.online.server.command.mode.surfing;

import bg.sofia.uni.fmi.mjt.battleships.online.server.command.response.CommandResponseDTO;
import bg.sofia.uni.fmi.mjt.battleships.online.server.storage.ListGamesViewHelper;
import bg.sofia.uni.fmi.mjt.battleships.online.server.storage.Storage;

public class SavedGames {
    private static final String LOGIN_OR_REGISTER = "You must login or register first";

    public static CommandResponseDTO execute(Storage storage, String id) {
        String apiKey = storage.getApiKey(id);

        if (apiKey == null) {
            return new CommandResponseDTO(LOGIN_OR_REGISTER);
        }

        StringBuilder sb = ListGamesViewHelper.getHeader();

        storage.getSavedGamesAsStream(apiKey).forEach(ListGamesViewHelper.getMySavedGamesListConsumer(sb));

        sb.setLength(sb.length() - 1);

        return new CommandResponseDTO(sb.toString());
    }
}
