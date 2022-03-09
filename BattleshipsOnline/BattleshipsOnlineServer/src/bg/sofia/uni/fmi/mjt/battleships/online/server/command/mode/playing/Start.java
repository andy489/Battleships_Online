package bg.sofia.uni.fmi.mjt.battleships.online.server.command.mode.playing;

import bg.sofia.uni.fmi.mjt.battleships.online.server.command.response.CommandResponseDTO;
import bg.sofia.uni.fmi.mjt.battleships.online.server.game.Game;
import bg.sofia.uni.fmi.mjt.battleships.online.server.game.GameStatus;
import bg.sofia.uni.fmi.mjt.battleships.online.server.game.player.PlayerStatus;
import bg.sofia.uni.fmi.mjt.battleships.online.server.storage.Storage;

import java.nio.channels.SocketChannel;

import static bg.sofia.uni.fmi.mjt.battleships.online.server.command.manager.CommandExecutor.BLUE;
import static bg.sofia.uni.fmi.mjt.battleships.online.server.command.manager.CommandExecutor.DEFAULT;

public class Start {
    private static final String PLACE_ALL_YOUR_SHIPS = "You must place all your ships first";
    private static final String THIS_PLAYER_READY = "You are ready to start the battle";
    private static final String OTHER_PLAYER_READY = "%s%s%s is ready to start the battle";
    private static final String SINGLE_PLAYER_MODE_MSG = "Cannot start a single player mode";
    private static final String NOT_IN_GAME = "No current game";

    public static CommandResponseDTO execute(Storage storage, String id) {
        PlayerStatus currStatus = storage.getPlayerStatus(id);

        if (currStatus == PlayerStatus.ONLINE) {
            return new CommandResponseDTO(NOT_IN_GAME);
        }

        Game currGame = storage.getCurrentGame(id);

        if (currGame.getStatus() != GameStatus.IN_PROGRESS) {
            return new CommandResponseDTO(SINGLE_PLAYER_MODE_MSG);
        }

        SocketChannel otherSocket;
        String otherUsername;

        if (currGame.getHostPlayer().id().equals(id)) {
            if (!currGame.getHostPlayer().board().allShipsPlaced()) {
                return new CommandResponseDTO(PLACE_ALL_YOUR_SHIPS);
            }

            currGame.setHostReady();
            otherSocket = currGame.getGuestPlayer().socketChannel();
            otherUsername = currGame.getHostPlayer().username();
        } else {
            if (!currGame.getGuestPlayer().board().allShipsPlaced()) {
                return new CommandResponseDTO(PLACE_ALL_YOUR_SHIPS);
            }

            currGame.setGuestReady();
            otherSocket = currGame.getHostPlayer().socketChannel();
            otherUsername = currGame.getGuestPlayer().username();
        }

        return new CommandResponseDTO(
                THIS_PLAYER_READY,
                String.format(OTHER_PLAYER_READY, BLUE, otherUsername, DEFAULT),
                otherSocket
        );
    }
}
