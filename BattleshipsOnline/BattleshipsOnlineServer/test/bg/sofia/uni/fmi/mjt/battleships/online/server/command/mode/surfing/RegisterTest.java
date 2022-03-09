package bg.sofia.uni.fmi.mjt.battleships.online.server.command.mode.surfing;

import bg.sofia.uni.fmi.mjt.battleships.online.server.command.response.CommandResponseDTO;
import bg.sofia.uni.fmi.mjt.battleships.online.server.storage.Storage;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
public class RegisterTest {
    private static final String ALREADY_LOGGED = "You are already logged in";
    private static final String REGISTER_SUCCESS = "You are now logged in with api-key: %s%s%s";

    private final String hostId = "host-id";

    @Mock
    private Storage storageMock;

    @Test
    public void testRegisterWhenAlreadyLoggedIn() {
        String hostApiKey = "489489489";
        when(storageMock.getApiKey(hostId)).thenReturn(hostApiKey);

        CommandResponseDTO response = Register.execute(storageMock, hostId);

        assertEquals(ALREADY_LOGGED, response.getMsgThis(), "Response should contain already logged message");
    }

    @Test
    public void testRegister() {
        doNothing().when(storageMock).setApiKey(any(String.class), any(String.class));
        doNothing().when(storageMock).initSavedGamesCollection(any(String.class));

        CommandResponseDTO response = Register.execute(storageMock, hostId);

        assertTrue(response.getMsgThis().startsWith(REGISTER_SUCCESS.substring(0, 36)),
                "Response should contain successful login message");
    }
}


