package bg.sofia.uni.fmi.mjt.battleships.online.server.game.player.board.field.ship;

import bg.sofia.uni.fmi.mjt.battleships.online.server.command.response.AttackResponseDTO;
import org.junit.jupiter.api.Test;

import static bg.sofia.uni.fmi.mjt.battleships.online.server.command.manager.CommandExecutor.DEFAULT;
import static bg.sofia.uni.fmi.mjt.battleships.online.server.command.manager.CommandExecutor.RED;
import static bg.sofia.uni.fmi.mjt.battleships.online.server.game.player.board.BoardConstants.BATTLESHIP_LENGTH;
import static bg.sofia.uni.fmi.mjt.battleships.online.server.game.player.board.BoardConstants.CARRIER_LENGTH;
import static bg.sofia.uni.fmi.mjt.battleships.online.server.game.player.board.BoardConstants.CRUISER_LENGTH;
import static bg.sofia.uni.fmi.mjt.battleships.online.server.game.player.board.BoardConstants.DESTROYER_LENGTH;
import static bg.sofia.uni.fmi.mjt.battleships.online.server.game.player.board.field.ship.ShipConstants.DESTROY;
import static bg.sofia.uni.fmi.mjt.battleships.online.server.game.player.board.field.ship.ShipConstants.HIT;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

public class ShipTest {
    @Test
    public void testGetShipTypeDestroyer() {
        Ship destroyerShip = new Ship(DESTROYER_LENGTH);
        assertEquals(ShipType.DESTROYER, destroyerShip.getShipType());
    }

    @Test
    public void testGetShipTypeCruiser() {
        Ship cruiserShip = new Ship(CRUISER_LENGTH);
        assertEquals(ShipType.CRUISER, cruiserShip.getShipType());
    }

    @Test
    public void testGetShipTypeBattleship() {
        Ship battleShip = new Ship(BATTLESHIP_LENGTH);
        assertEquals(ShipType.BATTLESHIP, battleShip.getShipType());
    }

    @Test
    public void testGetShipTypeCarrier() {
        Ship carrierShip = new Ship(CARRIER_LENGTH);
        assertEquals(ShipType.CARRIER, carrierShip.getShipType());
    }

    @Test
    public void testIsSunkDestroyer() {
        Ship destroyerShip = new Ship(DESTROYER_LENGTH);

        for (int i = 0; i < DESTROYER_LENGTH; i++) {
            assertFalse(destroyerShip.isSunk());
            destroyerShip.attack();
        }

        assertTrue(destroyerShip.isSunk());

        destroyerShip.attack();
        assertTrue(destroyerShip.isSunk());
    }

    @Test
    public void testIsSunkCruiser() {
        Ship cruiserShip = new Ship(CRUISER_LENGTH);

        for (int i = 0; i < CRUISER_LENGTH; i++) {
            assertFalse(cruiserShip.isSunk());
            cruiserShip.attack();
        }

        assertTrue(cruiserShip.isSunk());

        cruiserShip.attack();
        assertTrue(cruiserShip.isSunk());
    }

    @Test
    public void testIsSunkBattleship() {
        Ship battleShip = new Ship(BATTLESHIP_LENGTH);

        for (int i = 0; i < BATTLESHIP_LENGTH; i++) {
            assertFalse(battleShip.isSunk());
            battleShip.attack();
        }

        assertTrue(battleShip.isSunk());

        battleShip.attack();
        assertTrue(battleShip.isSunk());
    }

    @Test
    public void testIsSunkCarrier() {
        Ship carrierShip = new Ship(CARRIER_LENGTH);

        for (int i = 0; i < CARRIER_LENGTH; i++) {
            assertFalse(carrierShip.isSunk());
            carrierShip.attack();
        }

        assertTrue(carrierShip.isSunk());

        carrierShip.attack();
        assertTrue(carrierShip.isSunk());
    }

    @Test
    public void testAttackDestroyerResponseDTO() {
        Ship destroyerShip = new Ship(DESTROYER_LENGTH);

        AttackResponseDTO response = destroyerShip.attack();

        assertEquals(String.format(HIT, ShipType.DESTROYER), response.getMsgAttacker(),
                "Destroyer type ship hit message should be returned");
        assertTrue(response.getHit(), "Destroyer type ship should be hit");
        assertFalse(response.getDestroyed(), "Destroyer type ship should not be destroyed, yet");

        response = destroyerShip.attack();

        assertEquals(String.format(DESTROY, ShipType.DESTROYER, RED, DEFAULT), response.getMsgAttacker(),
                "Destroyer type ship destroyed message should be returned");
        assertTrue(response.getHit(), "Destroyer type ship should be hit");
        assertTrue(response.getDestroyed(), "Destroyer type ship should be destroyed");
    }

    @Test
    public void testShipCoordinates() {
        Ship destroyerShip = new Ship(DESTROYER_LENGTH);

        assertTrue(destroyerShip.getLocation().isEmpty(), "Location of new ship should be empty");

        destroyerShip.addCoordinateToLocation(0, 0);
        destroyerShip.addCoordinateToLocation(0, 1);

        assertEquals(DESTROYER_LENGTH, destroyerShip.getLocation().size(),
                "Location of destroyer ship ship should have coordinates of two cells");

        assertEquals(0, destroyerShip.getLocation().get(0).getC(),
                "Y (char) coordinate of first cell of the location should be 0");
        assertEquals(0, destroyerShip.getLocation().get(0).getI(),
                "X (integer) coordinate of first cell of the location should be 0");

        assertEquals(0, destroyerShip.getLocation().get(1).getC(),
                "X (integer) coordinate of first cell of the location should be 0");
        assertEquals(1, destroyerShip.getLocation().get(1).getI(),
                "X (integer) coordinate of first cell of the location should be 1");
    }

    @Test
    public void testInvalidShipBuild() {
        assertThrows(IllegalArgumentException.class, () -> new Ship(0),
                "Should throw illegal arguments exception for new ship with length 0");
    }
}
