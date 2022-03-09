package bg.sofia.uni.fmi.mjt.battleships.online.server.game;

import bg.sofia.uni.fmi.mjt.battleships.online.server.game.player.Player;
import bg.sofia.uni.fmi.mjt.battleships.online.server.game.player.board.Board;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.nio.channels.SocketChannel;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertSame;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
public class GameTest {
    @Mock
    private SocketChannel socketChannelJoinerMock;

    @Mock
    private Board boardJoinerMock;

    @Mock
    private Player playerMock1;

    @Mock
    private Player playerMock2;

    @Test
    public void testCreateGameConstructorWithoutSecondBoard() {
        Game myGame = new Game("my-game", playerMock1);
        assertEquals("my-game", myGame.getGameName(), "Expected myGame name to be my-game");
    }

    @Test
    public void testCreateGameConstructorWithSecondBoard() {
        Game myGame = new Game("my-game", playerMock1, boardJoinerMock);
        myGame.join("test-joiner", "test-id", socketChannelJoinerMock, "test-api-key");

        Player myGameJoiner = myGame.getGuestPlayer();

        assertSame(boardJoinerMock, myGameJoiner.board(),
                "Expected the board of the joiner player to be set to the board from the game constructor");

        assertEquals("test-joiner", myGameJoiner.username(),
                "Expected test-joiner for the name of the joiner player");

        assertEquals("test-id", myGameJoiner.id(),
                "Expected test-id for the id of the joiner player");

        assertEquals("test-api-key", myGameJoiner.apiKey(),
                "Expected test-api-key for the api-key of the joiner player");
    }

    @Test
    public void testNewGameStatus() {
        Game myGame = new Game("new-game", playerMock1);
        assertSame(GameStatus.PENDING, myGame.getStatus(),
                "Expected game status of newly created game to be \"pending\"");
    }

    @Test
    public void testSetGameStatus() {
        Game myGame = new Game("my-game", playerMock1);
        myGame.setStatus(GameStatus.IN_PROGRESS);

        assertSame(GameStatus.IN_PROGRESS, myGame.getStatus(),
                "Expected game status of myGame to be set to \"in progress\"");
    }

    @Test
    public void testNewGameCreator() {
        Game myGame = new Game("my-game", playerMock1);
        assertSame(playerMock1, myGame.getHostPlayer(), "Expected host player of myGame to be player1");
    }

    @Test
    public void testNewGamePlayers() {
        Game myGame = new Game("my-game", playerMock1);
        myGame.setGuestPlayer(playerMock2);

        assertSame(myGame.getHostPlayer(), playerMock1, "Expected host player of myGame to be player1");
        assertSame(myGame.getGuestPlayer(), playerMock2, "Expected guest player of myGame to be player2");
    }

    @Test
    public void testNewGameTurn() {
        Game myGame = new Game("my-game", playerMock1);
        assertTrue(myGame.getHostTurn(), "Expected first attack in newly created game to be from host player");
    }

    @Test
    public void testFlipGameTurn() {
        Game myGame = new Game("my-game", playerMock1);
        myGame.flipTurn();
        assertFalse(myGame.getHostTurn(), "Expected next attack to be from guest player");
    }

    @Test
    public void testNewGameNumOfPlayers() {
        Game myGame = new Game("my-game", playerMock1);
        assertEquals(1, myGame.numPlayers(), "Expected newly created game to have only one player");

        myGame.join("test-joiner", "test-id", socketChannelJoinerMock, "test-api-key");
        assertEquals(2, myGame.numPlayers(), "Expected myGame to have two players");
    }

    @Test
    public void testGameCheckReadyToStart() {
        Game myGame = new Game("my-game", playerMock1);
        assertTrue(myGame.notReadyToStart());

        myGame.setHostReady();
        assertTrue(myGame.notReadyToStart());

        myGame.setGuestReady();
        assertFalse(myGame.notReadyToStart());
    }

    @Test
    public void testGetHostPlayerName() {
        Game myGame = new Game("my-game", playerMock1);

        when(playerMock1.username()).thenReturn("player1");
        when(playerMock2.username()).thenReturn("player2");

        assertEquals(playerMock1.username(), myGame.getHostName(), "Expects username of creator to be player1");

        myGame.setHostPlayer(playerMock2);
        assertEquals(playerMock2.username(), myGame.getHostName(), "Expects username of creator to be player2");
    }
}
