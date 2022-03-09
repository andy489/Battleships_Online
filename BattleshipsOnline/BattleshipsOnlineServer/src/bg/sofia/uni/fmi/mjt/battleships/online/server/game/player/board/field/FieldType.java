package bg.sofia.uni.fmi.mjt.battleships.online.server.game.player.board.field;

public enum FieldType {
    EMPTY("_"),
    SHIP("X"),
    MISS("-"),
    HIT("*");

    private final String symbol;

    FieldType(String symbol) {
        this.symbol = symbol;
    }

    @Override
    public String toString() {
        return symbol;
    }
}
