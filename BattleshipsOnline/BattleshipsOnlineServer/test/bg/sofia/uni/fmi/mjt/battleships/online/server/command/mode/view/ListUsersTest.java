package bg.sofia.uni.fmi.mjt.battleships.online.server.command.mode.view;

import bg.sofia.uni.fmi.mjt.battleships.online.server.command.response.CommandResponseDTO;
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
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
public class ListUsersTest {
    private static final String TOTAL_USERS_MSG = "Total users connected now: ";

    @Mock
    private Storage storageMock;

    @Mock
    private UserIn userInMock;

    @Test
    public void testListUsers() {
        when(storageMock.getUsersInAsStream()).thenReturn(Stream.of(userInMock));
        when(userInMock.getUsername()).thenReturn("username");
        when(userInMock.getStatus()).thenReturn(PlayerStatus.HOST);

        CommandResponseDTO response = ListUsers.execute(storageMock);

        String expected = TOTAL_USERS_MSG + RED + "1" + DEFAULT + System.lineSeparator() +
                RED + "> " + DEFAULT + "username is " + PlayerStatus.HOST;

        assertEquals(expected, response.getMsgThis(),
                "Response should contain list of users in server now");
    }
}
