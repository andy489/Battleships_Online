package bg.sofia.uni.fmi.mjt.battleships.online.server.game.player.board;

import bg.sofia.uni.fmi.mjt.battleships.online.server.command.response.AttackResponseDTO;
import bg.sofia.uni.fmi.mjt.battleships.online.server.command.response.PlaceResponseDTO;
import bg.sofia.uni.fmi.mjt.battleships.online.server.game.player.board.field.ship.ShipType;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static bg.sofia.uni.fmi.mjt.battleships.online.server.command.manager.CommandExecutor.BLUE;
import static bg.sofia.uni.fmi.mjt.battleships.online.server.command.manager.CommandExecutor.DEFAULT;
import static bg.sofia.uni.fmi.mjt.battleships.online.server.command.manager.CommandExecutor.MISSING_ARGUMENT;
import static bg.sofia.uni.fmi.mjt.battleships.online.server.command.manager.CommandExecutor.RED;
import static bg.sofia.uni.fmi.mjt.battleships.online.server.game.player.board.BoardConstants.BOARD_SIZE;
import static bg.sofia.uni.fmi.mjt.battleships.online.server.game.player.board.BoardConstants.INVALID_COORDINATES_ATTACK;
import static bg.sofia.uni.fmi.mjt.battleships.online.server.game.player.board.BoardConstants.INVALID_COORDINATES_PLACE;
import static bg.sofia.uni.fmi.mjt.battleships.online.server.game.player.board.BoardConstants.INVALID_DIRECTION;
import static bg.sofia.uni.fmi.mjt.battleships.online.server.game.player.board.BoardConstants.INVALID_PLACEMENT;
import static bg.sofia.uni.fmi.mjt.battleships.online.server.game.player.board.BoardConstants.INVALID_SHIP;
import static bg.sofia.uni.fmi.mjt.battleships.online.server.game.player.board.BoardConstants.MSG_ATTACKER_MASSIVE_ATTACK;
import static bg.sofia.uni.fmi.mjt.battleships.online.server.game.player.board.BoardConstants.MSG_DEFENDER_INFO;
import static bg.sofia.uni.fmi.mjt.battleships.online.server.game.player.board.BoardConstants.MSG_DEFENDER_MASSIVE_ATTACK;
import static bg.sofia.uni.fmi.mjt.battleships.online.server.game.player.board.BoardConstants.OUT_OF_BORDER;
import static bg.sofia.uni.fmi.mjt.battleships.online.server.game.player.board.BoardConstants.OUT_OF_BORDER_DOWN;
import static bg.sofia.uni.fmi.mjt.battleships.online.server.game.player.board.BoardConstants.OUT_OF_BORDER_LEFT;
import static bg.sofia.uni.fmi.mjt.battleships.online.server.game.player.board.BoardConstants.OUT_OF_BORDER_RIGHT;
import static bg.sofia.uni.fmi.mjt.battleships.online.server.game.player.board.BoardConstants.OUT_OF_BORDER_UP;
import static bg.sofia.uni.fmi.mjt.battleships.online.server.game.player.board.BoardConstants.READY_TO_ATTACK;
import static bg.sofia.uni.fmi.mjt.battleships.online.server.game.player.board.BoardConstants.SHIPS_TYPE_OVERFLOW;
import static bg.sofia.uni.fmi.mjt.battleships.online.server.game.player.board.BoardConstants.TOTAL_SHIPS_CNT;
import static bg.sofia.uni.fmi.mjt.battleships.online.server.game.player.board.BoardConstants.VALID_PLACE_MSG;
import static bg.sofia.uni.fmi.mjt.battleships.online.server.game.player.board.field.FieldConstants.ATTACK_DESTROYED;
import static bg.sofia.uni.fmi.mjt.battleships.online.server.game.player.board.field.FieldConstants.ATTACK_HIT;
import static bg.sofia.uni.fmi.mjt.battleships.online.server.game.player.board.field.FieldConstants.ATTACK_MISSED;
import static bg.sofia.uni.fmi.mjt.battleships.online.server.game.player.board.field.FieldConstants.MISS;
import static bg.sofia.uni.fmi.mjt.battleships.online.server.game.player.board.field.ship.ShipConstants.DESTROY;
import static bg.sofia.uni.fmi.mjt.battleships.online.server.game.player.board.field.ship.ShipConstants.HIT;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

