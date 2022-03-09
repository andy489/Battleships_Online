package bg.sofia.uni.fmi.mjt.battleships.online.server.storage;

import bg.sofia.uni.fmi.mjt.battleships.online.server.game.Game;
import bg.sofia.uni.fmi.mjt.battleships.online.server.game.SavedGame;
import bg.sofia.uni.fmi.mjt.battleships.online.server.game.player.PlayerStatus;

import java.util.Collection;
import java.util.HashMap;
import java.util.Map;
import java.util.Objects;
import java.util.stream.Stream;

public class Storage {
    private final Map<String, UserIn> socketAddressUserIn;
    private final Map<String, Map<String, SavedGame>> apiKeySavedGames;

    {
        socketAddressUserIn = new HashMap<>();
        apiKeySavedGames = new HashMap<>();
    }

    public Storage() {
    }

    public Map<String, Map<String, SavedGame>> getApiKeySavedGames() {
        return apiKeySavedGames;
    }

    public PlayerStatus getPlayerStatus(String id) {
        return socketAddressUserIn.get(id).getStatus();
    }

    public void setPlayerStatus(String id, PlayerStatus playerStatus) {
        socketAddressUserIn.get(id).setStatus(playerStatus);
    }

    public String getUsername(String id) {
        return socketAddressUserIn.get(id).getUsername();
    }

    public boolean isUserConnected(String id) {
        return socketAddressUserIn.containsKey(id);
    }

    public Stream<UserIn> getUsersInAsStream() {
        return socketAddressUserIn.values().stream();
    }

    public void addNewUserIn(String id, UserIn userIn) {
        socketAddressUserIn.put(id, userIn);
    }

    public void changeUsername(String id, String newUsername) {
        socketAddressUserIn.get(id).setUsername(newUsername);
    }

    public String getApiKey(String id) {
        return socketAddressUserIn.get(id).getApiKey();
    }

    public void setApiKey(String id, String apiKey) {
        socketAddressUserIn.get(id).setApiKey(apiKey);
    }

    public void initSavedGamesCollection(String apiKey) {
        apiKeySavedGames.put(apiKey, new HashMap<>());
    }

    public boolean containsSavedGamesForApiKey(String apiKey) {
        return apiKeySavedGames.containsKey(apiKey);
    }

    public boolean checkForExistingGameName(String gameName) {
        Collection<Game> games = getUsersInAsStream().map(UserIn::getCurrGame).filter(Objects::nonNull).toList();

        for (Game game : games)
            if (game.getGameName().equals(gameName)) {
                return true;
            }

        return false;
    }

    public void setCurrentGame(String id, Game currentGame) {
        socketAddressUserIn.get(id).setCurrGame(currentGame);
    }

    public UserIn getUserIn(String id) {
        return socketAddressUserIn.get(id);
    }

    public void removeUserIn(String id) {
        socketAddressUserIn.remove(id);
    }

    public Stream<SavedGame> getSavedGamesAsStream(String apiKey) {
        return apiKeySavedGames.get(apiKey).values().stream();
    }

    public SavedGame getSavedGame(String apiKey, String gameName) {
        return apiKeySavedGames.get(apiKey).get(gameName);
    }

    public void addSavedGame(String apiKey, SavedGame savedGame) {
        apiKeySavedGames.get(apiKey).put(savedGame.getName(), savedGame);
    }

    public void removeSavedGame(String apiKey, String gameName) {
        apiKeySavedGames.get(apiKey).remove(gameName);
    }

    public Game getCurrentGame(String id) {
        return socketAddressUserIn.get(id).getCurrGame();
    }

    public Map<String, SavedGame> getGamesToSave(String apiKey) {
        return apiKeySavedGames.get(apiKey);
    }

    public boolean containsGameName(String apiKey, String gameName) {
        return apiKeySavedGames.get(apiKey).containsKey(gameName);
    }

    public void initSavedGamesAndRegisteredUsers(String apiKey, Map<String, SavedGame> savedGames) {
        apiKeySavedGames.put(apiKey, savedGames);
    }
}
