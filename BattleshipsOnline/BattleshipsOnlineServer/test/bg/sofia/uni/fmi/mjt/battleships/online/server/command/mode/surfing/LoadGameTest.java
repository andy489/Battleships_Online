package bg.sofia.uni.fmi.mjt.battleships.online.server.command.mode.surfing;

import bg.sofia.uni.fmi.mjt.battleships.online.server.command.response.CommandResponseDTO;
import bg.sofia.uni.fmi.mjt.battleships.online.server.game.SavedGame;
import bg.sofia.uni.fmi.mjt.battleships.online.server.game.player.PlayerStatus;
import bg.sofia.uni.fmi.mjt.battleships.online.server.game.player.board.Board;
import bg.sofia.uni.fmi.mjt.battleships.online.server.game.player.board.field.Field;
import bg.sofia.uni.fmi.mjt.battleships.online.server.game.player.board.field.ship.Ship;
import bg.sofia.uni.fmi.mjt.battleships.online.server.game.player.board.field.ship.coordinate.Coordinate;
import bg.sofia.uni.fmi.mjt.battleships.online.server.storage.Storage;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.nio.channels.SocketChannel;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static bg.sofia.uni.fmi.mjt.battleships.online.server.game.player.board.BoardConstants.BOARD_SIZE;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
public class LoadGameTest {
    private static final String LOAD_GAME_USAGE = "Requires one argument. Usage: load-game <game-name>";
    private static final String ALREADY_IN_A_GAME = "You are already in a game";
    private static final String NO_SUCH_GAME = "There is no such game";
    private static final String GAME_ALREADY_EXISTS = "Game with that name already exists. Choose another game-name";
    private static final String LOGIN_OR_REGISTER = "You must login or register first";
    private static final String GAME_LOADED = "Loaded game %s";

    private final String hostId = "host-id";
    private final String hostApiKey = "host-api-key";
    private final String gameName = "my-game";

    @Mock
    private Storage storageMock;

    @Mock
    private SocketChannel guestPlayerSocketChannelMock;

    @Mock
    private Board hostPlayerBoardMock;

    @Mock
    private Board guestPlayerBoardMock;

    @Mock
    private SavedGame savedGameMock;

    @Mock
    private Ship shipMock;

    @Test
    public void testLoadGameNoArgument() {
        String[] args = {};
        CommandResponseDTO response = LoadGame.execute(storageMock, hostId, guestPlayerSocketChannelMock, args);

        assertEquals(LOAD_GAME_USAGE, response.getMsgThis(),
                "Response should contain load-game usage message");
    }

    @Test
    public void testLoadGameNotLoggedIn() {
        String[] args = {gameName};
        CommandResponseDTO response = LoadGame.execute(storageMock, hostId, guestPlayerSocketChannelMock, args);

        assertEquals(LOGIN_OR_REGISTER, response.getMsgThis(),
                "Response should contain not logged in message");
    }

    @Test
    public void testLoadGameAlreadyPlaying() {
        when(storageMock.getApiKey(hostId)).thenReturn(hostApiKey);
        when(storageMock.getPlayerStatus(hostId)).thenReturn(PlayerStatus.HOST);

        String[] args = {gameName};
        CommandResponseDTO response = LoadGame.execute(storageMock, hostId, guestPlayerSocketChannelMock, args);

        assertEquals(ALREADY_IN_A_GAME, response.getMsgThis(),
                "Response should contain already in a game message");
    }

    @Test
    public void testLoadGameNoSuchGame() {
        when(storageMock.getApiKey(hostId)).thenReturn(hostApiKey);
        when(storageMock.getPlayerStatus(hostId)).thenReturn(PlayerStatus.ONLINE);
        when(storageMock.containsGameName(hostApiKey, gameName)).thenReturn(false);

        String[] args = {gameName};
        CommandResponseDTO response = LoadGame.execute(storageMock, hostId, guestPlayerSocketChannelMock, args);

        assertEquals(NO_SUCH_GAME, response.getMsgThis(),
                "Response should contain no such game message");
    }

    @Test
    public void testLoadGameGameWithThatNameAlreadyExist() {
        when(storageMock.getApiKey(hostId)).thenReturn(hostApiKey);
        when(storageMock.getPlayerStatus(hostId)).thenReturn(PlayerStatus.ONLINE);
        when(storageMock.containsGameName(hostApiKey, gameName)).thenReturn(true);
        when(storageMock.checkForExistingGameName(gameName)).thenReturn(true);

        String[] args = {gameName};
        CommandResponseDTO response = LoadGame.execute(storageMock, hostId, guestPlayerSocketChannelMock, args);

        assertEquals(GAME_ALREADY_EXISTS, response.getMsgThis(),
                "Response should contain game with that name is already playing (exists) message");
    }

