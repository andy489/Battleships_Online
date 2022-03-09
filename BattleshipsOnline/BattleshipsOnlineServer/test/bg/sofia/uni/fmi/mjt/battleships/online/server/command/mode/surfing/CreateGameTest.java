package bg.sofia.uni.fmi.mjt.battleships.online.server.command.mode.surfing;

import bg.sofia.uni.fmi.mjt.battleships.online.server.command.response.CommandResponseDTO;
import bg.sofia.uni.fmi.mjt.battleships.online.server.game.Game;
import bg.sofia.uni.fmi.mjt.battleships.online.server.game.player.PlayerStatus;
import bg.sofia.uni.fmi.mjt.battleships.online.server.storage.Storage;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.nio.channels.SocketChannel;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
public class CreateGameTest {
    private static final String CREATE_GAME_USAGE = "Requires one argument. Usage: create-game <game-name>";
    private static final String LOGIN_OR_REGISTER = "You must login or register first";
    private static final String ALREADY_IN_A_GAME = "You are already in a game";
    private static final String GAME_ALREADY_EXISTS = "Game \"%s\" already exists. Choose another game-name";
    private static final String GAME_CREATED = "Created game \"%s\", players 1/2";

    private final String hostId = "host-id";
    private final String hostApiKey = "host-api-key";

    @Mock
    private Storage storageMock;

    @Mock
    private SocketChannel hostPlayerSocketChannelMock;

    @Test
    public void testCreateGameNoArguments() {
        String[] args = {};
        CommandResponseDTO response = CreateGame.execute(storageMock, hostId, hostPlayerSocketChannelMock, args);

        assertEquals(CREATE_GAME_USAGE, response.getMsgThis(),
                "Response should contain create-game command usage message");
    }

    @Test
    public void testCreateNotLoggedIn() {
        String[] args = {"my-game"};
        CommandResponseDTO response = CreateGame.execute(storageMock, hostId, hostPlayerSocketChannelMock, args);

        assertEquals(LOGIN_OR_REGISTER, response.getMsgThis(),
                "Response should contain not logged in message");
    }

    @Test
    public void testCreateGameAlreadyPlaying() {
        when(storageMock.getApiKey(hostId)).thenReturn(hostApiKey);
        when(storageMock.getPlayerStatus(hostId)).thenReturn(PlayerStatus.HOST);

        String[] args = {"my-game"};
        CommandResponseDTO response = CreateGame.execute(storageMock, hostId, hostPlayerSocketChannelMock, args);

        assertEquals(ALREADY_IN_A_GAME, response.getMsgThis(),
                "Response should contain already in a game in message");
    }

    @Test
    public void testCreateGameGameWithThatNameAlreadyExists() {
        when(storageMock.getApiKey(hostId)).thenReturn(hostApiKey);
        when(storageMock.getPlayerStatus(hostId)).thenReturn(PlayerStatus.ONLINE);

        String[] args = {"my-game"};
        when(storageMock.checkForExistingGameName(args[0])).thenReturn(true);

        CommandResponseDTO response = CreateGame.execute(storageMock, hostId, hostPlayerSocketChannelMock, args);

        assertEquals(String.format(GAME_ALREADY_EXISTS,"my-game"), response.getMsgThis(),
                "Response should contain game with that name already exists message");
    }

    @Test
    public void testCreateGame() {
        when(storageMock.getApiKey(hostId)).thenReturn(hostApiKey);
        when(storageMock.getPlayerStatus(hostId)).thenReturn(PlayerStatus.ONLINE);

        String[] args = {"my-game"};
        when(storageMock.checkForExistingGameName(args[0])).thenReturn(false);
        when(storageMock.getUsername(hostId)).thenReturn("host-username");

        doNothing().when(storageMock).setCurrentGame(any(String.class), any(Game.class));
        doNothing().when(storageMock).setPlayerStatus(hostId, PlayerStatus.HOST);

        CommandResponseDTO response = CreateGame.execute(storageMock, hostId, hostPlayerSocketChannelMock, args);

        assertEquals(String.format(GAME_CREATED, args[0]), response.getMsgThis(),
                "Response should contain successful game creation message");
    }
}