public class BoardTest {
    private Board board;

    @BeforeEach
    public void setUp() {
        board = new Board();
    }

    @Test
    public void testDisplayBoardMyAndEnemyBoard() {
        int lengthMyBoardDisplayMsg = board.displayBoard(true).length();
        int lengthEnemyBoardDisplayMsg = board.displayBoard(false).length();

        String myBoard = board.displayBoard(true);
        String enemyBoard = board.displayBoard(false);

        final int EQUAL_SUFFIX_LENGTH = 1284;
        int i = 0;
        while (i != EQUAL_SUFFIX_LENGTH) {
            assertEquals(myBoard.charAt(lengthMyBoardDisplayMsg - 1), enemyBoard.charAt(lengthEnemyBoardDisplayMsg - 1),
                    "Expected my board view and enemy board view of newly created board to have same suffixes");
            lengthMyBoardDisplayMsg--;
            lengthEnemyBoardDisplayMsg--;
            i++;
        }
    }

    @Test
    public void testPlaceShipMissingArg() {
        PlaceResponseDTO placeResponse = board.placeShip("K RIGHT 3");
        assertEquals(String.format(MISSING_ARGUMENT, BLUE, DEFAULT), placeResponse.getMsg(),
                "The response should have missing argument message"
        );
    }

    @Test
    public void testPlaceShipInvalidArgs() {
        PlaceResponseDTO placeResponse = board.placeShip("B  2 UP 2");
        assertEquals(INVALID_COORDINATES_PLACE, placeResponse.getMsg(),
                "The response should have invalid coordinates message"
        );
    }

    @Test
    public void testPlaceShipFirstCoordinateOutOfRange() {
        PlaceResponseDTO placeResponse = board.placeShip("K 2 RIGHT 3");
        assertEquals(String.format(OUT_OF_BORDER, BLUE, DEFAULT), placeResponse.getMsg(),
                "The response should have out of border message"
        );
    }

    @Test
    public void testPlaceShipSecondCoordinateOutOfRange() {
        PlaceResponseDTO placeResponse = board.placeShip("A 0 RIGHT 3");
        assertEquals(String.format(OUT_OF_BORDER, BLUE, DEFAULT), placeResponse.getMsg(),
                "The response should have out of border message"
        );
    }

    @Test
    public void testPlaceShipSmallLength() {
        PlaceResponseDTO placeResponse = board.placeShip("F 5 LEFT 1");
        assertEquals(String.format(INVALID_SHIP, BLUE, DEFAULT), placeResponse.getMsg(),
                "The response should have invalid ship message"
        );
    }

    @Test
    public void testPlaceShipLargeLength() {
        PlaceResponseDTO placeResponse = board.placeShip("F 5 LEFT 6");
        assertEquals(String.format(INVALID_SHIP, BLUE, DEFAULT), placeResponse.getMsg(),
                "The response should have invalid ship message"
        );
    }

    @Test
    public void testPlaceTooManyShipsOfOneType() {
        board.placeShip("D 2 down 5");
        PlaceResponseDTO placeResponse = board.placeShip("D 3 down 5");

        assertEquals(SHIPS_TYPE_OVERFLOW, placeResponse.getMsg(),
                "The response should have ships type overflow message");
    }

    @Test
    void testPlaceShipOverflowUp() {
        PlaceResponseDTO placeResponse = board.placeShip("A 3 UP 5");
        assertEquals(OUT_OF_BORDER_UP, placeResponse.getMsg(),
                "The response should have out of border for upper bound message");
    }

