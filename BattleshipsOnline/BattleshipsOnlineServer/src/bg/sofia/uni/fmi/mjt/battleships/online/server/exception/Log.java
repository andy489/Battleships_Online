package bg.sofia.uni.fmi.mjt.battleships.online.server.exception;

import java.time.LocalDateTime;

public record Log(String userId, LocalDateTime timestamp, String msg) {
    @Override
    public String toString() {
        return String.format("%s|%s|%s%n", userId, timestamp, msg);
    }
}
