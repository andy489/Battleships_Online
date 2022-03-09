package bg.sofia.uni.fmi.mjt.battleships.online.server.game.player.board.field.ship;

import bg.sofia.uni.fmi.mjt.battleships.online.server.command.response.AttackResponseDTO;
import bg.sofia.uni.fmi.mjt.battleships.online.server.game.player.board.field.ship.coordinate.Coordinate;

import java.util.ArrayList;
import java.util.List;

import static bg.sofia.uni.fmi.mjt.battleships.online.server.command.manager.CommandExecutor.DEFAULT;
import static bg.sofia.uni.fmi.mjt.battleships.online.server.command.manager.CommandExecutor.RED;
import static bg.sofia.uni.fmi.mjt.battleships.online.server.game.player.board.field.ship.ShipConstants.BATTLESHIP;
import static bg.sofia.uni.fmi.mjt.battleships.online.server.game.player.board.field.ship.ShipConstants.CARRIER;
import static bg.sofia.uni.fmi.mjt.battleships.online.server.game.player.board.field.ship.ShipConstants.CRUISER;
import static bg.sofia.uni.fmi.mjt.battleships.online.server.game.player.board.field.ship.ShipConstants.DESTROY;
import static bg.sofia.uni.fmi.mjt.battleships.online.server.game.player.board.field.ship.ShipConstants.DESTROYER;
import static bg.sofia.uni.fmi.mjt.battleships.online.server.game.player.board.field.ship.ShipConstants.HIT;
import static bg.sofia.uni.fmi.mjt.battleships.online.server.game.player.board.field.ship.ShipConstants.INVALID_SHIP;

public class Ship {
    private final ShipType shipType;
    private int shipDeck;

    private final List<Coordinate> location;

    {
        location = new ArrayList<>();
    }

    public Ship(int len) {
        shipDeck = len;

        shipType = switch (len) {
            case DESTROYER -> ShipType.DESTROYER;
            case CRUISER -> ShipType.CRUISER;
            case BATTLESHIP -> ShipType.BATTLESHIP;
            case CARRIER -> ShipType.CARRIER;
            default -> throw new IllegalArgumentException(INVALID_SHIP);
        };
    }

    public ShipType getShipType() {
        return shipType;
    }

    public boolean isSunk() {
        return shipDeck == 0;
    }

    public AttackResponseDTO attack() {
        if (shipDeck > 0) {
            shipDeck--;
        }

        if (isSunk()) {
            return new AttackResponseDTO(
                    true,
                    true,
                    String.format(DESTROY, this.shipType, RED, DEFAULT),
                    null,
                    true
            );
        } else {
            return new AttackResponseDTO(
                    true,
                    false,
                    String.format(HIT, this.shipType),
                    null,
                    true
            );
        }
    }

    public List<Coordinate> getLocation() {
        return location;
    }

    public void addCoordinateToLocation(int i, int c) {
        location.add(new Coordinate(i, c));
    }
}
