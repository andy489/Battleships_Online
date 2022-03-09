package bg.sofia.uni.fmi.mjt.battleships.online.server.command.response;

public class PlaceResponseDTO {
    private final String msg;

    public PlaceResponseDTO(String msg) {
        this.msg = msg;
    }

    public String getMsg() {
        return msg;
    }
}
