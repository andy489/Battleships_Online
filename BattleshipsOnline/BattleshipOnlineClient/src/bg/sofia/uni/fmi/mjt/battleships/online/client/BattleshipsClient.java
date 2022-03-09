package bg.sofia.uni.fmi.mjt.battleships.online.client;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.PrintWriter;
import java.net.InetSocketAddress;
import java.nio.channels.Channels;
import java.nio.channels.SocketChannel;
import java.nio.charset.StandardCharsets;
import java.util.Scanner;

import com.google.gson.Gson;

import static bg.sofia.uni.fmi.mjt.battleships.online.client.username.input.InputUsername.enterUsername;
import static bg.sofia.uni.fmi.mjt.battleships.online.client.username.validator.Validator.connectWithValidAndFreeUsername;

public class BattleshipsClient {
    public static final String RED = "\u001B[31m";
    public static final String BLUE = "\u001B[34m";
    public static final String DEFAULT = "\u001B[0m";

    private static final int SERVER_PORT = 4889;
    private static final String SERVER_HOST = "localhost";

    private final String serverHost;
    private final int serverPort;

    private final Gson gson;

    public BattleshipsClient(String serverHost, int serverPort) {
        this.serverPort = serverPort;
        this.serverHost = serverHost;

        gson = new Gson();
    }

    public static void main(String... args) {
        new BattleshipsClient(SERVER_HOST, SERVER_PORT).start();
    }

    public void start() {
        String username = enterUsername();

        try (SocketChannel socketChannel = SocketChannel.open();
             var reader = new BufferedReader(Channels.newReader(socketChannel, StandardCharsets.UTF_8));
             var writer = new PrintWriter(Channels.newWriter(socketChannel, StandardCharsets.UTF_8), true);
             Scanner scanner = new Scanner(System.in)
        ) {
            socketChannel.connect(new InetSocketAddress(serverHost, serverPort));

            System.out.println(BLUE + "Welcome to Battleships online!" + DEFAULT);

            connectWithValidAndFreeUsername(username, reader, writer, gson);

            new Thread(new ClientRunnable(reader)).start();

            System.out.print("battleships menu" + RED + "> " + DEFAULT);

            while (true) {
                String message = scanner.nextLine();

                writer.println(message);

                if ("disconnect".equals(message)) {
                    System.out.println("See you soon on the battlefield " + username);
                    break;
                }
            }
        } catch (IOException e) {
            throw new RuntimeException("There is a problem with the network communication", e);
        }
    }
}


