package bg.sofia.uni.fmi.mjt.battleships.online.server.game.player.board;

import static bg.sofia.uni.fmi.mjt.battleships.online.server.command.manager.CommandExecutor.BLUE;
import static bg.sofia.uni.fmi.mjt.battleships.online.server.command.manager.CommandExecutor.DEFAULT;
import static bg.sofia.uni.fmi.mjt.battleships.online.server.command.manager.CommandExecutor.RED;

public class BoardConstants {
    public static final int BOARD_SIZE = 10;
    public static final int TOTAL_SHIPS_CNT = 10;

    public static final String GAME_STATE_TITLE = BLUE + "GAME STATE:" + DEFAULT;
    public static final String MY_BOARD_TITLE = "       YOUR BOARD";
    public static final String ENEMY_BOARD_TITLE = "      ENEMY BOARD";

    public static final char FIRST_LETTER_CAPITAL = 'A';
    public static final char SPACE = ' ';

    public static final String COLS = "   1 2 3 4 5 6 7 8 9 10";
    public static final String DASHES = "   _ _ _ _ _ _ _ _ _ _";
    public static final String RED_SEPARATOR = RED + "|" + DEFAULT;

    public static final int SIMPLE_PLACE_ARGS_COUNT = 4;

    public static final String INVALID_DIRECTION = "Invalid direction";

    public static final String REG_EX_SPLIT_ARGS = " ";

    public static final int ROW_LETTER = 0;
    public static final int COL_INDEX = 1;
    public static final int SHIP_DIRECTION = 2;
    public static final int SHIP_LENGTH = 3;

    public static final String OUT_OF_BORDER = "Position out of border! See%s man%s for usage";
    public static final String INVALID_SHIP = "Invalid ship length! See%s man%s for usage";

    public static final int MIN_SHIP_LENGTH = 2;
    public static final int MAX_SHIP_LENGTH = 5;

    public static final int DESTROYER_LENGTH = 2;
    public static final int CRUISER_LENGTH = 3;
    public static final int BATTLESHIP_LENGTH = 4;
    public static final int CARRIER_LENGTH = 5;

    public static final int DESTROYER_COUNT = 4;
    public static final int CRUISER_COUNT = 3;
    public static final int BATTLESHIP_COUNT = 2;
    public static final int CARRIER_COUNT = 1;

    public static final String SHIPS_TYPE_OVERFLOW = "You have already maximum ships of that type";
    public static final String OUT_OF_BORDER_UP = "Out of border [UP]!";
    public static final String OUT_OF_BORDER_RIGHT = "Out of border [RIGHT]!";
    public static final String OUT_OF_BORDER_DOWN = "Out of border [DOWN]!";
    public static final String OUT_OF_BORDER_LEFT = "Out of border [LEFT]!";

    public static final String INVALID_PLACEMENT = "There already is a ship placed there";
    public static final String VALID_PLACE_MSG = "%s%s%s added";

    public static final int MIN_COL_INDEX = 1;
    public static final int MAX_COL_INDEX = 10;
    public static final int MIN_ROW_LETTER = 'a';
    public static final int MAX_ROW_LETTER = 'j';

    public static final int DIRECTION_TYPES = 4;

    public static final String READY_TO_ATTACK = "All ships were added, you are ready to attack!";

    public static final int DIR_UP = 0;
    public static final int DIR_RIGHT = 1;
    public static final int DIR_DOWN = 2;

    public static final int ATTACK_ARGS_CNT = 2;

    public static final String INVALID_COORDINATES_PLACE =
            "Invalid coordinates. Usage: place <[A-J]> <[1-10]> <[UP, RIGHT, DOWN, RIGHT]> <[2-5]>";
    public static final String INVALID_COORDINATES_ATTACK = "Invalid coordinates. Usage: attack <[A-J]> <[1-10]>";

    public static final String MSG_ATTACKER_MASSIVE_ATTACK = "You hit %d time(s) and destroyed %d ship(s)";
    public static final String MSG_DEFENDER_MASSIVE_ATTACK =
            "You were massively attacked and hit %d time(s) and lost %d ship(s)!";
    public static final String MSG_DEFENDER_INFO = "'s last turn: %c%c";
}