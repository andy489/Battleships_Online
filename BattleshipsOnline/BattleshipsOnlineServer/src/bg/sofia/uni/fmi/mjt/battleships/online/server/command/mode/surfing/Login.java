package bg.sofia.uni.fmi.mjt.battleships.online.server.command.mode.surfing;

import bg.sofia.uni.fmi.mjt.battleships.online.server.command.response.CommandResponseDTO;
import bg.sofia.uni.fmi.mjt.battleships.online.server.game.SavedGame;
import bg.sofia.uni.fmi.mjt.battleships.online.server.storage.Storage;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.io.IOException;
import java.lang.reflect.Type;
import java.nio.file.Path;
import java.util.Map;

import static bg.sofia.uni.fmi.mjt.battleships.online.server.command.manager.CommandExecutor.DEFAULT;
import static bg.sofia.uni.fmi.mjt.battleships.online.server.command.manager.CommandExecutor.RED;
import static bg.sofia.uni.fmi.mjt.battleships.online.server.file.manager.FileManager.getSavePath;

import static java.nio.file.Files.readString;

public class Login {
    private static final String LOGIN_USAGE = "Requires one argument. Usage: login <api-key>";
    private static final String ALREADY_LOGGED = "You are already logged in";
    private static final String LOGIN_SUCCESS = "You are now logged in with api-key: %s%s%s";
    private static final String NO_SUCH_API_KEY = "No such api-key";
    private static final String ALREADY_USER_WITH_API_KEY_ONLINE = "There already is a user with such api-key surfing";
    private static final String FAIL_TO_READ_SAVED_GAMES =
            "%sFailed to read from server file system to load saved games in dynamic memory%s";

    public static CommandResponseDTO execute(Storage storage, String id, Gson gson, String... args) {
        if (args.length == 0) {
            return new CommandResponseDTO(LOGIN_USAGE);
        }

        if (storage.getApiKey(id) != null) {
            return new CommandResponseDTO(ALREADY_LOGGED);
        }

        String apiKey = args[0];

        if (!storage.containsSavedGamesForApiKey(apiKey)) {
            return new CommandResponseDTO(NO_SUCH_API_KEY);
        }

        if (storage.getUsersInAsStream()
                .filter(u -> u.getApiKey() != null)
                .anyMatch(u -> u.getApiKey().equals(apiKey))
        ) {
            return new CommandResponseDTO(ALREADY_USER_WITH_API_KEY_ONLINE);
        }

        storage.setApiKey(id, apiKey);

        Type typeToken = new TypeToken<Map<String, SavedGame>>() {
        }.getType();

        String savePath = getSavePath(apiKey);
        try {
            String text = readString(Path.of(savePath));
            Map<String, SavedGame> savedGames = gson.fromJson(text, typeToken);

            storage.initSavedGamesAndRegisteredUsers(apiKey, savedGames);
        } catch (IOException e) {
            return new CommandResponseDTO(
                    String.format(LOGIN_SUCCESS, RED, apiKey, DEFAULT) +
                            System.lineSeparator() + String.format(FAIL_TO_READ_SAVED_GAMES, RED, DEFAULT));
        }

        return new CommandResponseDTO(String.format(LOGIN_SUCCESS, RED, apiKey, DEFAULT));
    }
}
