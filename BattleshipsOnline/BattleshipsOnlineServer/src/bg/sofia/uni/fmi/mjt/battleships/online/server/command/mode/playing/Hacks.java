package bg.sofia.uni.fmi.mjt.battleships.online.server.command.mode.playing;

import bg.sofia.uni.fmi.mjt.battleships.online.server.command.response.CommandResponseDTO;
import bg.sofia.uni.fmi.mjt.battleships.online.server.game.player.PlayerStatus;
import bg.sofia.uni.fmi.mjt.battleships.online.server.storage.Storage;

public class Hacks {
    private static final String HACK_USAGE = "attack-all <[A-J]> {<[A-J]>}...";
    private static final String FORBIDDEN_COMMAND = "Cannot use hack commands in non-playing mode";

    public static CommandResponseDTO execute(Storage storage, String id) {
        if (storage.getPlayerStatus(id) == PlayerStatus.ONLINE) {
            return new CommandResponseDTO(FORBIDDEN_COMMAND);
        } else {
            return new CommandResponseDTO(HACK_USAGE);
        }
    }
}
