package bg.sofia.uni.fmi.mjt.battleships.online.server.command.mode.view;

import bg.sofia.uni.fmi.mjt.battleships.online.server.command.response.CommandResponseDTO;
import bg.sofia.uni.fmi.mjt.battleships.online.server.storage.DisplayGameHelper;
import bg.sofia.uni.fmi.mjt.battleships.online.server.storage.Storage;

public class Display {
    public static CommandResponseDTO execute(Storage storage, String id) {
        return new CommandResponseDTO(DisplayGameHelper.displayGameState(storage, id));
    }
}
