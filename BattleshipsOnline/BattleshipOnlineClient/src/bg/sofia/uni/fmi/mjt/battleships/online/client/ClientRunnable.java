package bg.sofia.uni.fmi.mjt.battleships.online.client;

import bg.sofia.uni.fmi.mjt.battleships.online.client.dto.MessageJson;
import com.google.gson.Gson;

import java.io.BufferedReader;
import java.io.IOException;

public class ClientRunnable implements Runnable {
    private final BufferedReader reader;
    private final Gson gson = new Gson();

    public ClientRunnable(BufferedReader reader) {
        this.reader = reader;
    }

    @Override
    public void run() {
        String reply;
        while (true) {
            try {
                if ((reply = reader.readLine()) != null) {
                    MessageJson msg = gson.fromJson(reply, MessageJson.class);
                    System.out.println(BattleshipsClient.RED + "> " + BattleshipsClient.DEFAULT + msg.getMsg());
                    System.out.print("battleships menu" + BattleshipsClient.RED + "> " + BattleshipsClient.DEFAULT);
                }
            } catch (IOException e) {
                System.out.println("Disconnected from server");
                break;
            }
        }
    }
}

