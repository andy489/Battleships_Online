package bg.sofia.uni.fmi.mjt.battleships.online.server.command.response;

public class AttackResponseDTO {
    private final boolean hit;
    private final boolean destroyed;

    private String msgAttacker;
    private String msgDefender;

    private final boolean validAttack;
    private boolean endGame;

    public AttackResponseDTO(boolean hit,
                             boolean destroyed,
                             String msgAttacker,
                             String msgDefender,
                             boolean validAttack
    ) {
        this.hit = hit;
        this.destroyed = destroyed;

        this.msgAttacker = msgAttacker;
        this.msgDefender = msgDefender;

        this.validAttack = validAttack;
        this.endGame = false;
    }

    public boolean getHit() {
        return hit;
    }

    public boolean getDestroyed() {
        return destroyed;
    }

    public String getMsgAttacker() {
        return msgAttacker;
    }

    public String getMsgDefender() {
        return msgDefender;
    }

    public boolean getValidAttack() {
        return validAttack;
    }

    public void setMsgDefender(String msgDefender) {
        this.msgDefender = msgDefender;
    }

    public void setMsgAttacker(String msgAttacker) {
        this.msgAttacker = msgAttacker;
    }

    public boolean checkEndGame() {
        return endGame;
    }

    public void endGame() {
        endGame = true;
    }
}
