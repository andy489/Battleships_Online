package bg.sofia.uni.fmi.mjt.battleships.online.server.command.mode;

import bg.sofia.uni.fmi.mjt.battleships.online.server.command.response.CommandResponseDTO;
import bg.sofia.uni.fmi.mjt.battleships.online.server.game.Game;
import bg.sofia.uni.fmi.mjt.battleships.online.server.game.GameStatus;
import bg.sofia.uni.fmi.mjt.battleships.online.server.game.SavedGame;
import bg.sofia.uni.fmi.mjt.battleships.online.server.game.player.Player;
import bg.sofia.uni.fmi.mjt.battleships.online.server.game.player.board.Board;
import bg.sofia.uni.fmi.mjt.battleships.online.server.storage.Storage;
import bg.sofia.uni.fmi.mjt.battleships.online.server.storage.UserIn;
import com.google.gson.Gson;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.io.File;
import java.io.IOException;
import java.nio.channels.SocketChannel;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.HashMap;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertSame;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
public class DisconnectTest {
    private static final String BLUE = "\u001B[34m";
    private static final String DEFAULT = "\u001B[0m";

    private static final String HOST_DISCONNECT = "%s%s%s exit the game. Waiting for another player to join...";

    private final String hostId = "host-id";
    private final String hostApiKey = "host-api-key";

    private final Gson gson = new Gson();

    @Mock
    private Storage storageMock;

    @Mock
    private UserIn hostUserInMock;

    @Mock
    private UserIn guestUserInMock;

    @Mock
    private Game gameMock;

    @Mock
    private Player hostPlayerMock;

    @Mock
    private Player guestPlayerMock;

    @Mock
    private SocketChannel guestPlayerSocketChannelMock;

    @Mock
    private Board hostPlayerBoardMock;

    @Test
    public void testDisconnect() {
        doNothing().when(storageMock).removeUserIn(hostId);

        CommandResponseDTO response = Disconnect.execute(storageMock, hostId, gson);

        assertSame(null, response.getMsgThis(), "Response is not needed, because user is disconnected");
    }

    @Test
    public void testDisconnectGamePending() throws IOException {
        when(storageMock.getApiKey(hostId)).thenReturn(hostApiKey);

        when(storageMock.getUserIn(hostId)).thenReturn(hostUserInMock);
        when(hostUserInMock.getCurrGame()).thenReturn(gameMock);
        when(gameMock.getStatus()).thenReturn(GameStatus.PENDING);

        doNothing().when(storageMock).removeUserIn(hostId);

        SavedGame savedGameStub = new SavedGame(
                "my-game",
                new Board(),
                new Board(),
                true,
                true
        );

        Map<String, SavedGame> apiKeySavedGamesStub = new HashMap<>();
        apiKeySavedGamesStub.put(hostApiKey, savedGameStub);

        when(storageMock.getGamesToSave(hostApiKey)).thenReturn(apiKeySavedGamesStub);

        Disconnect.execute(storageMock, hostId, gson);

        Path filePath = Path.of("saved", hostApiKey);
        deleteDirectoryRecursively(filePath);
    }

    @Test
    public void testDisconnectGameHostPlayer() throws IOException {
        when(storageMock.getApiKey(hostId)).thenReturn(hostApiKey);

        when(storageMock.getUserIn(hostId)).thenReturn(hostUserInMock);
        String guestId = "guest-id";
        when(storageMock.getUserIn(guestId)).thenReturn(guestUserInMock);
        when(hostUserInMock.getCurrGame()).thenReturn(gameMock);
        when(hostUserInMock.getUsername()).thenReturn("host-username");
        when(storageMock.getCurrentGame(hostId)).thenReturn(gameMock);

        when(gameMock.getStatus()).thenReturn(GameStatus.IN_PROGRESS);
        when(gameMock.getGameName()).thenReturn("my-game");
        when(gameMock.getHostTurn()).thenReturn(true);

        doNothing().when(storageMock).removeUserIn(hostId);

        when(storageMock.containsGameName(hostApiKey, "my-game")).thenReturn(true);

        SavedGame savedGameStub = new SavedGame(
                "my-game",
                new Board(),
                new Board(),
                true,
                true
        );

        Map<String, SavedGame> apiKeySavedGamesStub = new HashMap<>();
        apiKeySavedGamesStub.put(hostApiKey, savedGameStub);

        when(storageMock.getGamesToSave(hostApiKey)).thenReturn(apiKeySavedGamesStub);

        when(gameMock.getHostPlayer()).thenReturn(hostPlayerMock);
        when(gameMock.getGuestPlayer()).thenReturn(guestPlayerMock);

        when(hostPlayerMock.id()).thenReturn(hostId);
        when(guestPlayerMock.id()).thenReturn(guestId);

        when(hostPlayerMock.board()).thenReturn(hostPlayerBoardMock);

        when(guestPlayerMock.socketChannel()).thenReturn(guestPlayerSocketChannelMock);

        CommandResponseDTO response = Disconnect.execute(storageMock, hostId, gson);

        Path filePath = Path.of("saved", hostApiKey);
        deleteDirectoryRecursively(filePath);

        assertEquals(String.format(HOST_DISCONNECT, BLUE, "host-username", DEFAULT), response.getMsgOther(),
                "Expected message to guest player, indicating that host player left the game");
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
