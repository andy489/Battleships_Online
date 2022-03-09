package bg.sofia.uni.fmi.mjt.battleships.online.server.storage;

import bg.sofia.uni.fmi.mjt.battleships.online.server.game.Game;
import bg.sofia.uni.fmi.mjt.battleships.online.server.game.SavedGame;
import bg.sofia.uni.fmi.mjt.battleships.online.server.game.player.PlayerStatus;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.HashMap;
import java.util.Map;
import java.util.stream.Stream;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertSame;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.Mockito.lenient;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
public class StorageTest {
    private static final String id1 = "id1";
    private static final String id2 = "id2";

    private static final String apiKey1 = "api-key1";
    private static final String apiKey2 = "api-key2";

    private static final String username1 = "username1";
    private static final String username2 = "username2";

    private static final String gameName1 = "my-game1";
    private static final String gameName2 = "my-game2";

    private static final String newApiKey = "new-api-key";
    private static final String newUsername = "new-username";

    private static UserIn userInMock1;
    private static UserIn userInMock2;

    private static Game gameMock1;
    private static Game gameMock2;

    private static SavedGame savedGameMock1;

    private static Storage storage;

    @BeforeAll
    public static void setUp() {
        userInMock1 = mock(UserIn.class);
        userInMock2 = mock(UserIn.class);

        gameMock1 = mock(Game.class);
        gameMock2 = mock(Game.class);

        savedGameMock1 = mock(SavedGame.class);

        storage = new Storage();

        storage.addNewUserIn(id1, userInMock1);
        storage.addNewUserIn(id2, userInMock2);

        lenient().when(userInMock1.getUsername()).thenReturn(username1);
        lenient().when(userInMock1.getStatus()).thenReturn(PlayerStatus.HOST);
        lenient().when(userInMock1.getApiKey()).thenReturn(apiKey1);
        lenient().when(userInMock1.getCurrGame()).thenReturn(gameMock1);

        lenient().when(userInMock2.getUsername()).thenReturn(username2);
        lenient().when(userInMock2.getStatus()).thenReturn(PlayerStatus.ONLINE);
        lenient().when(userInMock2.getApiKey()).thenReturn(apiKey2);
        lenient().when(userInMock2.getCurrGame()).thenReturn(gameMock2);

        lenient().when(gameMock1.getGameName()).thenReturn(gameName1);
        lenient().when(gameMock2.getGameName()).thenReturn(gameName2);

        lenient().when(savedGameMock1.getName()).thenReturn(gameName1);

        Map<String, SavedGame> gameNameSavedGame = new HashMap<>();
        gameNameSavedGame.put(gameName1, savedGameMock1);

        storage.getApiKeySavedGames().put(apiKey1, gameNameSavedGame);
    }

    @Test
    public void testStorageGetPlayerStatus() {
        assertSame(PlayerStatus.HOST, storage.getPlayerStatus(id1),
                "Expected user with id \"id1\" to be with status \"host\"");
    }

    @Test
    public void testStorageGetCurrentGame() {
        assertSame(gameMock1, storage.getCurrentGame(id1),
                "Expected user with id \"id1\" to be with current game \"gameMock1\"");
    }

    @Test
    public void testStorageGetApiKey() {
        assertEquals(apiKey1, storage.getApiKey(id1),
                "Expected user with id \"id1\" to be \"api-key1\"");
    }

    @Test
    public void testStorageGetUsername() {
        assertEquals(username1, storage.getUsername(id1),
                "Expected user with id \"id1\" to be with username \"username1\"");
    }

    @Test
    public void testStorageGetUserIn() {
        assertSame(userInMock1, storage.getUserIn(id1),
                "Expected user with id \"id1\" to be \"userInMock1\"");
    }

    @Test
    public void testStorageGetUsersInAsStream() {
        assertTrue(Stream.of(userInMock1, userInMock2).toList().containsAll(storage.getUsersInAsStream().toList()),
                "Expected users in server to be \"userInMock1\" and \"userInMock2\"");

        assertTrue(storage.getUsersInAsStream().toList().containsAll(Stream.of(userInMock1, userInMock2).toList()),
                "Expected users in server to be only \"userInMock1\" and \"userInMock2\"");
    }

    @Test
    public void testStorageGetApiKeySavedGame() {
        assertSame(savedGameMock1, storage.getApiKeySavedGames().get(apiKey1).get(gameName1),
                "Expected user with api key \"api-key1\" to have as saved game \"my-game1\"");
    }

    @Test
    public void testStorageSetPlayerStatus() {
        storage.setPlayerStatus(id1, PlayerStatus.GUEST);
        when(userInMock1.getStatus()).thenReturn(PlayerStatus.GUEST);

        assertSame(PlayerStatus.GUEST, storage.getPlayerStatus(id1),
                "Expected user with id \"id1\" to be with status \"guest\" instead of \"online\"");

        when(userInMock1.getStatus()).thenReturn(PlayerStatus.HOST);
    }

    @Test
    public void testStorageIsUserConnected() {
        assertTrue(storage.isUserConnected(id1), "Expected user with id \"id1\" to be connected to server");
        assertTrue(storage.isUserConnected(id2), "Expected user with id \"id2\" to be connected to server");
    }

    @Test
    public void testStorageAddNewUserIn() {
        storage.removeUserIn(id1);
        assertFalse(storage.isUserConnected(id1), "Expected user with \"id1\" not to be connected to server");

        storage.addNewUserIn(id1, userInMock1);
        assertTrue(storage.isUserConnected(id1), "Expected user with \"id1\" to be connected to server");
    }

