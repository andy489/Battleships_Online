package bg.sofia.uni.fmi.mjt.battleships.online.server.game.player.board.field.ship.coordinate;

public class Coordinate {
    private final int c;
    private final int i;

    public Coordinate(int c, int i) {
        this.c = c;
        this.i = i;
    }

    public int getC() {
        return c;
    }

    public int getI() {
        return i;
    }
}
