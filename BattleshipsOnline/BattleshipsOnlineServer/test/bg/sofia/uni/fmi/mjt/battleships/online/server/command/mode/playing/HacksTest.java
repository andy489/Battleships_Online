package bg.sofia.uni.fmi.mjt.battleships.online.server.command.mode.playing;

import bg.sofia.uni.fmi.mjt.battleships.online.server.command.response.CommandResponseDTO;
import bg.sofia.uni.fmi.mjt.battleships.online.server.game.player.PlayerStatus;
import bg.sofia.uni.fmi.mjt.battleships.online.server.storage.Storage;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
public class HacksTest {
    private static final String HACK_USAGE = "attack-all <[A-J]> {<[A-J]>}...";
    private static final String FORBIDDEN_COMMAND = "Cannot use hack commands in non-playing mode";

    private final String hostId = "host-id";

    @Mock
    private Storage storageMock;

    @Test
    public void testHacksNotPlaying() {
        when(storageMock.getPlayerStatus(hostId)).thenReturn(PlayerStatus.ONLINE);
        CommandResponseDTO response = Hacks.execute(storageMock, hostId);

        assertEquals(FORBIDDEN_COMMAND, response.getMsgThis(),
                "Response should contain forbidden command message");
    }

    @Test
    public void testHacksPlaying() {
        when(storageMock.getPlayerStatus(hostId)).thenReturn(PlayerStatus.GUEST);
        CommandResponseDTO response = Hacks.execute(storageMock, hostId);

        assertEquals(HACK_USAGE, response.getMsgThis(), "Response should hacks usage message");
    }

}
