package bg.sofia.uni.fmi.mjt.battleships.online.server.command.mode.surfing;

import bg.sofia.uni.fmi.mjt.battleships.online.server.command.response.CommandResponseDTO;
import bg.sofia.uni.fmi.mjt.battleships.online.server.game.player.PlayerStatus;
import bg.sofia.uni.fmi.mjt.battleships.online.server.storage.Storage;

public class ChangeUsername {
    private static final String CHANGE_USERNAME_USAGE = "Requires one argument. Usage: set-username <username>";
    private static final String CHANGE_USERNAME_WHILE_PLAYING = "Cannot change username while you are playing";
    private static final String USERNAME_TAKEN_MSG = "Username %s is taken, please select another username";
    private static final String ALREADY_SET_USERNAME = "Your username is already set to %s";
    private static final String CHANGE_USERNAME_SUCCESS = "Username changed. Your username is now %s";

    public static CommandResponseDTO execute(Storage storage, String id, String... args) {
        if (args.length == 0) {
            return new CommandResponseDTO(CHANGE_USERNAME_USAGE);
        }

        if (storage.getPlayerStatus(id) != PlayerStatus.ONLINE) {
            return new CommandResponseDTO(CHANGE_USERNAME_WHILE_PLAYING);
        }

        String newUsername = args[0];

        String currUsername = storage.getUsername(id);

        if(currUsername.equals(newUsername)){
            return new CommandResponseDTO(String.format(ALREADY_SET_USERNAME, newUsername));
        }

        if (storage.getUsersInAsStream().anyMatch(u -> u.getUsername().equals(newUsername))) {
            return new CommandResponseDTO(String.format(USERNAME_TAKEN_MSG, newUsername));
        } else {
            storage.changeUsername(id, newUsername);
            return new CommandResponseDTO(String.format(CHANGE_USERNAME_SUCCESS, newUsername));
        }
    }
}
