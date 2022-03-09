package bg.sofia.uni.fmi.mjt.battleships.online.server.command.mode.surfing;

import bg.sofia.uni.fmi.mjt.battleships.online.server.command.response.CommandResponseDTO;
import bg.sofia.uni.fmi.mjt.battleships.online.server.game.Game;
import bg.sofia.uni.fmi.mjt.battleships.online.server.game.player.PlayerStatus;
import bg.sofia.uni.fmi.mjt.battleships.online.server.storage.Storage;
import bg.sofia.uni.fmi.mjt.battleships.online.server.storage.UserIn;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
public class CurrentGameTest {
    private static final String NOT_IN_GAME = "No current game";
    private static final String CURRENT_GAME_MSG = "You are currently %s in %s";

    private final String hostId = "host-id";
    private final String hostApiKey = "host-api-key";

    @Mock
    private Storage storageMock;

    @Mock
    private UserIn userInMock;

    @Mock
    private Game gameMock;

    @Test
    public void testCurrentGameNotRegistered() {
        CommandResponseDTO response = CurrentGame.execute(storageMock, hostId);

        assertEquals(NOT_IN_GAME, response.getMsgThis(),
                "Response should contain not in game message");
    }

    @Test
    public void testCurrentGameNotInGame() {
        when(storageMock.getApiKey(hostId)).thenReturn(hostApiKey);
        when(storageMock.getUserIn(hostId)).thenReturn(userInMock);
        when(userInMock.getCurrGame()).thenReturn(null);

        CommandResponseDTO response = CurrentGame.execute(storageMock, hostId);

        assertEquals(NOT_IN_GAME, response.getMsgThis(),
                "Response should contain not in game message");
    }

    @Test
    public void testCurrentGame() {
        when(storageMock.getApiKey(hostId)).thenReturn(hostApiKey);
        when(storageMock.getUserIn(hostId)).thenReturn(userInMock);

        when(userInMock.getStatus()).thenReturn(PlayerStatus.HOST);
        when(userInMock.getCurrGame()).thenReturn(gameMock);
        when(gameMock.getGameName()).thenReturn("current-game");

        CommandResponseDTO response = CurrentGame.execute(storageMock, hostId);

        assertEquals(String.format(CURRENT_GAME_MSG, PlayerStatus.HOST, "current-game"), response.getMsgThis(),
                "Response should information about the current game");
    }
}
