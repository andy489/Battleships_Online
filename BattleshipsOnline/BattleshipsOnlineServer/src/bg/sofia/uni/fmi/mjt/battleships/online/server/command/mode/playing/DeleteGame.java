package bg.sofia.uni.fmi.mjt.battleships.online.server.command.mode.playing;

import bg.sofia.uni.fmi.mjt.battleships.online.server.command.response.CommandResponseDTO;
import bg.sofia.uni.fmi.mjt.battleships.online.server.storage.Storage;

import static bg.sofia.uni.fmi.mjt.battleships.online.server.command.manager.CommandExecutor.DEFAULT;
import static bg.sofia.uni.fmi.mjt.battleships.online.server.command.manager.CommandExecutor.RED;

public class DeleteGame {
    private static final String NOT_LOGGED_IN = "You are not logged in. Cannot delete saved game";
    private static final String NO_SUCH_GAME = "Could not delete \"%s\". No such game-name";
    private static final String GAME_DELETED = "Game \"%s\" was deleted";
    private static final String GAME_DELETED_USAGE = "Requires at least one game-name. Usage: delete-game <game-name>";
    private static final String TRYING = "Trying to delete selected game(s)";

    public static CommandResponseDTO execute(Storage storage, String id, String... args) {
        String apiKey = storage.getApiKey(id);

        if (apiKey == null) {
            return new CommandResponseDTO(NOT_LOGGED_IN);
        }

        if (args.length == 0) {
            return new CommandResponseDTO(GAME_DELETED_USAGE);
        }

        StringBuilder sb = new StringBuilder(TRYING).append(System.lineSeparator());

        for (final String currentGameName : args) {
            if (storage.containsGameName(apiKey, currentGameName)) {
                storage.removeSavedGame(apiKey, currentGameName);
                sb
                        .append(RED).append("> ").append(DEFAULT)
                        .append(String.format(GAME_DELETED, currentGameName))
                        .append(System.lineSeparator());
            } else {
                sb
                        .append(RED).append("> ").append(DEFAULT)
                        .append(String.format(NO_SUCH_GAME, currentGameName))
                        .append(System.lineSeparator());
            }
        }

        sb.setLength(sb.length() - 1);

        return new CommandResponseDTO(sb.toString());
    }
}
