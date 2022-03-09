package bg.sofia.uni.fmi.mjt.battleships.online.server;

import bg.sofia.uni.fmi.mjt.battleships.online.server.command.manager.CommandCreator;
import bg.sofia.uni.fmi.mjt.battleships.online.server.command.manager.CommandExecutor;
import bg.sofia.uni.fmi.mjt.battleships.online.server.command.manager.Command;

import bg.sofia.uni.fmi.mjt.battleships.online.server.command.response.CommandResponseDTO;

import bg.sofia.uni.fmi.mjt.battleships.online.server.command.response.MessageJson;
import bg.sofia.uni.fmi.mjt.battleships.online.server.storage.Storage;
import com.google.gson.Gson;

import java.io.IOException;
import java.io.UncheckedIOException;
import java.net.InetSocketAddress;
import java.nio.ByteBuffer;
import java.nio.channels.SelectionKey;
import java.nio.channels.Selector;
import java.nio.channels.ServerSocketChannel;
import java.nio.channels.SocketChannel;
import java.nio.charset.StandardCharsets;
import java.util.Iterator;

public class BattleshipsServer {
    private static final int BUFFER_SIZE = 1024 * 6;
    private static final String SERVER_HOST = "localhost";
    private static final int SERVER_PORT = 4889;

    private final CommandExecutor commandExecutor;

    private final int port;
    private boolean isServerWorking;

    private ByteBuffer buffer;
    private Selector selector;

    private final Gson gson;

    {
        gson = new Gson();
        commandExecutor = new CommandExecutor(new Storage(), gson);
    }

    public BattleshipsServer(int port) {
        this.port = port;
    }

    public static void main(String... args) {
        new BattleshipsServer(SERVER_PORT).start();
    }

    public void start() {
        try (ServerSocketChannel serverSocketChannel = ServerSocketChannel.open()) {
            selector = Selector.open();
            configureServerSocketChannel(serverSocketChannel, selector);
            this.buffer = ByteBuffer.allocate(BUFFER_SIZE);
            isServerWorking = true;

            System.out.println("Battle ships server started!");
            while (isServerWorking) {
                try {
                    int readyChannels = selector.select();

                    if (readyChannels == 0) {
                        continue;
                    }

                    Iterator<SelectionKey> keyIterator = selector.selectedKeys().iterator();
                    while (keyIterator.hasNext()) {
                        SelectionKey key = keyIterator.next();

                        if (key.isReadable()) {
                            SocketChannel clientChannel = (SocketChannel) key.channel();

                            String clientInput = getClientInput(clientChannel);

                            if (clientInput == null) {
                                continue;
                            }

                            Command cmd = CommandCreator.newCommand(clientInput);

                            CommandResponseDTO output = commandExecutor.executeCommand(
                                    cmd,
                                    clientChannel,
                                    gson,
                                    false
                            );

                            if (output.getMsgThis() != null) {
                                writeClientOutput(clientChannel, gson.toJson(new MessageJson(output.getMsgThis())));
                            }

                            if (output.getMsgOther() != null) {
                                writeClientOutput(
                                        output.getHostSocketChannel(),
                                        gson.toJson(new MessageJson(output.getMsgOther()))
                                );

                            }
                        } else if (key.isAcceptable()) {
                            accept(selector, key);
                        }
                        keyIterator.remove();
                    }
                } catch (IOException e) {
                    System.out.println("Error occurred while processing client request: " + e.getMessage());
                }
            }
        } catch (IOException e) {
            throw new UncheckedIOException("Failed to start server", e);
        }
    }

    public void stop() {
        this.isServerWorking = false;
        if (selector.isOpen()) {
            selector.wakeup();
        }
    }

    private void configureServerSocketChannel(ServerSocketChannel channel, Selector selector) throws IOException {
        channel.bind(new InetSocketAddress(SERVER_HOST, this.port));
        channel.configureBlocking(false);
        channel.register(selector, SelectionKey.OP_ACCEPT);
    }

    private String getClientInput(SocketChannel clientChannel) throws IOException {
        buffer.clear();

        int readBytes = clientChannel.read(buffer);
        if (readBytes < 0) {
            clientChannel.close();
            return null;
        }

        buffer.flip();

        byte[] clientInputBytes = new byte[buffer.remaining()];
        buffer.get(clientInputBytes);

        return new String(clientInputBytes, StandardCharsets.UTF_8).trim();
    }

    private void writeClientOutput(SocketChannel clientChannel, String output) throws IOException {
        buffer.clear();
        buffer.put((output + System.lineSeparator()).getBytes());
        buffer.flip();

        clientChannel.write(buffer);
    }

    private void accept(Selector selector, SelectionKey key) throws IOException {
        ServerSocketChannel sockChannel = (ServerSocketChannel) key.channel();
        SocketChannel accept = sockChannel.accept();

        System.out.println(accept.socket().getRemoteSocketAddress() + " connected to server!");

        accept.configureBlocking(false);
        accept.register(selector, SelectionKey.OP_READ);
    }
}