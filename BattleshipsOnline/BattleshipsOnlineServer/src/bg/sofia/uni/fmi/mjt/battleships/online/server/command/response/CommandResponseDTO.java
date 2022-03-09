package bg.sofia.uni.fmi.mjt.battleships.online.server.command.response;

import java.nio.channels.SocketChannel;

public class CommandResponseDTO {
    String msgThis;
    String msgOther;
    SocketChannel otherSocketChannel;

    public CommandResponseDTO(String msgThis) {
        this.msgThis = msgThis;
        msgOther = null;
        otherSocketChannel = null;
    }

    public CommandResponseDTO(String msgThis, String msgOther, SocketChannel hostSocket) {
        this.msgThis = msgThis;
        this.msgOther = msgOther;
        this.otherSocketChannel = hostSocket;
    }

    public String getMsgThis() {
        return msgThis;
    }

    public String getMsgOther() {
        return msgOther;
    }

    public SocketChannel getHostSocketChannel() {
        return otherSocketChannel;
    }
}
