package bg.sofia.uni.fmi.mjt.battleships.online.server.command.mode.surfing;

import bg.sofia.uni.fmi.mjt.battleships.online.server.command.response.CommandResponseDTO;
import bg.sofia.uni.fmi.mjt.battleships.online.server.game.Game;
import bg.sofia.uni.fmi.mjt.battleships.online.server.game.GameStatus;
import bg.sofia.uni.fmi.mjt.battleships.online.server.game.player.PlayerStatus;
import bg.sofia.uni.fmi.mjt.battleships.online.server.storage.Storage;
import bg.sofia.uni.fmi.mjt.battleships.online.server.storage.UserIn;

import java.nio.channels.SocketChannel;
import java.util.Collection;
import java.util.Objects;
import java.util.Optional;
import java.util.function.Predicate;

import static bg.sofia.uni.fmi.mjt.battleships.online.server.command.manager.CommandExecutor.BLUE;
import static bg.sofia.uni.fmi.mjt.battleships.online.server.command.manager.CommandExecutor.DEFAULT;

public class JoinGame {
    private static final String LOGIN_OR_REGISTER = "You must login or register first";
    private static final String ALREADY_IN_A_GAME = "You are already in a game";
    private static final String GAME_IN_PROGRESS = "Cannot join game \"%s\" -> Game is in progress";
    private static final String HOST_MSG = "Joined game \"%s\". Place your ships!";
    private static final String GUEST_MSG = "%s%s%s joined the game. Place your ships!";
    private static final String NO_RANDOM_GAME = "There is no pending game at the moment";
    private static final String NO_SUCH_GAME = "There is no such game";

    public static CommandResponseDTO execute(Storage storage, String id, SocketChannel socketChannel, String... args) {
        String apiKey = storage.getApiKey(id);

        if (storage.getApiKey(id) == null) {
            return new CommandResponseDTO(LOGIN_OR_REGISTER);
        }

        if (storage.getPlayerStatus(id) != PlayerStatus.ONLINE) {
            return new CommandResponseDTO(ALREADY_IN_A_GAME);
        }

        Predicate<Game> gameLambdaPredicate;

        if (args.length == 0) {
            gameLambdaPredicate = g -> g.getStatus() == GameStatus.PENDING;
        } else {
            gameLambdaPredicate = g -> g.getGameName().equals(args[0]);
        }

        Collection<Game> games = storage.getUsersInAsStream()
                .map(UserIn::getCurrGame).filter(Objects::nonNull).toList();

        Optional<Game> gameBox = games.stream().filter(gameLambdaPredicate).findAny();

        if (gameBox.isPresent()) {
            Game selectedGame = gameBox.get();

            if (selectedGame.getStatus() == GameStatus.IN_PROGRESS) {
                return new CommandResponseDTO(String.format(GAME_IN_PROGRESS, selectedGame.getGameName()));
            }

            String userName = storage.getUsername(id);

            selectedGame.join(userName, id, socketChannel, apiKey);

            storage.setCurrentGame(id, selectedGame);
            storage.setPlayerStatus(id, PlayerStatus.GUEST);

            return new CommandResponseDTO(
                    String.format(HOST_MSG, gameBox.get().getGameName()),
                    String.format(GUEST_MSG, BLUE, userName, DEFAULT),
                    selectedGame.getHostPlayer().socketChannel()
            );
        } else {
            if (args.length == 0) {
                return new CommandResponseDTO(NO_RANDOM_GAME);
            } else {
                return new CommandResponseDTO(NO_SUCH_GAME);
            }
        }
    }
}
