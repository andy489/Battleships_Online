package bg.sofia.uni.fmi.mjt.battleships.online.server.game.player.board;

import bg.sofia.uni.fmi.mjt.battleships.online.server.command.response.AttackResponseDTO;
import bg.sofia.uni.fmi.mjt.battleships.online.server.command.response.PlaceResponseDTO;
import bg.sofia.uni.fmi.mjt.battleships.online.server.game.player.board.field.Field;
import bg.sofia.uni.fmi.mjt.battleships.online.server.game.player.board.field.FieldType;
import bg.sofia.uni.fmi.mjt.battleships.online.server.game.player.board.field.ship.Ship;
import bg.sofia.uni.fmi.mjt.battleships.online.server.game.player.board.field.ship.ShipDirection;
import bg.sofia.uni.fmi.mjt.battleships.online.server.game.player.board.field.ship.ShipType;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Random;

import static bg.sofia.uni.fmi.mjt.battleships.online.server.command.manager.CommandExecutor.BLUE;
import static bg.sofia.uni.fmi.mjt.battleships.online.server.command.manager.CommandExecutor.DEFAULT;
import static bg.sofia.uni.fmi.mjt.battleships.online.server.command.manager.CommandExecutor.MISSING_ARGUMENT;
import static bg.sofia.uni.fmi.mjt.battleships.online.server.command.manager.CommandExecutor.RED;
import static bg.sofia.uni.fmi.mjt.battleships.online.server.game.player.board.BoardConstants.INVALID_COORDINATES_ATTACK;
import static bg.sofia.uni.fmi.mjt.battleships.online.server.game.player.board.BoardConstants.INVALID_COORDINATES_PLACE;
import static bg.sofia.uni.fmi.mjt.battleships.online.server.game.player.board.BoardConstants.INVALID_DIRECTION;
import static bg.sofia.uni.fmi.mjt.battleships.online.server.game.player.board.BoardConstants.READY_TO_ATTACK;
import static bg.sofia.uni.fmi.mjt.battleships.online.server.game.player.board.BoardConstants.ATTACK_ARGS_CNT;
import static bg.sofia.uni.fmi.mjt.battleships.online.server.game.player.board.BoardConstants.BATTLESHIP_COUNT;
import static bg.sofia.uni.fmi.mjt.battleships.online.server.game.player.board.BoardConstants.BATTLESHIP_LENGTH;
import static bg.sofia.uni.fmi.mjt.battleships.online.server.game.player.board.BoardConstants.BOARD_SIZE;
import static bg.sofia.uni.fmi.mjt.battleships.online.server.game.player.board.BoardConstants.CARRIER_COUNT;
import static bg.sofia.uni.fmi.mjt.battleships.online.server.game.player.board.BoardConstants.CARRIER_LENGTH;
import static bg.sofia.uni.fmi.mjt.battleships.online.server.game.player.board.BoardConstants.COLS;
import static bg.sofia.uni.fmi.mjt.battleships.online.server.game.player.board.BoardConstants.COL_INDEX;
import static bg.sofia.uni.fmi.mjt.battleships.online.server.game.player.board.BoardConstants.CRUISER_COUNT;
import static bg.sofia.uni.fmi.mjt.battleships.online.server.game.player.board.BoardConstants.CRUISER_LENGTH;
import static bg.sofia.uni.fmi.mjt.battleships.online.server.game.player.board.BoardConstants.DASHES;
import static bg.sofia.uni.fmi.mjt.battleships.online.server.game.player.board.BoardConstants.DESTROYER_COUNT;
import static bg.sofia.uni.fmi.mjt.battleships.online.server.game.player.board.BoardConstants.DESTROYER_LENGTH;
import static bg.sofia.uni.fmi.mjt.battleships.online.server.game.player.board.BoardConstants.DIRECTION_TYPES;
import static bg.sofia.uni.fmi.mjt.battleships.online.server.game.player.board.BoardConstants.DIR_DOWN;
import static bg.sofia.uni.fmi.mjt.battleships.online.server.game.player.board.BoardConstants.DIR_RIGHT;
import static bg.sofia.uni.fmi.mjt.battleships.online.server.game.player.board.BoardConstants.DIR_UP;
import static bg.sofia.uni.fmi.mjt.battleships.online.server.game.player.board.BoardConstants.ENEMY_BOARD_TITLE;
import static bg.sofia.uni.fmi.mjt.battleships.online.server.game.player.board.BoardConstants.FIRST_LETTER_CAPITAL;
import static bg.sofia.uni.fmi.mjt.battleships.online.server.game.player.board.BoardConstants.GAME_STATE_TITLE;
import static bg.sofia.uni.fmi.mjt.battleships.online.server.game.player.board.BoardConstants.INVALID_PLACEMENT;
import static bg.sofia.uni.fmi.mjt.battleships.online.server.game.player.board.BoardConstants.INVALID_SHIP;
import static bg.sofia.uni.fmi.mjt.battleships.online.server.game.player.board.BoardConstants.MAX_COL_INDEX;
import static bg.sofia.uni.fmi.mjt.battleships.online.server.game.player.board.BoardConstants.MAX_ROW_LETTER;
import static bg.sofia.uni.fmi.mjt.battleships.online.server.game.player.board.BoardConstants.MAX_SHIP_LENGTH;
import static bg.sofia.uni.fmi.mjt.battleships.online.server.game.player.board.BoardConstants.MIN_COL_INDEX;
import static bg.sofia.uni.fmi.mjt.battleships.online.server.game.player.board.BoardConstants.MIN_ROW_LETTER;
import static bg.sofia.uni.fmi.mjt.battleships.online.server.game.player.board.BoardConstants.MIN_SHIP_LENGTH;
import static bg.sofia.uni.fmi.mjt.battleships.online.server.game.player.board.BoardConstants.MSG_ATTACKER_MASSIVE_ATTACK;
import static bg.sofia.uni.fmi.mjt.battleships.online.server.game.player.board.BoardConstants.MSG_DEFENDER_INFO;
import static bg.sofia.uni.fmi.mjt.battleships.online.server.game.player.board.BoardConstants.MSG_DEFENDER_MASSIVE_ATTACK;
import static bg.sofia.uni.fmi.mjt.battleships.online.server.game.player.board.BoardConstants.MY_BOARD_TITLE;
import static bg.sofia.uni.fmi.mjt.battleships.online.server.game.player.board.BoardConstants.OUT_OF_BORDER;
import static bg.sofia.uni.fmi.mjt.battleships.online.server.game.player.board.BoardConstants.OUT_OF_BORDER_DOWN;
import static bg.sofia.uni.fmi.mjt.battleships.online.server.game.player.board.BoardConstants.OUT_OF_BORDER_LEFT;
import static bg.sofia.uni.fmi.mjt.battleships.online.server.game.player.board.BoardConstants.OUT_OF_BORDER_RIGHT;
import static bg.sofia.uni.fmi.mjt.battleships.online.server.game.player.board.BoardConstants.OUT_OF_BORDER_UP;
import static bg.sofia.uni.fmi.mjt.battleships.online.server.game.player.board.BoardConstants.RED_SEPARATOR;
import static bg.sofia.uni.fmi.mjt.battleships.online.server.game.player.board.BoardConstants.REG_EX_SPLIT_ARGS;
import static bg.sofia.uni.fmi.mjt.battleships.online.server.game.player.board.BoardConstants.ROW_LETTER;
import static bg.sofia.uni.fmi.mjt.battleships.online.server.game.player.board.BoardConstants.SHIPS_TYPE_OVERFLOW;
import static bg.sofia.uni.fmi.mjt.battleships.online.server.game.player.board.BoardConstants.SHIP_DIRECTION;
import static bg.sofia.uni.fmi.mjt.battleships.online.server.game.player.board.BoardConstants.SHIP_LENGTH;
import static bg.sofia.uni.fmi.mjt.battleships.online.server.game.player.board.BoardConstants.SIMPLE_PLACE_ARGS_COUNT;
import static bg.sofia.uni.fmi.mjt.battleships.online.server.game.player.board.BoardConstants.SPACE;
import static bg.sofia.uni.fmi.mjt.battleships.online.server.game.player.board.BoardConstants.TOTAL_SHIPS_CNT;
import static bg.sofia.uni.fmi.mjt.battleships.online.server.game.player.board.BoardConstants.VALID_PLACE_MSG;