    @Test
    public void testLoadGameGuestPlayerNotGuestPlayerTurn() {
        when(storageMock.getApiKey(hostId)).thenReturn(hostApiKey);
        when(storageMock.getPlayerStatus(hostId)).thenReturn(PlayerStatus.ONLINE);
        when(storageMock.containsGameName(hostApiKey, gameName)).thenReturn(true);
        when(storageMock.checkForExistingGameName(gameName)).thenReturn(false);

        when(storageMock.getSavedGame(hostApiKey, gameName)).thenReturn(savedGameMock);

        when(savedGameMock.getHostBoard()).thenReturn(hostPlayerBoardMock);
        when(savedGameMock.getGuestBoard()).thenReturn(guestPlayerBoardMock);

        when(savedGameMock.getWasThisPlayerHost()).thenReturn(false);
        when(savedGameMock.getHostTurn()).thenReturn(true);

        Map<Integer, List<Ship>> shipsStub = new HashMap<>();
        shipsStub.put(2, List.of(shipMock));

        Coordinate coordinateStub = new Coordinate(3, 3);

        List<Coordinate> coordinatesStub = new ArrayList<>();
        coordinatesStub.add(coordinateStub);

        when(shipMock.getLocation()).thenReturn(coordinatesStub);

        when(hostPlayerBoardMock.getAllShips()).thenReturn(shipsStub);
        when(guestPlayerBoardMock.getAllShips()).thenReturn(shipsStub);

        Field[][] fieldsStub = new Field[BOARD_SIZE][BOARD_SIZE];

        for (int i = 0; i < BOARD_SIZE; i++) {
            for (int j = 0; j < BOARD_SIZE; j++) {
                fieldsStub[i][j] = new Field();
            }
        }

        when(hostPlayerBoardMock.getBoard()).thenReturn(fieldsStub);
        when(guestPlayerBoardMock.getBoard()).thenReturn(fieldsStub);

        String[] args = {gameName};
        CommandResponseDTO response = LoadGame.execute(storageMock, hostId, guestPlayerSocketChannelMock, args);

        assertEquals(String.format(GAME_LOADED, gameName), response.getMsgThis(),
                "Response should contain successful loading of a game message");
    }

    @Test
    public void testLoadGameHostPlayerNotHostPlayerTurn() {
        when(storageMock.getApiKey(hostId)).thenReturn(hostApiKey);
        when(storageMock.getPlayerStatus(hostId)).thenReturn(PlayerStatus.ONLINE);
        when(storageMock.containsGameName(hostApiKey, gameName)).thenReturn(true);
        when(storageMock.checkForExistingGameName(gameName)).thenReturn(false);

        when(storageMock.getSavedGame(hostApiKey, gameName)).thenReturn(savedGameMock);

        when(savedGameMock.getHostBoard()).thenReturn(hostPlayerBoardMock);
        when(savedGameMock.getGuestBoard()).thenReturn(guestPlayerBoardMock);

        when(savedGameMock.getWasThisPlayerHost()).thenReturn(true);
        when(savedGameMock.getHostTurn()).thenReturn(false);

        Map<Integer, List<Ship>> shipsStub = new HashMap<>();
        shipsStub.put(2, List.of(shipMock));

        Coordinate coordinateStub = new Coordinate(3, 3);

        List<Coordinate> coordinatesStub = new ArrayList<>();
        coordinatesStub.add(coordinateStub);

        when(shipMock.getLocation()).thenReturn(coordinatesStub);

        when(hostPlayerBoardMock.getAllShips()).thenReturn(shipsStub);
        when(guestPlayerBoardMock.getAllShips()).thenReturn(shipsStub);

        Field[][] fieldsStub = new Field[BOARD_SIZE][BOARD_SIZE];

        for (int i = 0; i < BOARD_SIZE; i++) {
            for (int j = 0; j < BOARD_SIZE; j++) {
                fieldsStub[i][j] = new Field();
            }
        }

        when(hostPlayerBoardMock.getBoard()).thenReturn(fieldsStub);
        when(guestPlayerBoardMock.getBoard()).thenReturn(fieldsStub);

        String[] args = {gameName};
        CommandResponseDTO response = LoadGame.execute(storageMock, hostId, guestPlayerSocketChannelMock, args);

        assertEquals(String.format(GAME_LOADED, gameName), response.getMsgThis(),
                "Response should contain successful loading of a game message");
    }
}
