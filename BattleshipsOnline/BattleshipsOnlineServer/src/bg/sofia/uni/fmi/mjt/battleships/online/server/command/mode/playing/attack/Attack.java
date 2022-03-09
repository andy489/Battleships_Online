package bg.sofia.uni.fmi.mjt.battleships.online.server.command.mode.playing.attack;

import bg.sofia.uni.fmi.mjt.battleships.online.server.command.response.AttackResponseDTO;
import bg.sofia.uni.fmi.mjt.battleships.online.server.command.response.CommandResponseDTO;
import bg.sofia.uni.fmi.mjt.battleships.online.server.game.Game;
import bg.sofia.uni.fmi.mjt.battleships.online.server.game.GameStatus;
import bg.sofia.uni.fmi.mjt.battleships.online.server.game.player.Player;
import bg.sofia.uni.fmi.mjt.battleships.online.server.game.player.PlayerStatus;
import bg.sofia.uni.fmi.mjt.battleships.online.server.storage.DisplayGameHelper;
import bg.sofia.uni.fmi.mjt.battleships.online.server.storage.Storage;

import java.nio.channels.SocketChannel;

import static bg.sofia.uni.fmi.mjt.battleships.online.server.command.manager.CommandExecutor.BLUE;
import static bg.sofia.uni.fmi.mjt.battleships.online.server.command.manager.CommandExecutor.DEFAULT;

public class Attack {
    private static final String ATTACK_USAGE = "Not enough arguments. Usage: attack <[A-J]> <[1-10]>";
    private static final String NOT_IN_GAME = "No current game";
    private static final String SINGLE_PLAYER_MODE_MSG = "Cannot start a single player mode";
    private static final String ONE_PLAYER_IS_NOT_PRESSED_START = "One of the players did not press start";
    private static final String NOT_YOUR_TURN = "It's not your turn";
    private static final String WIN_MSG = "All enemy ships are destroyed. %sYOU WIN!%s";
    private static final String LOST_MSG = "All your ships have been destroyed. %sYOU LOST!%s";

    public static CommandResponseDTO execute(Storage storage, String id, String... args) {
        if (args.length < 2) {
            return new CommandResponseDTO(ATTACK_USAGE);
        }

        if (storage.getPlayerStatus(id) == PlayerStatus.ONLINE) {
            return new CommandResponseDTO(NOT_IN_GAME);
        }

        Game currGame = storage.getCurrentGame(id);

        if (currGame.getStatus() != GameStatus.IN_PROGRESS) {
            return new CommandResponseDTO(SINGLE_PLAYER_MODE_MSG);
        }

        if (currGame.notReadyToStart()) {
            return new CommandResponseDTO(ONE_PLAYER_IS_NOT_PRESSED_START);
        }

        Player otherPlayer;

        if (currGame.getHostPlayer().id().equals(id)) {
            if (!currGame.getHostTurn()) {
                return new CommandResponseDTO(NOT_YOUR_TURN);
            }

            otherPlayer = currGame.getGuestPlayer();
        } else {
            if (currGame.getHostTurn()) {
                return new CommandResponseDTO(NOT_YOUR_TURN);
            }

            otherPlayer = currGame.getHostPlayer();
        }

        SocketChannel otherSocketChannel = otherPlayer.socketChannel();
        String thisUsername = storage.getUsername(id);

        AttackResponseDTO response = otherPlayer.board().attack(args[0] + " " + args[1]);
        response.setMsgDefender(thisUsername + response.getMsgDefender());

        if (response.getValidAttack()) {
            storage.getCurrentGame(id).flipTurn();
        } else {
            return new CommandResponseDTO(response.getMsgAttacker());
        }

        if (response.checkEndGame()) {
            // GAME END

            String otherId = otherPlayer.id();

            storage.getUserIn(id).removeCurrentGame();
            storage.getUserIn(otherId).removeCurrentGame();

            storage.setPlayerStatus(id, PlayerStatus.ONLINE);
            storage.setPlayerStatus(otherId, PlayerStatus.ONLINE);

            return new CommandResponseDTO(
                    String.format(WIN_MSG, BLUE, DEFAULT),
                    String.format(LOST_MSG, BLUE, DEFAULT),
                    otherSocketChannel
            );
        }

        CommandResponseDTO responseDisplayThisPlayer =
                new CommandResponseDTO(DisplayGameHelper.displayGameState(storage, id));
        CommandResponseDTO responseDisplayOtherPlayer =
                new CommandResponseDTO(DisplayGameHelper.displayGameState(storage, otherPlayer.id()));

        return new CommandResponseDTO(
                responseDisplayThisPlayer.getMsgThis() + response.getMsgAttacker(),
                responseDisplayOtherPlayer.getMsgThis() + response.getMsgDefender(),
                otherSocketChannel
        );
    }
}