public class Board {
    private final Field[][] board;
    private final Map<Integer, List<Ship>> ships; // ships[i] -> all ships with length 3

    private int totalDestroyedShips;

    {
        board = new Field[BOARD_SIZE][BOARD_SIZE];
        ships = new HashMap<>();
    }

    public Board() {
        initBoard();
    }

    public String displayBoard(boolean my) {
        StringBuilder sb = new StringBuilder();

        if (my) {
            sb
                    .append(GAME_STATE_TITLE)
                    .append(System.lineSeparator())
                    .append(MY_BOARD_TITLE)
                    .append(System.lineSeparator());
        } else {
            sb
                    .append(ENEMY_BOARD_TITLE)
                    .append(System.lineSeparator());
        }

        char letter = FIRST_LETTER_CAPITAL;

        sb.append(COLS);
        sb.append(System.lineSeparator());
        sb.append(DASHES);
        sb.append(System.lineSeparator());


        for (int i = 0; i < BOARD_SIZE; i++) {
            sb.append(letter).append(SPACE);

            for (int j = 0; j < BOARD_SIZE; j++) {
                FieldType fieldType = board[i][j].getFieldType();

                sb.append(RED_SEPARATOR);

                if (fieldType == FieldType.SHIP) {
                    if (my) {
                        sb.append(FieldType.SHIP);
                    } else {
                        sb.append(FieldType.EMPTY);
                    }
                } else if (fieldType == FieldType.HIT) {
                    if (my) {
                        sb.append(RED).append(FieldType.HIT).append(DEFAULT);
                    } else {
                        sb.append(FieldType.SHIP);
                    }
                } else if (fieldType == FieldType.MISS) {
                    sb.append(FieldType.MISS);
                } else {
                    sb.append(FieldType.EMPTY);
                }
            }

            sb.append(RED_SEPARATOR);
            sb.append(System.lineSeparator());

            letter++;
        }

        return sb.toString();
    }

