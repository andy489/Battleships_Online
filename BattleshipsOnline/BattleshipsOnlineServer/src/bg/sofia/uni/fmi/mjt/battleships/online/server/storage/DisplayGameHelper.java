package bg.sofia.uni.fmi.mjt.battleships.online.server.storage;

import bg.sofia.uni.fmi.mjt.battleships.online.server.game.Game;
import bg.sofia.uni.fmi.mjt.battleships.online.server.game.GameStatus;
import bg.sofia.uni.fmi.mjt.battleships.online.server.game.player.Player;
import bg.sofia.uni.fmi.mjt.battleships.online.server.game.player.PlayerStatus;

public class DisplayGameHelper {
    private static final String NOT_IN_GAME = "No current game";

    public static String displayGameState(Storage storage, String id) {
        if (storage.getPlayerStatus(id) == PlayerStatus.ONLINE) {
            return NOT_IN_GAME;
        }

        Game currGame = storage.getCurrentGame(id);

        StringBuilder sb = new StringBuilder();

        if (currGame.getStatus() == GameStatus.PENDING) {
            sb.append(currGame.getHostPlayer().board().displayBoard(true));
            return sb.toString();
        }

        Player hostPlayer = currGame.getHostPlayer();
        Player guestPlayer = currGame.getGuestPlayer();

        if (hostPlayer.id().equals(id)) {
            sb.append(hostPlayer.board().displayBoard(true));
            sb.append(guestPlayer.board().displayBoard(false));
        } else {
            sb.append(guestPlayer.board().displayBoard(true));
            sb.append(hostPlayer.board().displayBoard(false));
        }

        return sb.toString();
    }
}
