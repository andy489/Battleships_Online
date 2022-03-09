package bg.sofia.uni.fmi.mjt.battleships.online.server.command.mode.view;

import bg.sofia.uni.fmi.mjt.battleships.online.server.command.response.CommandResponseDTO;
import bg.sofia.uni.fmi.mjt.battleships.online.server.storage.Storage;

public class WhoAmI {
    private static final String WHO_AM_I = "You are %s";

    public static CommandResponseDTO execute(Storage storage, String id) {
        return new CommandResponseDTO(String.format(WHO_AM_I, storage.getUsername(id)));
    }
}