    @Test
    void testPlaceShipOverflowRight() {
        PlaceResponseDTO placeResponse = board.placeShip("A 10 RiGhT 5");
        assertEquals(OUT_OF_BORDER_RIGHT, placeResponse.getMsg(),
                "The response should have out of border for right bound message");
    }

    @Test
    void testPlaceShipOverflowDown() {
        PlaceResponseDTO placeResponse = board.placeShip("j 3 down 5");
        assertEquals(OUT_OF_BORDER_DOWN, placeResponse.getMsg(),
                "The response should have out of border for down bound message");
    }

    @Test
    void testPlaceShipOverflowLeft() {
        PlaceResponseDTO placeResponse = board.placeShip("C 4 left 5");
        assertEquals(OUT_OF_BORDER_LEFT, placeResponse.getMsg(),
                "The response should have out of border for left bound message");
    }

    @Test
    public void testPlaceShipInvalidDirection() {
        PlaceResponseDTO placeResponse = board.placeShip("F 3 DIAGONAL 3");
        assertEquals(INVALID_DIRECTION, placeResponse.getMsg(),
                "The response should have invalid ship placement message"
        );
    }

    @Test
    void testPlaceShipOverExistingDestroyer() {
        PlaceResponseDTO placeResponse1 = board.placeShip("B 3 RIGHT 2");
        PlaceResponseDTO placeResponse2 = board.placeShip("B 4 DOWN 4");

        assertEquals(String.format(VALID_PLACE_MSG, BLUE, ShipType.DESTROYER, DEFAULT), placeResponse1.getMsg(),
                "Response 1 valid ship placement message");
        assertEquals(INVALID_PLACEMENT, placeResponse2.getMsg(),
                "Response 2 should have invalid ship placement message");
    }

    @Test
    void testPlaceShipOverExistingCruiser() {
        PlaceResponseDTO placeResponse1 = board.placeShip("b 3 right 3");
        PlaceResponseDTO placeResponse2 = board.placeShip("B 4 DOWN 4");

        assertEquals(String.format(VALID_PLACE_MSG, BLUE, ShipType.CRUISER, DEFAULT), placeResponse1.getMsg(),
                "Response 1 valid ship placement message");
        assertEquals(INVALID_PLACEMENT, placeResponse2.getMsg(),
                "Response 2 should have invalid ship placement message");
    }

    @Test
    void testPlaceShipOverExistingBattleship() {
        PlaceResponseDTO placeResponse1 = board.placeShip("B 3 RIGHT 4");
        PlaceResponseDTO placeResponse2 = board.placeShip("B 4 dOwN 4");

        assertEquals(String.format(VALID_PLACE_MSG, BLUE, ShipType.BATTLESHIP, DEFAULT), placeResponse1.getMsg(),
                "Response 1 valid ship placement message");
        assertEquals(INVALID_PLACEMENT, placeResponse2.getMsg(),
                "Response 2 should have invalid ship placement message");
    }

    @Test
    void testPlaceShipOverExistingCarrierDown() {
        PlaceResponseDTO placeResponse1 = board.placeShip("B 3 RIGHT 5");
        PlaceResponseDTO placeResponse2 = board.placeShip("B 4 DOWN 4");

        assertEquals(String.format(VALID_PLACE_MSG, BLUE, ShipType.CARRIER, DEFAULT), placeResponse1.getMsg(),
                "Response 1 valid ship placement message");
        assertEquals(INVALID_PLACEMENT, placeResponse2.getMsg(),
                "Response 2 should have invalid ship placement message");
    }

    @Test
    void testPlaceShipOverExistingCarrierUp() {
        PlaceResponseDTO placeResponse1 = board.placeShip("B 3 RIGHT 5");
        PlaceResponseDTO placeResponse2 = board.placeShip("B 4 UP 2");

        assertEquals(String.format(VALID_PLACE_MSG, BLUE, ShipType.CARRIER, DEFAULT), placeResponse1.getMsg(),
                "Response 1 valid ship placement message");
        assertEquals(INVALID_PLACEMENT, placeResponse2.getMsg(),
                "Response 2 should have invalid ship placement message");
    }

