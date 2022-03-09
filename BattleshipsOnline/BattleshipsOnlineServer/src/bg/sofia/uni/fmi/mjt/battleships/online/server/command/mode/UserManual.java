package bg.sofia.uni.fmi.mjt.battleships.online.server.command.mode;

import bg.sofia.uni.fmi.mjt.battleships.online.server.command.response.CommandResponseDTO;
import bg.sofia.uni.fmi.mjt.battleships.online.server.game.player.PlayerStatus;
import bg.sofia.uni.fmi.mjt.battleships.online.server.storage.Storage;

import static bg.sofia.uni.fmi.mjt.battleships.online.server.command.manager.CommandExecutor.BLUE;
import static bg.sofia.uni.fmi.mjt.battleships.online.server.command.manager.CommandExecutor.DEFAULT;
import static bg.sofia.uni.fmi.mjt.battleships.online.server.command.manager.CommandExecutor.RED;

public class UserManual {
    private static final String USER_MANUAL_SURFING_MODE =
            BLUE + "User manual in SURFING mode:" + DEFAULT + System.lineSeparator() +
                    RED + "> " + DEFAULT + "man" + System.lineSeparator() +
                    RED + "> " + DEFAULT + "who-am-i" + System.lineSeparator() +
                    RED + "> " + DEFAULT + "set-username <username>" + System.lineSeparator() +
                    RED + "> " + DEFAULT + "change-username <username>" + System.lineSeparator() +
                    RED + "> " + DEFAULT + "list-users" + System.lineSeparator() +
                    RED + "> " + DEFAULT + "list-games" + System.lineSeparator() +
                    RED + "> " + DEFAULT + "register" + System.lineSeparator() +
                    RED + "> " + DEFAULT + "login <api-key>" + System.lineSeparator() +
                    RED + "> " + DEFAULT + "create-game <game-name>" + System.lineSeparator() +
                    RED + "> " + DEFAULT + "join-game {<game-name>}" + System.lineSeparator() +
                    RED + "> " + DEFAULT + "current-game" + System.lineSeparator() +
                    RED + "> " + DEFAULT + "save-game" + System.lineSeparator() +
                    RED + "> " + DEFAULT + "saved-games" + System.lineSeparator() +
                    RED + "> " + DEFAULT + "load-game <game-name>" + System.lineSeparator() +
                    RED + "> " + DEFAULT + "disconnect";

    private static final String USER_MANUAL_PLAYING_MODE =
            BLUE + "User manual in PLAYING mode:" + DEFAULT + System.lineSeparator() +
                    RED + "> " + DEFAULT + "display" + System.lineSeparator() +
                    RED + "> " + DEFAULT + "place <[A-J]> <[1-10]> <[UP, RIGHT, LEFT, DOWN]> <[2-5]>" +
                    System.lineSeparator() +
                    RED + "> " + DEFAULT + "place-all" + System.lineSeparator() +
                    RED + "> " + DEFAULT + "attack <[A-J]> <[1-10]>" + System.lineSeparator() +
                    RED + "> " + DEFAULT + "hacks";

    public static CommandResponseDTO execute(Storage storage, String id) {
        if (storage.getPlayerStatus(id) == PlayerStatus.ONLINE) {
            return new CommandResponseDTO(USER_MANUAL_SURFING_MODE);
        } else {
            return new CommandResponseDTO(USER_MANUAL_PLAYING_MODE);
        }
    }
}
