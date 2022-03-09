package bg.sofia.uni.fmi.mjt.battleships.online.server.storage;

import bg.sofia.uni.fmi.mjt.battleships.online.server.game.SavedGame;

import java.util.function.Consumer;

import static bg.sofia.uni.fmi.mjt.battleships.online.server.command.manager.CommandExecutor.DEFAULT;
import static bg.sofia.uni.fmi.mjt.battleships.online.server.command.manager.CommandExecutor.RED;
import static bg.sofia.uni.fmi.mjt.battleships.online.server.command.manager.CommandExecutor.RED_SEP;

public class ListGamesViewHelper {
    private static final String HEADER_NAME = " NAME";
    private static final String HEADER_CREATOR = " CREATOR";
    private static final String HEADER_STATUS = " STATUS";
    private static final String HEADER_PLAYERS = " PLAYERS";

    private static final int GAME_NAME_CELLS = 28;
    private static final int NOT_GAME_NAME_CELLS = 13;

    public static StringBuilder getHeader() {
        return new StringBuilder(RED_SEP).append(HEADER_NAME)
                .append(repeatChar(GAME_NAME_CELLS - HEADER_NAME.length(), ' '))
                .append(RED_SEP)
                .append(HEADER_CREATOR)
                .append(repeatChar(NOT_GAME_NAME_CELLS - HEADER_CREATOR.length(), ' '))
                .append(RED_SEP)
                .append(HEADER_STATUS)
                .append(repeatChar(NOT_GAME_NAME_CELLS - HEADER_STATUS.length(), ' '))
                .append(RED_SEP)
                .append(HEADER_PLAYERS)
                .append(repeatChar(NOT_GAME_NAME_CELLS - HEADER_PLAYERS.length(), ' '))
                .append(RED_SEP)
                .append(System.lineSeparator()).append(RED + "> |" + DEFAULT)
                .append(repeatChar(GAME_NAME_CELLS, '-'))
                .append("+")
                .append(repeatChar(NOT_GAME_NAME_CELLS, '-'))
                .append("+")
                .append(repeatChar(NOT_GAME_NAME_CELLS, '-'))
                .append("+")
                .append(repeatChar(NOT_GAME_NAME_CELLS, '-'))
                .append(RED_SEP)
                .append(System.lineSeparator());
    }

    public static Consumer<UserIn> getCurrentGamesListConsumer(StringBuilder sb) {
        return u -> sb.append(RED + "> |" + DEFAULT)
                .append(" ")
                .append(u.getCurrGame().getGameName())
                .append(repeatChar(GAME_NAME_CELLS - u.getCurrGame().getGameName().length() - 1, ' '))
                .append(RED_SEP)
                .append(" ")
                .append(u.getCurrGame().getHostName())
                .append(repeatChar(NOT_GAME_NAME_CELLS - u.getCurrGame().getHostName().length() - 1, ' '))
                .append(RED_SEP)
                .append(" ")
                .append(u.getCurrGame().getStatus())
                .append(repeatChar(NOT_GAME_NAME_CELLS - u.getCurrGame().getStatus().len() - 1, ' '))
                .append(RED_SEP)
                .append(" ")
                .append(u.getCurrGame().numPlayers())
                .append("/2")
                .append(repeatChar(NOT_GAME_NAME_CELLS - 4, ' '))
                .append(RED_SEP)
                .append(System.lineSeparator());
    }

    public static Consumer<SavedGame> getMySavedGamesListConsumer(StringBuilder sb) {
        return sg -> sb.append(RED + "> |" + DEFAULT).append(" ")
                .append(sg.getName())
                .append(repeatChar(GAME_NAME_CELLS - sg.getName().length() - 1, ' '))
                .append(RED_SEP)
                .append(" ")
                .append(sg.getWasThisPlayerHost() ? "host " : "guest")
                .append(repeatChar(NOT_GAME_NAME_CELLS - "guest".length() - 1, ' '))
                .append(RED_SEP)
                .append(" ")
                .append("saved")
                .append(repeatChar(NOT_GAME_NAME_CELLS - "saved".length() - 1, ' '))
                .append(RED_SEP)
                .append(" 0/2")
                .append(repeatChar(NOT_GAME_NAME_CELLS - 4, ' '))
                .append(RED_SEP)
                .append(System.lineSeparator());
    }

    private static String repeatChar(int n, char symbol) {
        n = Math.abs(n);
        return String.valueOf(symbol).repeat(Math.max(0, n));
    }
}
