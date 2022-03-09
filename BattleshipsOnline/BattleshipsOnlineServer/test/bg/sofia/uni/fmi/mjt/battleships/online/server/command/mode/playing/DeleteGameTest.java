package bg.sofia.uni.fmi.mjt.battleships.online.server.command.mode.playing;

import bg.sofia.uni.fmi.mjt.battleships.online.server.command.response.CommandResponseDTO;
import bg.sofia.uni.fmi.mjt.battleships.online.server.storage.Storage;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import static bg.sofia.uni.fmi.mjt.battleships.online.server.command.manager.CommandExecutor.DEFAULT;
import static bg.sofia.uni.fmi.mjt.battleships.online.server.command.manager.CommandExecutor.RED;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
public class DeleteGameTest {
    private static final String NOT_LOGGED_IN = "You are not logged in. Cannot delete saved game";
    private static final String NO_SUCH_GAME = "Could not delete \"%s\". No such game-name";
    private static final String GAME_DELETED = "Game \"%s\" was deleted";
    private static final String GAME_DELETED_USAGE = "Requires at least one game-name. Usage: delete-game <game-name>";
    private static final String TRYING = "Trying to delete selected game(s)";

    private final String hostId = "host-id";
    private final String hostApiKey = "host-api-key";

    @Mock
    private Storage storageMock;

    @Test
    public void testDeleteGameNoApi() {
        CommandResponseDTO response = DeleteGame.execute(storageMock, hostId, "my-game");

        assertEquals(NOT_LOGGED_IN, response.getMsgThis(),
                "Response should contain not logged in usage message");
    }

    @Test
    public void testDeleteGameNoArguments() {
        when(storageMock.getApiKey(hostId)).thenReturn(hostApiKey);
        CommandResponseDTO response = DeleteGame.execute(storageMock, hostId);

        assertEquals(GAME_DELETED_USAGE, response.getMsgThis(),
                "Response should contain delete-game usage message");
    }

    @Test
    public void testDeleteGameTwoGames() {
        when(storageMock.getApiKey(hostId)).thenReturn(hostApiKey);
        when(storageMock.containsGameName(hostApiKey, "my-game")).thenReturn(true);
        when(storageMock.containsGameName(hostApiKey, "new-game")).thenReturn(false);

        CommandResponseDTO response = DeleteGame.execute(storageMock, hostId, "my-game", "new-game");

        StringBuilder sb = new StringBuilder(TRYING).append(System.lineSeparator())
                .append(RED).append("> ").append(DEFAULT)
                .append(String.format(GAME_DELETED, "my-game"))
                .append(System.lineSeparator())
                .append(RED).append("> ").append(DEFAULT)
                .append(String.format(NO_SUCH_GAME, "new-game"))
                .append(System.lineSeparator());

        sb.setLength(sb.length() - 1);

        assertEquals(sb.toString(), response.getMsgThis(),
                "Expected first game to be deleted and second one not");
    }
}
