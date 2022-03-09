package bg.sofia.uni.fmi.mjt.battleships.online.server.command.manager;

import bg.sofia.uni.fmi.mjt.battleships.online.server.storage.Storage;
import com.google.gson.Gson;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.net.Socket;
import java.net.SocketAddress;
import java.nio.channels.SocketChannel;

import static bg.sofia.uni.fmi.mjt.battleships.online.server.command.manager.CommandExecutor.BLUE;
import static bg.sofia.uni.fmi.mjt.battleships.online.server.command.manager.CommandExecutor.DEFAULT;
import static bg.sofia.uni.fmi.mjt.battleships.online.server.command.manager.CommandExecutor.NO_SUCH_COMMAND;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
public class CommandExecutorTest {

    @Mock
    private Storage storageMock;

    @Mock
    private SocketChannel socketChannelMock;

    @Mock
    private Socket socketMock;

    @Mock
    private SocketAddress socketAddressMock;

    private final Gson gson = new Gson();

    @Test
    public void testCommandExecutorWithNonExistingCommand() {
        CommandExecutor executor = new CommandExecutor(storageMock, gson);

        String[] args = {"A", "3", "RIGHT", "2"};
        Command cmd = new Command("no-such-command", args);

        when(socketChannelMock.socket()).thenReturn(socketMock);
        when(socketMock.getRemoteSocketAddress()).thenReturn(socketAddressMock);
        when(socketAddressMock.toString()).thenReturn("id");

       assertEquals(String.format(NO_SUCH_COMMAND, BLUE, DEFAULT),
               executor.executeCommand(cmd, socketChannelMock, gson, true).getMsgThis(),
               "Expected response to have no such command message");
    }


}