    @Test
    public void testStorageChangeUsername() {
        storage.changeUsername(id1, newUsername);
        when(userInMock1.getUsername()).thenReturn(newUsername);

        assertEquals("new-username", storage.getUsername(id1),
                "Expected user with id \"id2\" to have username \"new-username\"");

        when(userInMock1.getUsername()).thenReturn(username1);
    }

    @Test
    public void testStorageSetApiKey() {
        storage.setApiKey(id1, newApiKey);
        when(userInMock1.getApiKey()).thenReturn(newApiKey);

        assertEquals(newApiKey, storage.getApiKey(id1),
                "Expected user with id \"id1\" to have api key \"new-api-key\"");

        when(userInMock1.getApiKey()).thenReturn(apiKey1);
    }

    @Test
    public void testStorageContainsGameApiKey() {
        assertFalse(storage.containsSavedGamesForApiKey(newApiKey),
                "Api key \"new-api-key\" should not exist in server data");

        storage.initSavedGamesCollection(newApiKey);

        assertTrue(storage.containsSavedGamesForApiKey(newApiKey),
                "Api key \"new-api-key\" should be in server data");
    }

    @Test
    public void testStorageCheckForExistingGame() {
        assertTrue(storage.checkForExistingGameName(gameName1),
                "Expected game with name \"my-game1\" to be present in server played games");

        assertTrue(storage.checkForExistingGameName(gameName2),
                "Expected game with name \"my-game2\" to be present in server played games");
    }

    @Test
    public void testStorageSetCurrentGame() {
        assertSame(gameMock1, storage.getCurrentGame(id1),
                "Expected user with id \"id1\" to have current game \"gameMock1\"");

        storage.setCurrentGame(id1, gameMock2);
        when(userInMock1.getCurrGame()).thenReturn(gameMock2);

        assertSame(gameMock2, storage.getCurrentGame(id1),
                "Expected user with id \"id1\" to have current game \"gameMock2\"");
        when(userInMock1.getCurrGame()).thenReturn(gameMock1);
    }

    @Test
    public void testStorageRemoveUserIn() {
        assertTrue(storage.isUserConnected(id2), "Expected user with id \"id2\" to be connected to server");

        storage.removeUserIn(id2);

        assertFalse(storage.isUserConnected(id2),
                "Expected user with id \"id2\" not to be connected to server");

        storage.addNewUserIn(id2, userInMock1);
    }

    @Test
    public void testStorageGetSavedGamesAsStream() {
        assertEquals(1, storage.getSavedGamesAsStream(apiKey1).toList().size(),
                "Expected user with api key \"api-key1\" to have only one saved game");

        assertEquals(savedGameMock1, storage.getSavedGamesAsStream(apiKey1).toList().get(0),
                "Expected user with api key \"api-key1\" to have as saved game only \"savedGameMock1\"");
    }

    @Test
    public void testStorageGetSavedGame() {
        assertEquals(savedGameMock1, storage.getSavedGame(apiKey1, gameName1),
                "Expected user with api key \"api-key1\" to \"my-game1\" as saved game \"savedGameMock1\"");
    }

    @Test
    public void testStorageRemoveSavedGame() {
        assertEquals(savedGameMock1, storage.getSavedGame(apiKey1, gameName1),
                "Expected user with api key \"api-key1\" to \"my-game1\" as saved game \"savedGameMock1\"");


        storage.removeSavedGame(apiKey1, gameName1);

        assertSame(null, storage.getSavedGame(apiKey1, gameName1),
                "Expected user with api key \"api-key1\" not to  have saved game \"my-game1\"");

        storage.addSavedGame(apiKey1, savedGameMock1);
    }

    @Test
    public void testStorageAddSavedGame() {
        storage.removeSavedGame(apiKey1, gameName1);
        assertSame(null, storage.getSavedGame(apiKey1, gameName1),
                "Expected user with api key \"api-key1\" not to have saved games");

      storage.addSavedGame(apiKey1, savedGameMock1);

        assertSame(savedGameMock1, storage.getSavedGame(apiKey1, gameName1),
                "Expected user with api key \"api-key1\" to have saved game \"my-game1\"");
    }

    @Test
    public void testStorageGetGamesToSave() {
        assertSame(savedGameMock1, storage.getGamesToSave(apiKey1).get(gameName1),
                "Expected user with api key \"api-key1\" to have saved game \"my-game1\"");
    }

    @Test
    public void testStorageContainsGameName() {
        assertTrue(storage.containsGameName(apiKey1, gameName1),
                "Expected user with api key \"api-key1\" to have saved game \"my-game1\"");
    }

    @Test
    public void testStorageInitSavedGameAndRegisteredUsers() {
        storage.removeSavedGame(apiKey1, gameName1);
        assertSame(null, storage.getSavedGame(apiKey1, gameName1),
                "Expected user with api key \"api-key1\" not to have saved games");


        Map<String, SavedGame> gameNameSavedGameStub = new HashMap<>();
        gameNameSavedGameStub.put(gameName1, savedGameMock1);
        storage.initSavedGamesAndRegisteredUsers(apiKey1, gameNameSavedGameStub);

        assertSame(savedGameMock1, storage.getSavedGame(apiKey1, gameName1),
                "Expected user with api key \"api-key1\" to have \"my-game1\" as saved game");
    }
}
