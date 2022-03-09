package bg.sofia.uni.fmi.mjt.battleships.online.server.command.mode.surfing;

import bg.sofia.uni.fmi.mjt.battleships.online.server.command.response.CommandResponseDTO;
import bg.sofia.uni.fmi.mjt.battleships.online.server.storage.Storage;
import bg.sofia.uni.fmi.mjt.battleships.online.server.storage.UserIn;
import com.google.gson.Gson;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.stream.Stream;

import static bg.sofia.uni.fmi.mjt.battleships.online.server.command.manager.CommandExecutor.DEFAULT;
import static bg.sofia.uni.fmi.mjt.battleships.online.server.command.manager.CommandExecutor.RED;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
public class LoginTest {
    private static final String LOGIN_USAGE = "Requires one argument. Usage: login <api-key>";
    private static final String ALREADY_LOGGED = "You are already logged in";
    private static final String LOGIN_SUCCESS = "You are now logged in with api-key: %s%s%s";
    private static final String NO_SUCH_API_KEY = "No such api-key";
    private static final String ALREADY_USER_WITH_API_KEY_ONLINE = "There already is a user with such api-key surfing";
    private static final String FAIL_TO_READ_SAVED_GAMES =
            "%sFailed to read from server file system to load saved games in dynamic memory%s";

    private final String hostId = "host-id";
    private final String hostApiKey = "489489489";

    private final Gson gson = new Gson();

    @Mock
    private Storage storageMock;

    @Mock
    private UserIn userInMock;

    @Test
    public void testLoginNoApiKeyAsArgument() {
        String[] args = {};
        CommandResponseDTO response = Login.execute(storageMock, hostId, gson, args);

        assertEquals(LOGIN_USAGE, response.getMsgThis(), "Response should contain login-usage message");
    }

    @Test
    public void testLoginAlreadyLogged() {
        when(storageMock.getApiKey(hostId)).thenReturn(hostApiKey);
        CommandResponseDTO response = Login.execute(storageMock, hostId, gson, hostApiKey);

        assertEquals(ALREADY_LOGGED, response.getMsgThis(), "Response should contain already logged message");
    }

    @Test
    public void testLoginNoSuchApiKeyInServer() {
        when(storageMock.containsSavedGamesForApiKey(hostApiKey)).thenReturn(false);

        CommandResponseDTO response = Login.execute(storageMock, hostId, gson, hostApiKey);

        assertEquals(NO_SUCH_API_KEY, response.getMsgThis(),
                "Response should contain no such api-key message");
    }

    @Test
    public void testLoginWithAlreadyLoggedInUserWithThatApiKey() {
        when(storageMock.containsSavedGamesForApiKey(hostApiKey)).thenReturn(true);
        when(storageMock.getUsersInAsStream()).thenReturn(Stream.of(userInMock));
        when(userInMock.getApiKey()).thenReturn(hostApiKey);

        CommandResponseDTO response = Login.execute(storageMock, hostId, gson, hostApiKey);

        assertEquals(ALREADY_USER_WITH_API_KEY_ONLINE, response.getMsgThis(),
                "Response should contain user with current api-key online message");
    }

    @Test
    public void testLoginSuccessfulLoginButFailedToReadSavedGamesFromServerFileSystem() {
        when(storageMock.containsSavedGamesForApiKey(hostApiKey)).thenReturn(true);
        when(storageMock.getUsersInAsStream()).thenReturn(Stream.of());

        doNothing().when(storageMock).setApiKey(hostId, hostApiKey);

        CommandResponseDTO response = Login.execute(storageMock, hostId, gson, hostApiKey);

        assertEquals(
                String.format(LOGIN_SUCCESS, RED, hostApiKey, DEFAULT) +
                        System.lineSeparator() + String.format(FAIL_TO_READ_SAVED_GAMES, RED, DEFAULT)
                , response.getMsgThis(),
                "Response should successful login message");
    }

    @Test
    public void testLogin() throws IOException {
        when(storageMock.containsSavedGamesForApiKey(hostApiKey)).thenReturn(true);
        when(storageMock.getUsersInAsStream()).thenReturn(Stream.of());

        doNothing().when(storageMock).setApiKey(hostId, hostApiKey);

        Path filePath = Path.of("saved", hostApiKey);
        Files.createDirectories(filePath.getParent());

        FileWriter myWriter = new FileWriter(filePath.toString());
        myWriter.write("{}");
        myWriter.close();

        CommandResponseDTO response = Login.execute(storageMock, hostId, gson, hostApiKey);

        deleteDirectoryRecursively(filePath);

        assertEquals(
                String.format(LOGIN_SUCCESS, RED, hostApiKey, DEFAULT), response.getMsgThis(),
                "Response should successful login message");
    }

    private void deleteDirectoryRecursively(Path pathToDelete) throws IOException {
        if (Files.exists(pathToDelete)) {
            File[] allContents = pathToDelete.toFile().listFiles();
            if (allContents != null) {
                for (File file : allContents) {
                    deleteDirectoryRecursively(file.toPath());
                }
            }
            Files.delete(pathToDelete);
        }
    }
}


