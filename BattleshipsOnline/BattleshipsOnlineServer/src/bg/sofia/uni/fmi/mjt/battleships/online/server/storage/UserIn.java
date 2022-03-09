package bg.sofia.uni.fmi.mjt.battleships.online.server.storage;

import bg.sofia.uni.fmi.mjt.battleships.online.server.game.Game;
import bg.sofia.uni.fmi.mjt.battleships.online.server.game.player.PlayerStatus;

public class UserIn {
    private String username;
    private PlayerStatus status;
    private String apiKey;
    private Game currGame;

    public UserIn(String username) {
        this.username = username;
        status = PlayerStatus.ONLINE;
        currGame = null;
        apiKey = null;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getUsername() {
        return username;
    }

    public void setStatus(PlayerStatus status) {
        this.status = status;
    }

    public PlayerStatus getStatus() {
        return status;
    }

    public void setApiKey(String apiKey) {
        this.apiKey = apiKey;
    }

    public String getApiKey() {
        return apiKey;
    }

    public void setCurrGame(Game currGame) {
        this.currGame = currGame;
    }

    public Game getCurrGame() {
        return currGame;
    }

    public void removeCurrentGame() {
        currGame = null;
    }
}
