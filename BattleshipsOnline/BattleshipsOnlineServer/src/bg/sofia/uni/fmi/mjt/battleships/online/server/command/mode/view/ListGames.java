package bg.sofia.uni.fmi.mjt.battleships.online.server.command.mode.view;

import bg.sofia.uni.fmi.mjt.battleships.online.server.command.response.CommandResponseDTO;
import bg.sofia.uni.fmi.mjt.battleships.online.server.game.player.PlayerStatus;

import bg.sofia.uni.fmi.mjt.battleships.online.server.storage.Storage;

import static bg.sofia.uni.fmi.mjt.battleships.online.server.storage.ListGamesViewHelper.getCurrentGamesListConsumer;
import static bg.sofia.uni.fmi.mjt.battleships.online.server.storage.ListGamesViewHelper.getHeader;

public class ListGames {
    public static CommandResponseDTO execute(Storage storage) {
        StringBuilder sb = getHeader();

        storage.getUsersInAsStream()
                .filter(u -> u.getCurrGame() != null && u.getStatus() == PlayerStatus.HOST)
                .forEach(getCurrentGamesListConsumer(sb));

        sb.setLength(sb.length() - 1);

        return new CommandResponseDTO(sb.toString());
    }
}