    public PlaceResponseDTO placeShipSoft(char c, int i, ShipDirection dir, int len) {
        c = Character.toLowerCase(c);

        if (c < MIN_ROW_LETTER || c > MAX_ROW_LETTER || i < MIN_COL_INDEX || i > MAX_COL_INDEX) {
            return new PlaceResponseDTO(String.format(OUT_OF_BORDER, BLUE, DEFAULT));
        }

        if (len < MIN_SHIP_LENGTH || len > MAX_SHIP_LENGTH) {
            return new PlaceResponseDTO(String.format(INVALID_SHIP, BLUE, DEFAULT));
        }

        boolean destroyerShipsOverflow = len == DESTROYER_LENGTH &&
                ships.get(DESTROYER_LENGTH).size() == DESTROYER_COUNT;
        boolean cruiserShipsOverflow = len == CRUISER_LENGTH &&
                ships.get(CRUISER_LENGTH).size() == CRUISER_COUNT;
        boolean battleshipShipsOverflow = len == BATTLESHIP_LENGTH &&
                ships.get(BATTLESHIP_LENGTH).size() == BATTLESHIP_COUNT;
        boolean carrierShipsOverflow = len == CARRIER_LENGTH &&
                ships.get(CARRIER_LENGTH).size() == CARRIER_COUNT;

        if (destroyerShipsOverflow || carrierShipsOverflow || battleshipShipsOverflow || cruiserShipsOverflow) {
            return new PlaceResponseDTO(SHIPS_TYPE_OVERFLOW);
        }

        if (dir == ShipDirection.NOT_VALID) {
            return new PlaceResponseDTO(INVALID_DIRECTION);
        }

        if (dir == ShipDirection.UP && c - MIN_ROW_LETTER + 1 - len < 0) {
            return new PlaceResponseDTO(OUT_OF_BORDER_UP);
        }

        if (dir == ShipDirection.RIGHT && i + len - 1 > BOARD_SIZE) {
            return new PlaceResponseDTO(OUT_OF_BORDER_RIGHT);
        }

        if (dir == ShipDirection.DOWN && c - MIN_ROW_LETTER + len - 1 >= BOARD_SIZE) {
            return new PlaceResponseDTO(OUT_OF_BORDER_DOWN);
        }

        if (dir == ShipDirection.LEFT && i - len < 0) {
            return new PlaceResponseDTO(OUT_OF_BORDER_LEFT);
        }

        Ship ship = new Ship(len);

        boolean validDir = validShipPlace(dir, len, c, i, ship);

        if (!validDir) {
            return new PlaceResponseDTO(INVALID_PLACEMENT);
        }

        ships.get(len).add(ship);

        String shipType = switch (len) {
            case DESTROYER_LENGTH -> ShipType.DESTROYER.toString();
            case CRUISER_LENGTH -> ShipType.CRUISER.toString();
            case BATTLESHIP_LENGTH -> ShipType.BATTLESHIP.toString();
            default -> ShipType.CARRIER.toString();
        };

        return new PlaceResponseDTO(String.format(VALID_PLACE_MSG, BLUE, shipType, DEFAULT));
    }

