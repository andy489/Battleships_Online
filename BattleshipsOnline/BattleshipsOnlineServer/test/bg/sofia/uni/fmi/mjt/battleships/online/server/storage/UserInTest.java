package bg.sofia.uni.fmi.mjt.battleships.online.server.storage;

import bg.sofia.uni.fmi.mjt.battleships.online.server.game.Game;
import bg.sofia.uni.fmi.mjt.battleships.online.server.game.player.PlayerStatus;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertSame;

@ExtendWith(MockitoExtension.class)
public class UserInTest {
    private final String username1 = "username1";

    @Mock
    private Game gameMock;

    @Test
    public void testUserInSetUsername() {
        UserIn userIn = new UserIn(username1);
        String username2 = "username2";
        userIn.setUsername(username2);

        assertEquals(username2, userIn.getUsername(), "Expected username to be username2");
    }

    @Test
    public void testUserInSetPlayerStatus() {
        UserIn userIn = new UserIn(username1);
        assertEquals(PlayerStatus.ONLINE, userIn.getStatus(), "User status should be online");
        userIn.setStatus(PlayerStatus.HOST);
        assertEquals(PlayerStatus.HOST, userIn.getStatus(), "User status should be host");
    }

    @Test
    public void testUserInSetApiKey() {
        UserIn userIn = new UserIn(username1);
        assertSame(null, userIn.getApiKey(), "User should not have api key");
        userIn.setApiKey("api-key");
        assertEquals("api-key", userIn.getApiKey(), "Expected user api key to be api-key");
    }

    @Test
    public void testUserInGame() {
        UserIn userIn = new UserIn(username1);
        assertSame(null, userIn.getCurrGame(), "User should not have current game");
        userIn.setCurrGame(gameMock);
        assertSame(gameMock, userIn.getCurrGame(), "User game should be gameMock");
    }

    @Test
    public void testUserInRemoveGame() {
        UserIn userIn = new UserIn(username1);
        userIn.setCurrGame(gameMock);
        userIn.removeCurrentGame();
        assertSame(null, userIn.getCurrGame(), "User should not have current game");
    }
}
