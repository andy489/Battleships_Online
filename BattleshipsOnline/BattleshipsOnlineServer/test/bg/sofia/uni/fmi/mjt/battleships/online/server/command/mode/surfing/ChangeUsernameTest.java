package bg.sofia.uni.fmi.mjt.battleships.online.server.command.mode.surfing;

import bg.sofia.uni.fmi.mjt.battleships.online.server.command.response.CommandResponseDTO;
import bg.sofia.uni.fmi.mjt.battleships.online.server.game.player.PlayerStatus;
import bg.sofia.uni.fmi.mjt.battleships.online.server.storage.Storage;
import bg.sofia.uni.fmi.mjt.battleships.online.server.storage.UserIn;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.stream.Stream;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
public class ChangeUsernameTest {
    private static final String CHANGE_USERNAME_USAGE = "Requires one argument. Usage: set-username <username>";
    private static final String CHANGE_USERNAME_WHILE_PLAYING = "Cannot change username while you are playing";
    private static final String USERNAME_TAKEN_MSG = "Username %s is taken, please select another username";
    private static final String ALREADY_SET_USERNAME = "Your username is already set to %s";
    private static final String CHANGE_USERNAME_SUCCESS = "Username changed. Your username is now %s";

    private final String hostId = "host-id";

    @Mock
    private Storage storageMock;

    @Test
    public void testChangeNameMissingNewName() {
        String[] args = {};
        CommandResponseDTO response = ChangeUsername.execute(storageMock, hostId, args);

        assertEquals(CHANGE_USERNAME_USAGE, response.getMsgThis(),
                "Response should contain change-username command usage message");
    }

    @Test
    public void testChangeNameWhilePlaying() {
        when(storageMock.getPlayerStatus(hostId)).thenReturn(PlayerStatus.HOST);
        CommandResponseDTO response = ChangeUsername.execute(storageMock, hostId, "new-name");

        assertEquals(CHANGE_USERNAME_WHILE_PLAYING, response.getMsgThis(),
                "Response should contain forbidden change-name while playing message");
    }

    @Test
    public void testChangeNameWithSameName() {
        when(storageMock.getPlayerStatus(hostId)).thenReturn(PlayerStatus.ONLINE);
        when(storageMock.getUsername(hostId)).thenReturn("new-name");

        CommandResponseDTO response = ChangeUsername.execute(storageMock, hostId, "new-name");

        assertEquals(String.format(ALREADY_SET_USERNAME, "new-name"), response.getMsgThis(),
                "Response should contain message indicating already set same username");
    }

    @Test
    public void testChangeNameAlreadyExistingUsername() {
        when(storageMock.getPlayerStatus(hostId)).thenReturn(PlayerStatus.ONLINE);
        when(storageMock.getUsername(hostId)).thenReturn("some-other-username");
        when(storageMock.getUsersInAsStream()).thenReturn(Stream.of(new UserIn("new-name")));

        CommandResponseDTO response = ChangeUsername.execute(storageMock, hostId, "new-name");

        assertEquals(String.format(USERNAME_TAKEN_MSG, "new-name"), response.getMsgThis(),
                "Response should contain already taken username message");
    }

    @Test
    public void testChangeName() {
        when(storageMock.getPlayerStatus(hostId)).thenReturn(PlayerStatus.ONLINE);
        when(storageMock.getUsername(hostId)).thenReturn("some-other-username");
        when(storageMock.getUsersInAsStream()).thenReturn(Stream.of());

        CommandResponseDTO response = ChangeUsername.execute(storageMock, hostId, "new-name");

        assertEquals(String.format(CHANGE_USERNAME_SUCCESS, "new-name"), response.getMsgThis(),
                "Response should contain successful username message");
    }
}
