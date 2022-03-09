package bg.sofia.uni.fmi.mjt.battleships.online.server.game.player.board.field.ship;

public enum ShipDirection {
    UP,
    RIGHT,
    DOWN,
    LEFT,
    NOT_VALID;

    public static ShipDirection of(String direction) {
        return switch (direction.toLowerCase()) {
            case "up" -> ShipDirection.UP;
            case "right" -> ShipDirection.RIGHT;
            case "down" -> ShipDirection.DOWN;
            case "left" -> ShipDirection.LEFT;

            default -> ShipDirection.NOT_VALID;
        };
    }
}
