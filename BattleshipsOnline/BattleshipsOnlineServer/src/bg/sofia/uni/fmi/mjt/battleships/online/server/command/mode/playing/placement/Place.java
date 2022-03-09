package bg.sofia.uni.fmi.mjt.battleships.online.server.command.mode.playing.placement;

import bg.sofia.uni.fmi.mjt.battleships.online.server.command.response.CommandResponseDTO;
import bg.sofia.uni.fmi.mjt.battleships.online.server.command.response.PlaceResponseDTO;
import bg.sofia.uni.fmi.mjt.battleships.online.server.game.Game;
import bg.sofia.uni.fmi.mjt.battleships.online.server.game.player.Player;
import bg.sofia.uni.fmi.mjt.battleships.online.server.game.player.PlayerStatus;
import bg.sofia.uni.fmi.mjt.battleships.online.server.storage.Storage;

import static bg.sofia.uni.fmi.mjt.battleships.online.server.command.manager.CommandExecutor.BLUE;
import static bg.sofia.uni.fmi.mjt.battleships.online.server.command.manager.CommandExecutor.DEFAULT;

public class Place {
    private static final String PLACE_USAGE =
            "Not enough arguments. Usage: place <[A-J]> <[1-10]> <[UP, RIGHT, LEFT, DOWN]> <[2-5]>";
    private static final String NOT_IN_GAME = "No current game";
    private static final String ALL_SHIPS_PLACED = "All ships are placed. Type \"%sStart%s\" to start the game";

    private static final int PLACE_CMD_ARGS = 4;
    private static final int ROW_LETTER = 0;
    private static final int COL_INDEX = 1;
    private static final int SHIP_DIRECTION = 2;
    private static final int SHIP_LENGTH = 3;

    public static CommandResponseDTO execute(Storage storage, String id, String... args) {
        if (args.length < PLACE_CMD_ARGS) {
            return new CommandResponseDTO(PLACE_USAGE);
        }

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

        String shipPlace = args[ROW_LETTER] + " "
                + args[COL_INDEX] + " "
                + args[SHIP_DIRECTION] + " "
                + args[SHIP_LENGTH];

        PlaceResponseDTO response = player.board().placeShip(shipPlace); // host/guest
        String myBoard = player.board().displayBoard(true); // host/guest

        return new CommandResponseDTO(myBoard + response.getMsg());
    }
}
