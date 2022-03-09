package bg.sofia.uni.fmi.mjt.battleships.online.server.game.player.board.field;

import bg.sofia.uni.fmi.mjt.battleships.online.server.command.response.AttackResponseDTO;
import bg.sofia.uni.fmi.mjt.battleships.online.server.game.player.board.field.ship.Ship;

import static bg.sofia.uni.fmi.mjt.battleships.online.server.game.player.board.field.FieldConstants.ATTACK_DESTROYED;
import static bg.sofia.uni.fmi.mjt.battleships.online.server.game.player.board.field.FieldConstants.ATTACK_HIT;
import static bg.sofia.uni.fmi.mjt.battleships.online.server.game.player.board.field.FieldConstants.ATTACK_MISSED;
import static bg.sofia.uni.fmi.mjt.battleships.online.server.game.player.board.field.FieldConstants.MISS;

public class Field {
    private FieldType fieldType;
    private Ship ship;

    public Field() {
        fieldType = FieldType.EMPTY;
        ship = null;
    }

    public FieldType getFieldType() {
        return fieldType;
    }

    public void setFieldType(FieldType fieldType) {
        this.fieldType = fieldType;
    }

    public void setFieldPointer(Ship ship) {
        setFieldType(FieldType.SHIP);
        this.ship = ship;
    }

    public AttackResponseDTO attack() {
        if (fieldType == FieldType.MISS) {
            return new AttackResponseDTO(
                    false,
                    false,
                    ATTACK_MISSED,
                    null,
                    true
            );
        }

        if (fieldType == FieldType.HIT) {
            if (ship.isSunk()) {
                return new AttackResponseDTO(
                        false,
                        false,
                        String.format(ATTACK_DESTROYED, ship.getShipType()),
                        null,
                        true
                );
            } else {
                return new AttackResponseDTO(
                        false,
                        false,
                        String.format(ATTACK_HIT, ship.getShipType()),
                        null,
                        true
                );
            }
        }

        if (ship == null) {
            fieldType = FieldType.MISS;
            return new AttackResponseDTO(false,
                    false,
                    MISS,
                    null,
                    true
            );
        }

        fieldType = FieldType.HIT;
        return ship.attack();
    }

    public void setShip(Ship ship) {
        this.ship = ship;
    }
}