    public PlaceResponseDTO placeShip(String raw) {
        String[] tokens = raw.split(REG_EX_SPLIT_ARGS);

        if (tokens.length < SIMPLE_PLACE_ARGS_COUNT) {
            return new PlaceResponseDTO(String.format(MISSING_ARGUMENT, BLUE, DEFAULT));
        }

        for (int i = 0; i < SIMPLE_PLACE_ARGS_COUNT; i++) {
            tokens[i] = tokens[i].toLowerCase();
        }

        char x = tokens[ROW_LETTER].charAt(0);
        int y;
        try {
            y = Integer.parseInt(tokens[COL_INDEX]);
        } catch (NumberFormatException e) {
            return new PlaceResponseDTO(INVALID_COORDINATES_PLACE);
        }

        ShipDirection dir = ShipDirection.of(tokens[SHIP_DIRECTION]);
        int len = Integer.parseInt(tokens[SHIP_LENGTH]);

        return placeShipSoft(x, y, dir, len);
    }

    public PlaceResponseDTO placeAllShipsRandom() {
        Random rand = new Random();

        char x;
        int y;
        ShipDirection dir;
        int len;

        while (!allShipsPlaced()) {
            x = (char) (rand.nextInt(BOARD_SIZE) + MIN_ROW_LETTER);
            y = rand.nextInt(BOARD_SIZE) + 1;

            int randDir = rand.nextInt(DIRECTION_TYPES);

            dir = switch (randDir) {
                case DIR_UP -> ShipDirection.UP;
                case DIR_RIGHT -> ShipDirection.RIGHT;
                case DIR_DOWN -> ShipDirection.DOWN;
                default -> ShipDirection.LEFT;
            };

            len = rand.nextInt(DIRECTION_TYPES) + DESTROYER_LENGTH;

            placeShipSoft(x, y, dir, len);
        }

        return new PlaceResponseDTO(READY_TO_ATTACK);
    }

    public int allShipsCount() {
        int currShipsCnt = 0;

        for (int i = DESTROYER_LENGTH; i <= CARRIER_LENGTH; i++) {
            currShipsCnt += ships.get(i).size();
        }

        return currShipsCnt;
    }

    public AttackResponseDTO attack(String pos) {
        String[] coordinates = pos.split(REG_EX_SPLIT_ARGS);

        if (coordinates.length < ATTACK_ARGS_CNT) {
            return new AttackResponseDTO(
                    false,
                    false,
                    INVALID_COORDINATES_ATTACK,
                    null,
                    false
            );
        }

        char c = coordinates[0].charAt(0);

        int i;
        try {
            i = Integer.parseInt(coordinates[1]);
        } catch (Exception e) {
            return new AttackResponseDTO(
                    false,
                    false,
                    INVALID_COORDINATES_ATTACK,
                    null,
                    false
            );
        }

        AttackResponseDTO response = attackSoft(c, i);

        if (response.getDestroyed()) {
            totalDestroyedShips++;
        }

        if (playerLost()) {
            response.endGame();
        }

        return response;
    }

    public AttackResponseDTO attackAll(char... c) {
        int hits = 0;
        int destroyed = 0;

        for (final char ch : c) {
            for (char i = MIN_COL_INDEX; i <= MAX_COL_INDEX; i++) {
                AttackResponseDTO response = attackSoft(ch, i);

                if (response.getHit()) {
                    hits++;
                }

                if (response.getDestroyed()) {
                    destroyed++;
                }
            }
        }

        AttackResponseDTO response = new AttackResponseDTO(
                hits > 0,
                destroyed > 0,
                String.format(MSG_ATTACKER_MASSIVE_ATTACK, hits, destroyed),
                String.format(MSG_DEFENDER_MASSIVE_ATTACK, hits, destroyed),
                true
        );

        totalDestroyedShips += destroyed;

        if (playerLost()) {
            response.endGame();
            return response;
        }

        return response;
    }

