package bg.sofia.uni.fmi.mjt.battleships.online.server.game.player;

import bg.sofia.uni.fmi.mjt.battleships.online.server.game.SavedGame;
import bg.sofia.uni.fmi.mjt.battleships.online.server.game.player.board.Board;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertSame;
import static org.junit.jupiter.api.Assertions.assertTrue;

@ExtendWith(MockitoExtension.class)
public class SavedGameTest {
    @Mock
    private Board hostBoardMock;

    @Mock
    private Board guestBoardMock;

    @Test
    public void testSavedGame() {
        String gameName = "my-game";
        SavedGame savedGame = new SavedGame(
                gameName,
                hostBoardMock,
                guestBoardMock,
                true,
                false
        );

        assertTrue(savedGame.getHostTurn(), "Expected host player turn in the saved game");
        assertFalse(savedGame.getWasThisPlayerHost(), "Expected player to be guest in the saved game");
        assertSame(hostBoardMock, savedGame.getHostBoard(),
                "Expected host player board int the saved game to be hostBoardMock");
        assertSame(guestBoardMock, savedGame.getGuestBoard(),
                "Expected host player board in the saved game to be guestPlayerMock");
        assertEquals("my-game", savedGame.getName(), "Expected saved game name to be my-game");
    }
}
