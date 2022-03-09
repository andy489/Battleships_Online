package bg.sofia.uni.fmi.mjt.battleships.online.client.username.input;

import java.util.Scanner;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class InputUsername {
    public static String enterUsername() {
        Scanner input = new Scanner(System.in);
        Pattern pattern = Pattern.compile("^[a-zA-Z]{4,9}$");

        System.out.print("Please enter your username: ");

        String username;

        while (true) {
            username = input.nextLine();
            Matcher matcher = pattern.matcher(username);

            if (matcher.matches()) {
                break;
            }

            System.out.println("Accepts usernames only from 4 to 8 lowercase or uppercase latin letters!");
            System.out.print("Enter a valid username: ");
        }

        System.out.println("Trying to connect to Battle ships online with username " + username);

        return username;
    }
}
