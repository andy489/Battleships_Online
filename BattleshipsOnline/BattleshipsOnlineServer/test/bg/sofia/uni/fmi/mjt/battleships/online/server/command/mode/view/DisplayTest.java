package bg.sofia.uni.fmi.mjt.battleships.online.server.command.mode.view;

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

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertSame;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
public class DisplayTest {
    private final String hostId = "host-id";

    @Mock
    private Storage storageMock;

    @Mock
    private Game gameMock;

    @Mock
    private Player hostPlayerMock;

    @Mock
    private Player guestPlayerMock;

    @Mock
    private Board hostPlayerBoardMock;

    @Mock
    private Board guestPlayerBoardMock;

    @Test
    public void testDisplayNotPlaying() {
        when(storageMock.getPlayerStatus(hostId)).thenReturn(PlayerStatus.ONLINE);

        CommandResponseDTO response = Display.execute(storageMock, hostId);

        String NOT_IN_GAME = "No current game";
        assertEquals(NOT_IN_GAME, response.getMsgThis(), "Response should indicate not in a game message");
    }

    @Test
    public void testDisplayMyBoardPendingGame() {
        when(storageMock.getPlayerStatus(hostId)).thenReturn(PlayerStatus.HOST);
        when(storageMock.getCurrentGame(hostId)).thenReturn(gameMock);
        when(gameMock.getStatus()).thenReturn(GameStatus.PENDING);
        when(gameMock.getHostPlayer()).thenReturn(hostPlayerMock);
        when(hostPlayerMock.board()).thenReturn(hostPlayerBoardMock);
        when(hostPlayerBoardMock.displayBoard(true)).thenReturn("my-board");

        CommandResponseDTO response = Display.execute(storageMock, hostId);

        assertEquals("my-board", response.getMsgThis(), "Response should contain my-board only");
        assertSame(null, response.getMsgOther(), "Response should contain null for other player");
    }

    @Test
    public void testDisplayBoardHostPlayer() {
        when(storageMock.getPlayerStatus(hostId)).thenReturn(PlayerStatus.HOST);
        when(storageMock.getCurrentGame(hostId)).thenReturn(gameMock);
        when(gameMock.getStatus()).thenReturn(GameStatus.IN_PROGRESS);

        when(gameMock.getHostPlayer()).thenReturn(hostPlayerMock);
        when(gameMock.getGuestPlayer()).thenReturn(guestPlayerMock);

        when(hostPlayerMock.id()).thenReturn(hostId);

        when(hostPlayerMock.board()).thenReturn(hostPlayerBoardMock);
        when(guestPlayerMock.board()).thenReturn(guestPlayerBoardMock);

        when(hostPlayerBoardMock.displayBoard(true)).thenReturn("my-board");
        when(guestPlayerBoardMock.displayBoard(false)).thenReturn(" other-board");

        CommandResponseDTO response = Display.execute(storageMock, hostId);

        assertEquals("my-board other-board", response.getMsgThis(),
                "Response should contain my-board and other-board only");
    }

    @Test
    public void testDisplayBoardGuestPlayer() {
        String guestId = "guest-id";
        when(storageMock.getPlayerStatus(guestId)).thenReturn(PlayerStatus.GUEST);
        when(storageMock.getCurrentGame(guestId)).thenReturn(gameMock);
        when(gameMock.getStatus()).thenReturn(GameStatus.IN_PROGRESS);

        when(gameMock.getHostPlayer()).thenReturn(hostPlayerMock);
        when(gameMock.getGuestPlayer()).thenReturn(guestPlayerMock);

        when(hostPlayerMock.id()).thenReturn(hostId);

        when(hostPlayerMock.board()).thenReturn(hostPlayerBoardMock);
        when(guestPlayerMock.board()).thenReturn(guestPlayerBoardMock);

        when(hostPlayerBoardMock.displayBoard(false)).thenReturn(" other-board");
        when(guestPlayerBoardMock.displayBoard(true)).thenReturn("my-board");

        CommandResponseDTO response = Display.execute(storageMock, guestId);

        assertEquals("my-board other-board", response.getMsgThis(),
                "Response should contain my-board and other-board only");
    }
}
