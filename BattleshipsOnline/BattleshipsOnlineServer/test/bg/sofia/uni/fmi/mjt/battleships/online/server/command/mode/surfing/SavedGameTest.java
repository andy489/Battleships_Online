package bg.sofia.uni.fmi.mjt.battleships.online.server.command.mode.surfing;

import bg.sofia.uni.fmi.mjt.battleships.online.server.command.response.CommandResponseDTO;
import bg.sofia.uni.fmi.mjt.battleships.online.server.storage.Storage;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.stream.Stream;

import static bg.sofia.uni.fmi.mjt.battleships.online.server.command.manager.CommandExecutor.RED;
import static bg.sofia.uni.fmi.mjt.battleships.online.server.command.manager.CommandExecutor.DEFAULT;
import static bg.sofia.uni.fmi.mjt.battleships.online.server.command.manager.CommandExecutor.RED_SEP;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
public class SavedGameTest {
    private static final String LOGIN_OR_REGISTER = "You must login or register first";

    private final String hostId = "host-id";

    @Mock
    private Storage storageMock;

    @Test
    public void testSavedGamesNotLoggedIn() {
        CommandResponseDTO response = SavedGames.execute(storageMock, hostId);

        assertEquals(LOGIN_OR_REGISTER, response.getMsgThis(),
                "Response should contain login or register message");
    }

    @Test
    public void testSavedGames() {
        String hostApiKey = "host-api-key";
        when(storageMock.getApiKey(hostId)).thenReturn(hostApiKey);
        when(storageMock.getSavedGamesAsStream(hostApiKey)).thenReturn(Stream.empty());

        String sb = RED_SEP + " NAME                       " + RED_SEP + " CREATOR     " +
                RED_SEP + " STATUS      " + RED_SEP + " PLAYERS     " +
                RED_SEP + System.lineSeparator() + RED + "> |" + DEFAULT +
                "----------------------------+-------------+-------------+-------------" + RED_SEP;

        CommandResponseDTO response = SavedGames.execute(storageMock, hostId);

        assertEquals(sb, response.getMsgThis(), "Response should contain empty table of saved games");
    }
}
