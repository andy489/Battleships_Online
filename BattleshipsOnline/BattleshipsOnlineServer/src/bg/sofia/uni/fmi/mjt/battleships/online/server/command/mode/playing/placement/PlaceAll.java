package bg.sofia.uni.fmi.mjt.battleships.online.server.command.mode.playing.placement;

import bg.sofia.uni.fmi.mjt.battleships.online.server.command.response.CommandResponseDTO;
import bg.sofia.uni.fmi.mjt.battleships.online.server.command.response.PlaceResponseDTO;
import bg.sofia.uni.fmi.mjt.battleships.online.server.game.Game;
import bg.sofia.uni.fmi.mjt.battleships.online.server.game.player.Player;
import bg.sofia.uni.fmi.mjt.battleships.online.server.game.player.PlayerStatus;
import bg.sofia.uni.fmi.mjt.battleships.online.server.storage.Storage;

import static bg.sofia.uni.fmi.mjt.battleships.online.server.command.manager.CommandExecutor.BLUE;
import static bg.sofia.uni.fmi.mjt.battleships.online.server.command.manager.CommandExecutor.DEFAULT;

public class PlaceAll {
    private static final String NOT_IN_GAME = "No current game";
    private static final String ALL_SHIPS_PLACED = "All ships are placed. Type \"%sStart%s\" to start the game";

    public static CommandResponseDTO execute(Storage storage, String id) {
        if (storage.getPlayerStatus(id) == PlayerStatus.ONLINE) {
            return new CommandResponseDTO(NOT_IN_GAME);
        }

        Game currGame = storage.getCurrentGame(id);

        Player player;
        if (currGame.getHostPlayer().id().equals(id)) {
            player = currGame.getHostPlayer();
        } else {
            player = currGame.getGuestPlayer();
        }

        boolean allShipsPlaced = player.board().allShipsPlaced(); // host/guest

        if (allShipsPlaced) {
            return new CommandResponseDTO(String.format(ALL_SHIPS_PLACED, BLUE, DEFAULT));
        }

        PlaceResponseDTO response = player.board().placeAllShipsRandom();

        StringBuilder sb = new StringBuilder(response.getMsg());

        String myBoard = player.board().displayBoard(true); // host/guest

        sb.append(System.lineSeparator()).append(myBoard);

        return new CommandResponseDTO(sb + String.format(ALL_SHIPS_PLACED, BLUE, DEFAULT));
    }
}
