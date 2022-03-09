package bg.sofia.uni.fmi.mjt.battleships.online.server.command.response;

public class MessageJson {
    private final String msg;

    public MessageJson(String msg) {
        this.msg = msg;
    }

    public String getMsg() {
        return msg;
    }
}
