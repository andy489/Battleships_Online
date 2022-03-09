package bg.sofia.uni.fmi.mjt.battleships.online.client.username.validator;

import bg.sofia.uni.fmi.mjt.battleships.online.client.dto.MessageJson;
import com.google.gson.Gson;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.PrintWriter;

import static bg.sofia.uni.fmi.mjt.battleships.online.client.BattleshipsClient.DEFAULT;
import static bg.sofia.uni.fmi.mjt.battleships.online.client.BattleshipsClient.RED;
import static bg.sofia.uni.fmi.mjt.battleships.online.client.username.input.InputUsername.enterUsername;

public class Validator {
    public static void connectWithValidAndFreeUsername(
            String username,
            BufferedReader reader,
            PrintWriter writer,
            Gson gson
    )
            throws IOException {
        writer.println("set-username " + username);

        String reply = reader.readLine();
        MessageJson msg = gson.fromJson(reply, MessageJson.class);

        System.out.println(RED + "> " + DEFAULT + msg.getMsg());

        while (msg.getMsg().endsWith("select another username")) {
            username = enterUsername();
            writer.println("set-username " + username);
            reply = reader.readLine();

            msg = gson.fromJson(reply, MessageJson.class);

            System.out.println("> " + msg.getMsg());
        }
    }
}
