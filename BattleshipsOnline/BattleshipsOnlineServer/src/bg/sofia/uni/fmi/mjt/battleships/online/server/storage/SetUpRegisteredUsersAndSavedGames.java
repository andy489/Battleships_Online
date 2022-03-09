package bg.sofia.uni.fmi.mjt.battleships.online.server.storage;

import bg.sofia.uni.fmi.mjt.battleships.online.server.game.SavedGame;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.io.File;
import java.io.IOException;
import java.lang.reflect.Type;
import java.nio.file.DirectoryStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.Map;

import static bg.sofia.uni.fmi.mjt.battleships.online.server.command.manager.CommandExecutor.CURR_DIR;
import static bg.sofia.uni.fmi.mjt.battleships.online.server.command.manager.CommandExecutor.SAVED_GAMES_DIR;

public class SetUpRegisteredUsersAndSavedGames {
    private static final String FAIL_READ_START_SERVER = "Fail to read from server files to init registered users";
    private static final String SAVED_GAMES_DIR_CREATED = "Created new directory for registered users saved games";

    public static void setUp(Storage storage, Gson gson) {
        Path savedDir = Path.of(CURR_DIR + File.separator + SAVED_GAMES_DIR);

        if (Files.exists(savedDir)) {
            try (DirectoryStream<Path> stream = Files.newDirectoryStream(savedDir, "[0-9]*")) {
                for (Path entry : stream) {
                    String apiKey = entry.getFileName().toString();

                    String text = Files.readString(entry);

                    Type typeToken = new TypeToken<Map<String, SavedGame>>() {
                    }.getType();

                    Map<String, SavedGame> savedGames = gson.fromJson(text, typeToken);

                    storage.initSavedGamesAndRegisteredUsers(apiKey, savedGames);
                }
            } catch (IOException e) {
                System.err.println(FAIL_READ_START_SERVER);
            }
        } else {
            if (new File(savedDir.toString()).mkdirs()) {
                System.out.println(SAVED_GAMES_DIR_CREATED);
            }
        }
    }
}
