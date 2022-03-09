package bg.sofia.uni.fmi.mjt.battleships.online.server.command.manager;

import bg.sofia.uni.fmi.mjt.battleships.online.server.command.mode.playing.DeleteGame;
import bg.sofia.uni.fmi.mjt.battleships.online.server.command.mode.playing.attack.Attack;
import bg.sofia.uni.fmi.mjt.battleships.online.server.command.mode.playing.attack.AttackAll;
import bg.sofia.uni.fmi.mjt.battleships.online.server.command.mode.surfing.ChangeUsername;
import bg.sofia.uni.fmi.mjt.battleships.online.server.command.mode.surfing.CreateGame;
import bg.sofia.uni.fmi.mjt.battleships.online.server.command.mode.surfing.CurrentGame;
import bg.sofia.uni.fmi.mjt.battleships.online.server.command.mode.Disconnect;
import bg.sofia.uni.fmi.mjt.battleships.online.server.command.mode.view.Display;
import bg.sofia.uni.fmi.mjt.battleships.online.server.command.mode.playing.Hacks;
import bg.sofia.uni.fmi.mjt.battleships.online.server.command.mode.surfing.JoinGame;
import bg.sofia.uni.fmi.mjt.battleships.online.server.command.mode.view.ListGames;
import bg.sofia.uni.fmi.mjt.battleships.online.server.command.mode.view.ListUsers;
import bg.sofia.uni.fmi.mjt.battleships.online.server.command.mode.surfing.LoadGame;
import bg.sofia.uni.fmi.mjt.battleships.online.server.command.mode.surfing.Login;
import bg.sofia.uni.fmi.mjt.battleships.online.server.command.mode.playing.placement.Place;
import bg.sofia.uni.fmi.mjt.battleships.online.server.command.mode.playing.placement.PlaceAll;
import bg.sofia.uni.fmi.mjt.battleships.online.server.command.mode.surfing.Register;
import bg.sofia.uni.fmi.mjt.battleships.online.server.command.mode.playing.SaveGame;
import bg.sofia.uni.fmi.mjt.battleships.online.server.command.mode.surfing.SavedGames;
import bg.sofia.uni.fmi.mjt.battleships.online.server.command.mode.surfing.SetUsername;
import bg.sofia.uni.fmi.mjt.battleships.online.server.command.mode.playing.Start;
import bg.sofia.uni.fmi.mjt.battleships.online.server.command.mode.UserManual;
import bg.sofia.uni.fmi.mjt.battleships.online.server.command.mode.view.WhoAmI;
import bg.sofia.uni.fmi.mjt.battleships.online.server.command.response.CommandResponseDTO;

import bg.sofia.uni.fmi.mjt.battleships.online.server.storage.SetUpRegisteredUsersAndSavedGames;
import bg.sofia.uni.fmi.mjt.battleships.online.server.storage.Storage;

import com.google.gson.Gson;

import java.nio.channels.SocketChannel;

public class CommandExecutor {
    public static final String BLUE = "\u001B[34m";
    public static final String RED = "\u001B[31m";
    public static final String DEFAULT = "\u001B[0m";

    public static final String RED_SEP = RED + "|" + DEFAULT;
    public static final String SERVER_COMMAND_MSG = "Executing command %s from %s ";

    public static final String USER_MANUAL = "man";
    public static final String HACKS = "hacks";
    public static final String WHO_AM_I = "who-am-i";
    public static final String SET_USERNAME = "set-username";
    public static final String CHANGE_USERNAME = "change-username";
    public static final String LIST_USERS = "list-users";
    public static final String LIST_GAMES = "list-games";
    public static final String REGISTER = "register";
    public static final String LOGIN = "login";
    public static final String CREATE_GAME = "create-game";
    public static final String CURRENT_GAME = "current-game";
    public static final String JOIN_GAME = "join-game";
    public static final String SAVED_GAMES = "saved-games";
    public static final String SAVE_GAME = "save-game";
    public static final String LOAD_GAME = "load-game";
    public static final String DELETE_GAME = "delete-game";
    public static final String START = "start";
    public static final String PLACE = "place";
    public static final String PLACE_ALL = "place-all";
    public static final String DISPLAY = "display";
    public static final String ATTACK = "attack";
    public static final String ATTACK_ALL = "attack-all";
    public static final String DISCONNECT = "disconnect";

    public static final String MISSING_ARGUMENT = "Not enough arguments. See%s man%s for usage";

    public static final String NO_SUCH_COMMAND = "No such command. Please check user manual. Usage:%s man%s";

    public static final String SAVED_GAMES_DIR = "saved";
    public static final String CURR_DIR = ".";

    private final Storage storage;

    public CommandExecutor(Storage storage, Gson gson) {
        this.storage = storage;
        SetUpRegisteredUsersAndSavedGames.setUp(storage, gson);
    }

    public CommandResponseDTO executeCommand(
            Command cmd,
            SocketChannel socketChannel,
            Gson gson,
            boolean silent
    ) {
        String id = socketChannel.socket().getRemoteSocketAddress().toString();

        if (!silent) {
            System.out.printf(SERVER_COMMAND_MSG, cmd.command(), id);
            if (cmd.args().length == 0) {
                System.out.println("(with no args)");
            } else {
                System.out.print("(with args: ");
                for (final String arg : cmd.args()) {
                    System.out.printf("<%s> ", arg);
                }
                System.out.println(")");
            }
        }

        return switch (cmd.command().toLowerCase()) {
            case USER_MANUAL -> UserManual.execute(storage, id);
            case HACKS -> Hacks.execute(storage, id);
            case WHO_AM_I -> WhoAmI.execute(storage, id);
            case SET_USERNAME -> SetUsername.execute(storage, id, cmd.args());
            case CHANGE_USERNAME -> ChangeUsername.execute(storage, id, cmd.args());
            case LIST_USERS -> ListUsers.execute(storage);
            case LIST_GAMES -> ListGames.execute(storage);
            case REGISTER -> Register.execute(storage, id);
            case LOGIN -> Login.execute(storage, id, gson, cmd.args());
            case CREATE_GAME -> CreateGame.execute(storage, id, socketChannel, cmd.args());
            case JOIN_GAME -> JoinGame.execute(storage, id, socketChannel, cmd.args());
            case CURRENT_GAME -> CurrentGame.execute(storage, id);
            case SAVED_GAMES -> SavedGames.execute(storage, id);
            case SAVE_GAME -> SaveGame.execute(storage, id, gson);
            case LOAD_GAME -> LoadGame.execute(storage, id, socketChannel, cmd.args());
            case DELETE_GAME -> DeleteGame.execute(storage, id, cmd.args());
            case START -> Start.execute(storage, id);
            case PLACE -> Place.execute(storage, id, cmd.args());
            case PLACE_ALL -> PlaceAll.execute(storage, id);
            case DISPLAY -> Display.execute(storage, id);
            case ATTACK -> Attack.execute(storage, id, cmd.args());
            case ATTACK_ALL -> AttackAll.execute(storage, id, cmd.args());
            case DISCONNECT -> Disconnect.execute(storage, id, gson);
            default -> new CommandResponseDTO(String.format(NO_SUCH_COMMAND, BLUE, DEFAULT));
        };
    }
}