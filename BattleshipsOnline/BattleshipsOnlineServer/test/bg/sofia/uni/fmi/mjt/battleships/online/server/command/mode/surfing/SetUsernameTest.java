package bg.sofia.uni.fmi.mjt.battleships.online.server.command.mode.surfing;

import bg.sofia.uni.fmi.mjt.battleships.online.server.command.response.CommandResponseDTO;
import bg.sofia.uni.fmi.mjt.battleships.online.server.storage.Storage;
import bg.sofia.uni.fmi.mjt.battleships.online.server.storage.UserIn;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.stream.Stream;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
public class SetUsernameTest {
    private static final String SET_USERNAME_USAGE = "Requires one argument. Usage: set-username <username>";
    private static final String ALREADY_SET_USERNAME = "Your username is already set to %s";
    private static final String USERNAME_TAKEN = "Username %s is taken, please select another username";
    private static final String SET_USERNAME_SUCCESS = "Your username is now %s";

    private final String hostId = "host-id";
    private final String username = "username";

    @Mock
    private Storage storageMock;

    @Mock
    private UserIn userInMock;

    @Test
    public void testSetUsernameWithMissingArgument() {
        String[] args = {};
        CommandResponseDTO response = SetUsername.execute(storageMock, hostId, args);

        assertEquals(SET_USERNAME_USAGE, response.getMsgThis(),
                "Response should contain set-username usage message");
    }

    @Test
    public void testSetUsernameUserAlreadySetUsername() {
        when(storageMock.isUserConnected(hostId)).thenReturn(true);
        when(storageMock.getUsername(hostId)).thenReturn(username);
        String[] args = {username};

        CommandResponseDTO response = SetUsername.execute(storageMock, hostId, args);

        assertEquals(String.format(ALREADY_SET_USERNAME, username), response.getMsgThis(),
                "Response should contain already set username message");
    }

    @Test
    public void testSetUsernameAlreadyTakenUsername() {
        when(storageMock.isUserConnected(hostId)).thenReturn(false);
        String[] args = {username};
        when(storageMock.getUsersInAsStream()).thenReturn(Stream.of(userInMock));
        when(userInMock.getUsername()).thenReturn(username);

        CommandResponseDTO response = SetUsername.execute(storageMock, hostId, args);

        assertEquals(String.format(USERNAME_TAKEN, username), response.getMsgThis(),
                "Response should contain username taken message");
    }

    @Test
    public void testSetUsername() {
        when(storageMock.isUserConnected(hostId)).thenReturn(false);
        String[] args = {username};
        when(storageMock.getUsersInAsStream()).thenReturn(Stream.empty());

        CommandResponseDTO response = SetUsername.execute(storageMock, hostId, args);

        assertEquals(String.format(SET_USERNAME_SUCCESS, username), response.getMsgThis(),
                "Response should contain set username success message");
    }
}
