package bg.sofia.uni.fmi.mjt.battleships.online.server.command.mode.view;

import bg.sofia.uni.fmi.mjt.battleships.online.server.command.response.CommandResponseDTO;
import bg.sofia.uni.fmi.mjt.battleships.online.server.storage.Storage;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
public class WhoAmITest {
    private static final String WHO_AM_I = "You are %s";

    @Mock
    private Storage storageMock;

    @Test
    public void testWhoAmI() {
        String hostId = "hostId";
        String username = "username";

        when(storageMock.getUsername(hostId)).thenReturn(username);

        CommandResponseDTO response = WhoAmI.execute(storageMock, hostId);

        assertEquals(String.format(WHO_AM_I, username), response.getMsgThis(), "Response should have who am I message");
    }
}
