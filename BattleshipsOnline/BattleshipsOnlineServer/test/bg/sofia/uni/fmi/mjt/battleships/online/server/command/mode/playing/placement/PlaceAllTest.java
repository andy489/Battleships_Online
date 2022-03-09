package bg.sofia.uni.fmi.mjt.battleships.online.server.command.mode.playing.placement;

import bg.sofia.uni.fmi.mjt.battleships.online.server.command.response.CommandResponseDTO;
import bg.sofia.uni.fmi.mjt.battleships.online.server.command.response.PlaceResponseDTO;
import bg.sofia.uni.fmi.mjt.battleships.online.server.game.Game;
import bg.sofia.uni.fmi.mjt.battleships.online.server.game.player.Player;
import bg.sofia.uni.fmi.mjt.battleships.online.server.game.player.PlayerStatus;
import bg.sofia.uni.fmi.mjt.battleships.online.server.game.player.board.Board;
import bg.sofia.uni.fmi.mjt.battleships.online.server.storage.Storage;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
public class PlaceAllTest {
    private static final String BLUE = "\u001B[34m";
    private static final String DEFAULT = "\u001B[0m";

    private static final String NOT_IN_GAME = "No current game";
    private static final String ALL_SHIPS_PLACED = "All ships are placed. Type \"%sStart%s\" to start the game";

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
    private Board hostPlayerBoard;

    @Mock
    private Board guestPlayerBoard;

    @Mock
    private PlaceResponseDTO placeResponseDTOMock;

    @Test
    public void testPlaceAllNotPlaying() {
        when(storageMock.getPlayerStatus(hostId)).thenReturn(PlayerStatus.ONLINE);
        CommandResponseDTO response = PlaceAll.execute(storageMock, hostId);

        assertEquals(NOT_IN_GAME, response.getMsgThis(), "Response should contain not in game message");
    }

    @Test
    public void testPlaceAllHostAllShipsPlaced() {
        when(storageMock.getPlayerStatus(hostId)).thenReturn(PlayerStatus.HOST);
        when(storageMock.getCurrentGame(hostId)).thenReturn(gameMock);
        when(gameMock.getHostPlayer()).thenReturn(hostPlayerMock);

        when(hostPlayerMock.id()).thenReturn(hostId);
        when(hostPlayerMock.board()).thenReturn(hostPlayerBoard);
        when(hostPlayerBoard.allShipsPlaced()).thenReturn(true);

        CommandResponseDTO response = PlaceAll.execute(storageMock, hostId);

        assertEquals(String.format(ALL_SHIPS_PLACED, BLUE, DEFAULT), response.getMsgThis(),
                "Response should contain all ships placed message");
    }

    @Test
    public void testPlaceAllGuestAllShipsPlaced() {
        when(storageMock.getPlayerStatus(hostId)).thenReturn(PlayerStatus.HOST);
        when(storageMock.getCurrentGame(hostId)).thenReturn(gameMock);
        when(gameMock.getHostPlayer()).thenReturn(hostPlayerMock);
        when(gameMock.getGuestPlayer()).thenReturn(guestPlayerMock);

        String guestId = "guest-id";
        when(hostPlayerMock.id()).thenReturn(guestId);
        when(guestPlayerMock.board()).thenReturn(guestPlayerBoard);
        when(guestPlayerBoard.allShipsPlaced()).thenReturn(true);

        CommandResponseDTO response = PlaceAll.execute(storageMock, hostId);

        assertEquals(String.format(ALL_SHIPS_PLACED, BLUE, DEFAULT), response.getMsgThis(),
                "Response should contain all ships placed message");
    }

    @Test
    public void testPlaceAll() {
        when(storageMock.getPlayerStatus(hostId)).thenReturn(PlayerStatus.HOST);
        when(storageMock.getCurrentGame(hostId)).thenReturn(gameMock);
        when(gameMock.getHostPlayer()).thenReturn(hostPlayerMock);

        when(hostPlayerMock.id()).thenReturn(hostId);
        when(hostPlayerMock.board()).thenReturn(hostPlayerBoard);
        when(hostPlayerBoard.allShipsPlaced()).thenReturn(false);

        when(hostPlayerBoard.placeAllShipsRandom()).thenReturn(placeResponseDTOMock);
        when(placeResponseDTOMock.getMsg()).thenReturn("");
        when(hostPlayerBoard.displayBoard(true)).thenReturn("");

        CommandResponseDTO response = PlaceAll.execute(storageMock, hostId);

        assertEquals(System.lineSeparator() + String.format(ALL_SHIPS_PLACED, BLUE, DEFAULT),
                response.getMsgThis(),
                "Response should contain all ships placed message");
    }
}
