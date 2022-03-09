package bg.sofia.uni.fmi.mjt.battleships.online.server.command.mode.surfing;

import bg.sofia.uni.fmi.mjt.battleships.online.server.command.response.CommandResponseDTO;
import bg.sofia.uni.fmi.mjt.battleships.online.server.game.Game;
import bg.sofia.uni.fmi.mjt.battleships.online.server.game.GameStatus;
import bg.sofia.uni.fmi.mjt.battleships.online.server.game.player.Player;
import bg.sofia.uni.fmi.mjt.battleships.online.server.game.player.PlayerStatus;
import bg.sofia.uni.fmi.mjt.battleships.online.server.storage.Storage;
import bg.sofia.uni.fmi.mjt.battleships.online.server.storage.UserIn;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.nio.channels.SocketChannel;
import java.util.stream.Stream;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertSame;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
public class JoinGameTest {
    private static final String BLUE = "\u001B[34m";
    private static final String DEFAULT = "\u001B[0m";

    private static final String LOGIN_OR_REGISTER = "You must login or register first";
    private static final String ALREADY_IN_A_GAME = "You are already in a game";
    private static final String GAME_IN_PROGRESS = "Cannot join game \"%s\" -> Game is in progress";
    private static final String HOST_MSG = "Joined game \"%s\". Place your ships!";
    private static final String GUEST_MSG = "%s%s%s joined the game. Place your ships!";
    private static final String NO_RANDOM_GAME = "There is no pending game at the moment";
    private static final String NO_SUCH_GAME = "There is no such game";

    private final String hostId = "host-id";
    private final String hostApiKey = "host-api-key";

    @Mock
    private Storage storageMock;

    @Mock
    private UserIn userInMock;

    @Mock
    private Game gameMock;

    @Mock
    private Player hostPlayerMock;

    @Mock
    private SocketChannel hostPlayerSocketChannelMock;

    @Test
    public void testJoinGameNotLoggedIn() {
        String[] args = {"my-game"};
        CommandResponseDTO response = JoinGame.execute(storageMock, hostId, hostPlayerSocketChannelMock, args);

        assertEquals(LOGIN_OR_REGISTER, response.getMsgThis(),
                "Response should contain not logged in message");
    }

    @Test
    public void testJoinGameWhenAlreadyPlaying() {
        when(storageMock.getApiKey(hostId)).thenReturn(hostApiKey);
        when(storageMock.getPlayerStatus(hostId)).thenReturn(PlayerStatus.HOST);

        String[] args = {"my-game"};
        CommandResponseDTO response = JoinGame.execute(storageMock, hostId, hostPlayerSocketChannelMock, args);

        assertEquals(ALREADY_IN_A_GAME, response.getMsgThis(),
                "Response should contain already in a game message");
    }

    @Test
    public void testJoinGameInProgress() {
        when(storageMock.getApiKey(hostId)).thenReturn(hostApiKey);
        when(storageMock.getPlayerStatus(hostId)).thenReturn(PlayerStatus.ONLINE);
        when(storageMock.getUsersInAsStream()).thenReturn(Stream.of(userInMock));
        when(userInMock.getCurrGame()).thenReturn(gameMock);

        String[] args = {"my-game"};
        when(gameMock.getGameName()).thenReturn(args[0]);
        when(gameMock.getStatus()).thenReturn(GameStatus.IN_PROGRESS);

        CommandResponseDTO response = JoinGame.execute(storageMock, hostId, hostPlayerSocketChannelMock, args);

        assertEquals(String.format(GAME_IN_PROGRESS, args[0]), response.getMsgThis(),
                "Response should contain game in progress message");
    }

    @Test
    public void testJoinGameRandom() {
        when(storageMock.getApiKey(hostId)).thenReturn(hostApiKey);
        when(storageMock.getPlayerStatus(hostId)).thenReturn(PlayerStatus.ONLINE);
        when(storageMock.getUsersInAsStream()).thenReturn(Stream.of(userInMock));
        when(userInMock.getCurrGame()).thenReturn(gameMock);

        String[] args = {};

        when(gameMock.getGameName()).thenReturn("my-game");
        when(gameMock.getStatus()).thenReturn(GameStatus.PENDING);

        String hostUsername = "host-username";
        when(storageMock.getUsername(hostId)).thenReturn(hostUsername);
        doNothing().when(gameMock).join(
                any(String.class),
                any(String.class),
                any(SocketChannel.class),
                any(String.class)
        );

        doNothing().when(storageMock).setCurrentGame(any(String.class), any(Game.class));
        doNothing().when(storageMock).setPlayerStatus(any(String.class), any(PlayerStatus.class));

        when(gameMock.getHostPlayer()).thenReturn(hostPlayerMock);
        when(hostPlayerMock.socketChannel()).thenReturn(hostPlayerSocketChannelMock);

        CommandResponseDTO response = JoinGame.execute(storageMock, hostId, hostPlayerSocketChannelMock, args);

        assertEquals(String.format(HOST_MSG, "my-game"), response.getMsgThis(),
                "Response should contain host join-game message");
        assertEquals(String.format(GUEST_MSG, BLUE, hostUsername, DEFAULT), response.getMsgOther(),
                "Response should contain guest join-game message");

        assertSame(hostPlayerSocketChannelMock, response.getHostSocketChannel(),
                "Expected same socket channel");
    }

    @Test
    public void testJoinGameRandomNoGames() {
        when(storageMock.getApiKey(hostId)).thenReturn(hostApiKey);
        when(storageMock.getPlayerStatus(hostId)).thenReturn(PlayerStatus.ONLINE);
        when(storageMock.getUsersInAsStream()).thenReturn(Stream.empty());

        String[] args = {};

        CommandResponseDTO response = JoinGame.execute(storageMock, hostId, hostPlayerSocketChannelMock, args);

        assertEquals(NO_RANDOM_GAME, response.getMsgThis(), "Response should have no random game message");
    }

    @Test
    public void testJoinGameNoSuchGame() {
        when(storageMock.getApiKey(hostId)).thenReturn(hostApiKey);
        when(storageMock.getPlayerStatus(hostId)).thenReturn(PlayerStatus.ONLINE);
        when(storageMock.getUsersInAsStream()).thenReturn(Stream.empty());

        String[] args = {"my-game"};

        CommandResponseDTO response = JoinGame.execute(storageMock, hostId, hostPlayerSocketChannelMock, args);

        assertEquals(NO_SUCH_GAME, response.getMsgThis(), "Response should have no such game message");
    }
}
