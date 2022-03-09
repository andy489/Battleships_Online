package bg.sofia.uni.fmi.mjt.battleships.online.server.command.mode.view;

import bg.sofia.uni.fmi.mjt.battleships.online.server.command.response.CommandResponseDTO;
import bg.sofia.uni.fmi.mjt.battleships.online.server.game.Game;
import bg.sofia.uni.fmi.mjt.battleships.online.server.game.GameStatus;
import bg.sofia.uni.fmi.mjt.battleships.online.server.game.player.PlayerStatus;
import bg.sofia.uni.fmi.mjt.battleships.online.server.storage.Storage;
import bg.sofia.uni.fmi.mjt.battleships.online.server.storage.UserIn;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.stream.Stream;

import static bg.sofia.uni.fmi.mjt.battleships.online.server.command.manager.CommandExecutor.DEFAULT;
import static bg.sofia.uni.fmi.mjt.battleships.online.server.command.manager.CommandExecutor.RED;
import static bg.sofia.uni.fmi.mjt.battleships.online.server.command.manager.CommandExecutor.RED_SEP;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
public class ListGamesTest {
    @Mock
    private Storage storageMock;

    @Mock
    private UserIn userInMock;

    @Mock
    private Game gameMock;

    @Test
    public void testAttackNotTwoArguments() {
        when(storageMock.getUsersInAsStream()).thenReturn(Stream.of(userInMock));
        when(userInMock.getCurrGame()).thenReturn(gameMock);
        when(userInMock.getStatus()).thenReturn(PlayerStatus.HOST);

        when(gameMock.getGameName()).thenReturn("my-game");
        when(gameMock.getHostName()).thenReturn("host-name");
        when(gameMock.getStatus()).thenReturn(GameStatus.IN_PROGRESS);
        when(gameMock.numPlayers()).thenReturn(2);

        CommandResponseDTO response = ListGames.execute(storageMock);

        String expected = RED_SEP + " NAME                       " + RED_SEP + " CREATOR     " + RED_SEP +
                " STATUS      " + RED_SEP + " PLAYERS     " + RED_SEP + System.lineSeparator() + RED + "> |" + DEFAULT +
                "----------------------------" + "+" + "-------------" + "+" + "-------------" + "+" +
                "-------------" + RED_SEP + System.lineSeparator() + RED + "> |" + DEFAULT + " " +
                "my-game                    " + RED_SEP + " " + "host-name   " + RED_SEP + " " + RED + "in" + DEFAULT +
                " progress " + RED_SEP + " 2/2         " + RED_SEP;

        assertEquals(expected, response.getMsgThis(),
                "Response should contain list of current games in server message");
    }
}
