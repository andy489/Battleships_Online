package bg.sofia.uni.fmi.mjt.battleships.online.server.file.manager;

import bg.sofia.uni.fmi.mjt.battleships.online.server.exception.Log;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.time.LocalDateTime;

import static bg.sofia.uni.fmi.mjt.battleships.online.server.command.manager.CommandExecutor.CURR_DIR;
import static bg.sofia.uni.fmi.mjt.battleships.online.server.command.manager.CommandExecutor.SAVED_GAMES_DIR;

public class FileManager {
    private static final String GAME_SAVE_FAIL = "Failed to save games in server file system";

    public static String getSavePath(String apiKey) {
        return CURR_DIR + File.separator + SAVED_GAMES_DIR + File.separator + apiKey;
    }

    public static void writeSavedGames(String apiKey, String gamesToSave, String savePath) {
        File savedFile = new File(savePath);
        try (BufferedWriter writer = new BufferedWriter(new FileWriter(savedFile, false))) {
            writer.write(gamesToSave);
        } catch (IOException e1) {
            Log log = new Log(apiKey, LocalDateTime.now(), GAME_SAVE_FAIL);
            System.err.println(log);
        }
    }
}