    @Test
    void testPlaceShipOverExistingCarrierLeft() {
        PlaceResponseDTO placeResponse1 = board.placeShip("B 3 RIGHT 5");
        PlaceResponseDTO placeResponse2 = board.placeShip("B 4 Left 2");

        assertEquals(String.format(VALID_PLACE_MSG, BLUE, ShipType.CARRIER, DEFAULT), placeResponse1.getMsg(),
                "Response 1 valid ship placement message");
        assertEquals(INVALID_PLACEMENT, placeResponse2.getMsg(),
                "Response 2 should have invalid ship placement message");
    }

    @Test
    void testPlaceShipOverExistingCarrierRight() {
        PlaceResponseDTO placeResponse1 = board.placeShip("B 3 RIGHT 5");
        PlaceResponseDTO placeResponse2 = board.placeShip("B 4 rIgHt 2");

        assertEquals(String.format(VALID_PLACE_MSG, BLUE, ShipType.CARRIER, DEFAULT), placeResponse1.getMsg(),
                "Response 1 valid ship placement message");
        assertEquals(INVALID_PLACEMENT, placeResponse2.getMsg(),
                "Response 2 should have invalid ship placement message");
    }

    @Test
    public void testPlaceAllShipsManually() {
        PlaceResponseDTO placeResponse1 = board.placeShip("A 2 LEFT 2");
        assertEquals(String.format(VALID_PLACE_MSG, BLUE, ShipType.DESTROYER, DEFAULT), placeResponse1.getMsg(),
                "Response 1 should have valid ship placement message");

        PlaceResponseDTO placeResponse2 = board.placeShip("A 9 right 2");
        assertEquals(String.format(VALID_PLACE_MSG, BLUE, ShipType.DESTROYER, DEFAULT), placeResponse2.getMsg(),
                "Response 2 should have valid ship placement message");

        PlaceResponseDTO placeResponse3 = board.placeShip("I 1 DoWn 2");
        assertEquals(String.format(VALID_PLACE_MSG, BLUE, ShipType.DESTROYER, DEFAULT), placeResponse3.getMsg(),
                "Response 3 should have valid ship placement message");

        PlaceResponseDTO placeResponse4 = board.placeShip("J 10 UP 2");
        assertEquals(String.format(VALID_PLACE_MSG, BLUE, ShipType.DESTROYER, DEFAULT), placeResponse4.getMsg(),
                "Response 4 should have valid ship placement message");

        PlaceResponseDTO placeResponse5 = board.placeShip("E 3 UP 3");
        assertEquals(String.format(VALID_PLACE_MSG, BLUE, ShipType.CRUISER, DEFAULT), placeResponse5.getMsg(),
                "Response 5 should have valid ship placement message");

        PlaceResponseDTO placeResponse6 = board.placeShip("D 8 RIGHT 3");
        assertEquals(String.format(VALID_PLACE_MSG, BLUE, ShipType.CRUISER, DEFAULT), placeResponse6.getMsg(),
                "Response 6 should have valid ship placement message");

        PlaceResponseDTO placeResponse7 = board.placeShip("F 8 Left 3");
        assertEquals(String.format(VALID_PLACE_MSG, BLUE, ShipType.CRUISER, DEFAULT), placeResponse7.getMsg(),
                "Response 7 should have valid ship placement message");

        PlaceResponseDTO placeResponse8 = board.placeShip("G 7 DOWN 4");
        assertEquals(String.format(VALID_PLACE_MSG, BLUE, ShipType.BATTLESHIP, DEFAULT), placeResponse8.getMsg(),
                "Response 8 should have valid ship placement message");

        PlaceResponseDTO placeResponse9 = board.placeShip("D 6 UP 4");
        assertEquals(String.format(VALID_PLACE_MSG, BLUE, ShipType.BATTLESHIP, DEFAULT), placeResponse9.getMsg(),
                "Response 9 should have valid ship placement message");

        PlaceResponseDTO placeResponse10 = board.placeShip("G 5 LEFT 5");
        assertEquals(String.format(VALID_PLACE_MSG, BLUE, ShipType.CARRIER, DEFAULT), placeResponse10.getMsg(),
                "Response 10 should have valid ship placement message");

        assertEquals(TOTAL_SHIPS_CNT, board.allShipsCount(),
                "Count of all ships should be 10");
        assertTrue(board.allShipsPlaced(), "Expected all ship to be placed");
    }

