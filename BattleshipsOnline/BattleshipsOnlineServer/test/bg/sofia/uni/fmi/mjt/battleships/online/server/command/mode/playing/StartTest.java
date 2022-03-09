package bg.sofia.uni.fmi.mjt.battleships.online.server.command.mode.playing;

import bg.sofia.uni.fmi.mjt.battleships.online.server.command.response.CommandResponseDTO;
import bg.sofia.uni.fmi.mjt.battleships.online.server.game.Game;
import bg.sofia.uni.fmi.mjt.battleships.online.server.game.GameStatus;
import bg.sofia.uni.fmi.mjt.battleships.online.server.game.player.Player;
import bg.sofia.uni.fmi.mjt.battleships.online.server.game.player.PlayerStatus;
import bg.sofia.uni.fmi.mjt.battleships.online.server.game.player.board.Board;
import bg.sofia.uni.fmi.mjt.battleships.online.server.storage.Storage;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.nio.channels.SocketChannel;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
public class StartTest {
    private static final String BLUE = "\u001B[34m";
    private static final String DEFAULT = "\u001B[0m";

    private static final String PLACE_ALL_YOUR_SHIPS = "You must place all your ships first";
    private static final String THIS_PLAYER_READY = "You are ready to start the battle";
    private static final String OTHER_PLAYER_READY = "%s%s%s is ready to start the battle";
    private static final String SINGLE_PLAYER_MODE_MSG = "Cannot start a single player mode";
    private static final String NOT_IN_GAME = "No current game";

    private final String hostId = "host-id";
    private final String guestId = "guest-id";

    @Mock
    private Storage storageMock;

    @Mock
    private Game gameMock;

    @Mock
    private Player hostPlayerMock;

    @Mock
    private Player guestPlayerMock;

    @Mock
    private SocketChannel hostPlayerSocketChannelMock;

    @Mock
    private SocketChannel guestPlayerSocketChannelMock;

    @Mock
    private Board hostPlayerBoardMock;

    @Mock
    private Board guestPlayerBoardMock;

    @Test
    public void testStartNotPlaying() {
        when(storageMock.getPlayerStatus(hostId)).thenReturn(PlayerStatus.ONLINE);

        CommandResponseDTO response = Start.execute(storageMock, hostId);

        assertEquals(NOT_IN_GAME, response.getMsgThis(),
                "Response should contain not in a game message");
    }

    @Test
    public void testStartNotHavingOpponent() {
        when(storageMock.getPlayerStatus(hostId)).thenReturn(PlayerStatus.HOST);
        when(storageMock.getCurrentGame(hostId)).thenReturn(gameMock);
        when(gameMock.getStatus()).thenReturn(GameStatus.PENDING);

        CommandResponseDTO response = Start.execute(storageMock, hostId);

        assertEquals(SINGLE_PLAYER_MODE_MSG, response.getMsgThis(),
                "Response should contain massage indicating that cannot start game without opponent");
    }

    @Test
    public void testStartHostNotAllShipsPlaced() {
        when(storageMock.getPlayerStatus(hostId)).thenReturn(PlayerStatus.HOST);
        when(storageMock.getCurrentGame(hostId)).thenReturn(gameMock);
        when(gameMock.getStatus()).thenReturn(GameStatus.IN_PROGRESS);

        when(gameMock.getHostPlayer()).thenReturn(hostPlayerMock);
        when(hostPlayerMock.id()).thenReturn(hostId);

        when(hostPlayerMock.board()).thenReturn(hostPlayerBoardMock);
        when(hostPlayerBoardMock.allShipsPlaced()).thenReturn(false);

        CommandResponseDTO response = Start.execute(storageMock, hostId);

        assertEquals(PLACE_ALL_YOUR_SHIPS, response.getMsgThis(),
                "Response should have message indicating that not all ships have been placed");
    }

    @Test
    public void testStartGuestNotAllShipsPlaced() {
        when(storageMock.getPlayerStatus(guestId)).thenReturn(PlayerStatus.GUEST);
        when(storageMock.getCurrentGame(guestId)).thenReturn(gameMock);
        when(gameMock.getStatus()).thenReturn(GameStatus.IN_PROGRESS);

        when(gameMock.getHostPlayer()).thenReturn(hostPlayerMock);
        when(hostPlayerMock.id()).thenReturn(hostId);

        when(gameMock.getGuestPlayer()).thenReturn(guestPlayerMock);
        when(guestPlayerMock.board()).thenReturn(guestPlayerBoardMock);
        when(guestPlayerBoardMock.allShipsPlaced()).thenReturn(false);

        CommandResponseDTO response = Start.execute(storageMock, guestId);

        assertEquals(PLACE_ALL_YOUR_SHIPS, response.getMsgThis(),
                "Response should have message indicating that not all ships have been placed");
    }

    @Test
    public void testStart() {
        when(storageMock.getPlayerStatus(hostId)).thenReturn(PlayerStatus.HOST);
        when(storageMock.getCurrentGame(hostId)).thenReturn(gameMock);
        when(gameMock.getStatus()).thenReturn(GameStatus.IN_PROGRESS);

        when(gameMock.getHostPlayer()).thenReturn(hostPlayerMock);
        when(hostPlayerMock.id()).thenReturn(hostId);

        when(hostPlayerMock.board()).thenReturn(hostPlayerBoardMock);
        when(hostPlayerBoardMock.allShipsPlaced()).thenReturn(true);

        when(gameMock.getGuestPlayer()).thenReturn(guestPlayerMock);
        when(guestPlayerMock.socketChannel()).thenReturn(guestPlayerSocketChannelMock);

        String hostUsername = "host-username";
        when(hostPlayerMock.username()).thenReturn(hostUsername);

        CommandResponseDTO response = Start.execute(storageMock, hostId);

        assertEquals(THIS_PLAYER_READY, response.getMsgThis(),
                "Response should ready to start battle message for executing command player");
        assertEquals(String.format(OTHER_PLAYER_READY, BLUE, hostUsername, DEFAULT), response.getMsgOther(),
                "Response should ready to start battle message for opponent");
    }

    @Test
    public void testStartGuest() {
        when(storageMock.getPlayerStatus(guestId)).thenReturn(PlayerStatus.GUEST);
        when(storageMock.getCurrentGame(guestId)).thenReturn(gameMock);
        when(gameMock.getStatus()).thenReturn(GameStatus.IN_PROGRESS);

        when(gameMock.getHostPlayer()).thenReturn(hostPlayerMock);
        when(hostPlayerMock.id()).thenReturn(hostId);

        when(gameMock.getGuestPlayer()).thenReturn(guestPlayerMock);
        when(guestPlayerMock.board()).thenReturn(guestPlayerBoardMock);
        when(guestPlayerBoardMock.allShipsPlaced()).thenReturn(true);

        when(hostPlayerMock.socketChannel()).thenReturn(hostPlayerSocketChannelMock);
        String guestUsername = "guest-username";
        when(guestPlayerMock.username()).thenReturn(guestUsername);

        CommandResponseDTO response = Start.execute(storageMock, guestId);

        assertEquals(THIS_PLAYER_READY, response.getMsgThis(),
                "Response should ready to start battle message for executing command player");
        assertEquals(String.format(OTHER_PLAYER_READY, BLUE, guestUsername, DEFAULT), response.getMsgOther(),
                "Response should ready to start battle message for opponent");
    }
}
