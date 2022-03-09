package bg.sofia.uni.fmi.mjt.battleships.online.server.command.mode.surfing;

import bg.sofia.uni.fmi.mjt.battleships.online.server.command.response.CommandResponseDTO;
import bg.sofia.uni.fmi.mjt.battleships.online.server.storage.Storage;
import bg.sofia.uni.fmi.mjt.battleships.online.server.storage.UserIn;

public class SetUsername {
    private static final String SET_USERNAME_USAGE = "Requires one argument. Usage: set-username <username>";
    private static final String ALREADY_SET_USERNAME = "Your username is already set to %s";
    private static final String USERNAME_TAKEN = "Username %s is taken, please select another username";
    private static final String SET_USERNAME_SUCCESS = "Your username is now %s";

    public static CommandResponseDTO execute(Storage storage, String id, String... args) {
        if (args.length == 0) {
            return new CommandResponseDTO(SET_USERNAME_USAGE);
        }

        if (storage.isUserConnected(id)) {
            return new CommandResponseDTO(String.format(ALREADY_SET_USERNAME, storage.getUsername(id)));
        }

        String username = args[0];

        boolean takenUsername = storage.getUsersInAsStream().anyMatch(u -> u.getUsername().equals(username));

        if (takenUsername) {
            return new CommandResponseDTO(String.format(USERNAME_TAKEN, username));
        }

        storage.addNewUserIn(id, new UserIn(username));
        return new CommandResponseDTO(String.format(SET_USERNAME_SUCCESS, username));
    }
}