    @Test
    public void testPlaceAllShipsRandomAutomatically() {
        PlaceResponseDTO placeResponse = board.placeAllShipsRandom();

        assertEquals(READY_TO_ATTACK, placeResponse.getMsg(),
                "The response should have ready to attack message");
        assertEquals(TOTAL_SHIPS_CNT, board.allShipsCount(),
                "Count of all ships should be 10");
        assertTrue(board.allShipsPlaced(), "Expected all ship to be placed");
    }

    @Test
    public void testAttackAndMiss() {
        AttackResponseDTO attackResponse = board.attack("A 2");

        assertEquals(MISS, attackResponse.getMsgAttacker(), "The response should have message for miss-attack");
        assertEquals(String.format(MSG_DEFENDER_INFO, 'A', '2'), attackResponse.getMsgDefender(),
                "The response should have message about the attacker's last attacked position");

        assertTrue(attackResponse.getValidAttack(), "The response should indicate for a valid attack");
        assertFalse(attackResponse.getHit(), "The response should indicate for a miss-attack");
        assertFalse(attackResponse.getDestroyed(), "The response should not indicate for a destroy-attack");
    }

    @Test
    public void testAttackAndHit() {
        board.placeShip("B 3 DOWN 3");
        AttackResponseDTO attackResponse = board.attack("C 3");

        assertEquals(String.format(HIT, ShipType.CRUISER) + "C3", attackResponse.getMsgAttacker(),
                "The response should have hit-attack message");

        assertEquals(String.format(MSG_DEFENDER_INFO, 'C', '3'), attackResponse.getMsgDefender(),
                "The response should have info message for other player");

        assertTrue(attackResponse.getValidAttack(), "The response should indicate for a valid attack");
        assertTrue(attackResponse.getHit(), "The response should indicate for a miss-attack");
        assertFalse(attackResponse.getDestroyed(), "The response should not indicate for a destroy-attack");
    }

    @Test
    public void testAttackAndDestroy() {
        board.placeShip("B 3 UP 2");
        board.attack("B 3");
        AttackResponseDTO attackResponse = board.attack("A 3");

        assertEquals(String.format(DESTROY, ShipType.DESTROYER, RED, DEFAULT) + "A3",
                attackResponse.getMsgAttacker(), "The response should have destroy-attack message");

        assertEquals(String.format(MSG_DEFENDER_INFO, 'A', '3'), attackResponse.getMsgDefender(),
                "The response should have info message for other player");

        assertTrue(attackResponse.getValidAttack(), "The response should indicate for a valid attack");
        assertTrue(attackResponse.getHit(), "The response should indicate for a miss-attack");
        assertTrue(attackResponse.getDestroyed(), "The response should indicate for a destroy-attack");
    }

    @Test
    public void testAttackAlreadyMissed() {
        board.attack("I 6");
        AttackResponseDTO attackResponse = board.attack("I 6");

        assertEquals(ATTACK_MISSED, attackResponse.getMsgAttacker(),
                "The response should have already-missed message");
        assertEquals(String.format(MSG_DEFENDER_INFO, 'I', '6'), attackResponse.getMsgDefender(),
                "The response should have info message for other player");

        assertTrue(attackResponse.getValidAttack(), "The response should indicate for a valid attack");
        assertFalse(attackResponse.getHit(), "The response should not indicate for a miss-attack");
        assertFalse(attackResponse.getDestroyed(), "The response should not indicate for a destroy-attack");
    }