    public AttackResponseDTO attackSoft(char c, int i) {
        c = Character.toLowerCase(c);

        if (c < MIN_ROW_LETTER || c > MAX_ROW_LETTER || i < MIN_COL_INDEX || i > MAX_COL_INDEX) {
            return new AttackResponseDTO(
                    false,
                    false,
                    INVALID_COORDINATES_ATTACK,
                    null,
                    false
            );
        }

        AttackResponseDTO response = board[c - MIN_ROW_LETTER][i - MIN_COL_INDEX].attack();

        StringBuilder sb = new StringBuilder(response.getMsgAttacker())
                .append(Character.toUpperCase(c))
                .append(i);

        if (response.getHit()) {
            response.setMsgAttacker(sb.toString());
        }

        response.setMsgDefender(String.format(
                MSG_DEFENDER_INFO, Character.toUpperCase(c), '0' + i)
        );

        return response;
    }

    public boolean playerLost() {
        return totalDestroyedShips == TOTAL_SHIPS_CNT;
    }

    public boolean allShipsPlaced() {
        return allShipsCount() == TOTAL_SHIPS_CNT;
    }

    public Field[][] getBoard() {
        return board;
    }

    public Map<Integer, List<Ship>> getAllShips() {
        return ships;
    }

    private boolean validShipPlace(ShipDirection dir, int len, char c, int i, Ship ship) {
        return switch (dir) {
            case UP -> validShipPlaceUp(len, c, i, ship);
            case RIGHT -> validShipPlaceRight(len, c, i, ship);
            case DOWN -> validShipPlaceDown(len, c, i, ship);
            case LEFT -> validShipPlaceLeft(len, c, i, ship);
            case NOT_VALID -> false;
        };
    }

    private boolean validShipPlaceUp(int len, int c, int i, Ship ship) {
        for (int j = len; j > 0; j--) {
            if (board[c - 'a' + 1 - j][i - 1].getFieldType() == FieldType.SHIP) {
                return false;
            }
        }

        for (int j = len; j > 0; j--) {
            board[c - 'a' + 1 - j][i - 1].setFieldPointer(ship);
            ship.addCoordinateToLocation(c - 'a' + 1 - j, i - 1);
        }

        return true;
    }

    private boolean validShipPlaceRight(int len, int c, int i, Ship ship) {
        for (int j = 0; j < len; j++) {
            if (board[c - 'a'][i - 1 + j].getFieldType() == FieldType.SHIP) {
                return false;
            }
        }

        for (int j = 0; j < len; j++) {
            board[c - 'a'][i - 1 + j].setFieldPointer(ship);
            ship.addCoordinateToLocation(c - 'a', i - 1 + j);
        }

        return true;
    }

    private boolean validShipPlaceDown(int len, int c, int i, Ship ship) {
        for (int j = 0; j < len; j++) {
            if (board[c - 'a' + j][i - 1].getFieldType() == FieldType.SHIP) {
                return false;
            }
        }

        for (int j = 0; j < len; j++) {
            board[c - 'a' + j][i - 1].setFieldPointer(ship);
            ship.addCoordinateToLocation(c - 'a' + j, i - 1);
        }

        return true;
    }

    private boolean validShipPlaceLeft(int len, int c, int i, Ship ship) {
        for (int j = len; j > 0; j--) {
            if (board[c - 'a'][i - j].getFieldType() == FieldType.SHIP) {
                return false;
            }
        }

        for (int j = len; j > 0; j--) {
            board[c - 'a'][i - j].setFieldPointer(ship);
            ship.addCoordinateToLocation(c - 'a', i - j);
        }

        return true;
    }

    private void initBoard() {
        for (int i = MIN_SHIP_LENGTH; i <= MAX_SHIP_LENGTH; i++) {
            ships.put(i, new ArrayList<>());
        }

        for (int i = 0; i < BOARD_SIZE; i++) {
            for (int j = 0; j < BOARD_SIZE; j++) {
                board[i][j] = new Field();
            }
        }
    }
}