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
public class PlaceTest {
    private static final String BLUE = "\u001B[34m";
    private static final String DEFAULT = "\u001B[0m";

    private static final String PLACE_USAGE =
            "Not enough arguments. Usage: place <[A-J]> <[1-10]> <[UP, RIGHT, LEFT, DOWN]> <[2-5]>";
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
    public void testPlaceNotEnoughArguments() {
        CommandResponseDTO response = Place.execute(storageMock, hostId, "A");

        assertEquals(PLACE_USAGE, response.getMsgThis(), "Response should contain place usage message");
    }

    @Test
    public void testPlaceWhenNotInGame() {
        when(storageMock.getPlayerStatus(hostId)).thenReturn(PlayerStatus.ONLINE);
        CommandResponseDTO response = Place.execute(storageMock, hostId, "E", "4", "RIGHT", "3");

        assertEquals(NOT_IN_GAME, response.getMsgThis(), "Response should contain not in game message");
    }

    @Test
    public void testPlaceHostAllShipsPlaced() {
        when(storageMock.getPlayerStatus(hostId)).thenReturn(PlayerStatus.HOST);
        when(storageMock.getCurrentGame(hostId)).thenReturn(gameMock);
        when(gameMock.getHostPlayer()).thenReturn(hostPlayerMock);

        when(hostPlayerMock.id()).thenReturn(hostId);
        when(hostPlayerMock.board()).thenReturn(hostPlayerBoard);
        when(hostPlayerBoard.allShipsPlaced()).thenReturn(true);

        CommandResponseDTO response = Place.execute(storageMock, hostId, "A", "3", "DOWN", "2");

        assertEquals(String.format(ALL_SHIPS_PLACED, BLUE, DEFAULT), response.getMsgThis(),
                "Response should contain all ships placed message");
    }

    @Test
    public void testPlaceGuestAllShipsPlaced() {
        when(storageMock.getPlayerStatus(hostId)).thenReturn(PlayerStatus.HOST);
        when(storageMock.getCurrentGame(hostId)).thenReturn(gameMock);
        when(gameMock.getHostPlayer()).thenReturn(hostPlayerMock);
        when(gameMock.getGuestPlayer()).thenReturn(guestPlayerMock);

        String guestId = "guest-id";
        when(hostPlayerMock.id()).thenReturn(guestId);
        when(guestPlayerMock.board()).thenReturn(guestPlayerBoard);
        when(guestPlayerBoard.allShipsPlaced()).thenReturn(true);

        CommandResponseDTO response = Place.execute(storageMock, hostId, "A", "3", "DOWN", "2");

        assertEquals(String.format(ALL_SHIPS_PLACED, BLUE, DEFAULT), response.getMsgThis(),
                "Response should contain all ships placed message");
    }

    @Test
    public void testPlace() {
        when(storageMock.getPlayerStatus(hostId)).thenReturn(PlayerStatus.HOST);
        when(storageMock.getCurrentGame(hostId)).thenReturn(gameMock);
        when(gameMock.getHostPlayer()).thenReturn(hostPlayerMock);

        when(hostPlayerMock.id()).thenReturn(hostId);
        when(hostPlayerMock.board()).thenReturn(hostPlayerBoard);
        when(hostPlayerBoard.allShipsPlaced()).thenReturn(false);

        String coordinatesOfShip = "A 3 DOWN 2";
        when(hostPlayerBoard.placeShip(coordinatesOfShip)).thenReturn(placeResponseDTOMock);
        when(placeResponseDTOMock.getMsg()).thenReturn(" some message");
        when(hostPlayerBoard.displayBoard(true)).thenReturn("my-board");

        CommandResponseDTO response = Place.execute(storageMock, hostId, "A", "3", "DOWN", "2");

        assertEquals("my-board some message", response.getMsgThis(),
                "Response should contain all ships placed message");
    }
}