    @Test
    public void testAttackAlreadyHit() {
        board.placeShip("F 3 DOWN 2");
        board.attack("F 3");
        AttackResponseDTO attackResponse = board.attack("F 3");

        assertEquals(String.format(ATTACK_HIT, ShipType.DESTROYER),
                attackResponse.getMsgAttacker(), "The response should have destroyed ship message");

        assertEquals(String.format(MSG_DEFENDER_INFO, 'F', '3'), attackResponse.getMsgDefender(),
                "The response should have info message for other player");

        assertTrue(attackResponse.getValidAttack(), "The response should indicate for a valid attack");
        assertFalse(attackResponse.getHit(), "The response should not indicate for a miss-attack");
        assertFalse(attackResponse.getDestroyed(), "The response should not indicate for a destroy-attack");
    }

    @Test
    public void testAttackAlreadyDestroyed() {
        board.placeShip("F 3 DOWN 2");
        board.attack("F 3");
        board.attack("G 3");
        AttackResponseDTO attackResponse = board.attack("G 3");

        assertEquals(String.format(ATTACK_DESTROYED, ShipType.DESTROYER),
                attackResponse.getMsgAttacker(), "The response should have destroyed ship message");

        assertEquals(String.format(MSG_DEFENDER_INFO, 'G', '3'), attackResponse.getMsgDefender(),
                "The response should have info message for other player");

        assertTrue(attackResponse.getValidAttack(), "The response should indicate for a valid attack");
        assertFalse(attackResponse.getHit(), "The response should not indicate for a miss-attack");
        assertFalse(attackResponse.getDestroyed(), "The response should not indicate for a destroy-attack");
    }

    @Test
    public void testAttackInvalidCoordinates() {
        AttackResponseDTO attackResponse = board.attack("A B");

        assertEquals(INVALID_COORDINATES_ATTACK,
                attackResponse.getMsgAttacker(), "The response should have invalid coordinates message");
    }

    @Test
    public void testGetDestroyedShips() {
        board.placeAllShipsRandom();

        for (char c = 'A'; c <= 'J'; c++) {
            for (int i = 1; i <= BOARD_SIZE; i++) {
                board.attack(c + " " + i);
            }
        }

        assertTrue(board.playerLost());
    }

    @Test
    public void testHacksAttackAll() {
        board.placeShip("A 1 right 2");
        board.placeShip("A 5 RIGHT 3");
        board.placeShip("b 4 Right 4");
        board.placeShip("J 5 right 5");

        AttackResponseDTO attackResponse = board.attackAll('A', 'B', 'J');

        assertEquals(String.format(MSG_ATTACKER_MASSIVE_ATTACK, 14, 4), attackResponse.getMsgAttacker(),
                "Response should have statistics message of the attack: 14 hits and 4 destroys");

        assertEquals(String.format(MSG_DEFENDER_MASSIVE_ATTACK, 14, 4), attackResponse.getMsgDefender(),
                "Response should have defender message with statistics of damages: 14 hits and 4 destroys");

        assertTrue(attackResponse.getValidAttack(), "The response should indicate for a valid attack");
        assertTrue(attackResponse.getHit(), "The response should indicate for a miss-attack");
        assertTrue(attackResponse.getDestroyed(), "The response should indicate for a destroy-attack");
    }

    @Test
    public void testGetAllShipsGetParticularShipType() {
        assertEquals(0, board.getAllShips().get(5).size(), "Expected no CARRIER ships");

        board.placeShip("E 2 UP 5");

        assertEquals(1, board.getAllShips().get(5).size(), "Expected one CARRIER ship");
    }

    @Test
    public void testGetAllShipsCount() {
        board.placeAllShipsRandom();
        assertEquals(TOTAL_SHIPS_CNT, board.allShipsCount(), "Expected all ships to be placed");
    }
}
