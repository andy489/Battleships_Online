package bg.sofia.uni.fmi.mjt.battleships.online.server.command.mode.playing;

import bg.sofia.uni.fmi.mjt.battleships.online.server.command.response.CommandResponseDTO;
import bg.sofia.uni.fmi.mjt.battleships.online.server.game.Game;
import bg.sofia.uni.fmi.mjt.battleships.online.server.game.GameStatus;
import bg.sofia.uni.fmi.mjt.battleships.online.server.game.player.Player;
import bg.sofia.uni.fmi.mjt.battleships.online.server.game.player.PlayerStatus;
import bg.sofia.uni.fmi.mjt.battleships.online.server.game.player.board.Board;
import bg.sofia.uni.fmi.mjt.battleships.online.server.storage.Storage;
import com.google.gson.Gson;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;


import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
public class SaveGameTest {
    private static final String LOGIN_OR_REGISTER = "You must login or register first";
    private static final String NOT_IN_GAME = "No current game";
    private static final String GAME_SAVED = "Game \"%s\" saved";
    private static final String PENDING_GAME_SAVE = "Cannot save pending game";
    private static final String SAVE_GAME_EXISTING_NAME = "Cannot save game. Game with name \"%s\" already saved";


    private final String hostId = "host-id";
    private final String hostApiKey = "host-api-key";

    private final Gson gson = new Gson();

    @Mock
    private Storage storageMock;

    @Mock
    private Game gameMock;

    @Mock
    private Player hostPlayerMock;

    @Mock
    private Player guestPlayerMock;

    @Test
    public void testSaveGameNotLoggedIn() {
        CommandResponseDTO response = SaveGame.execute(storageMock, hostId, gson);

        assertEquals(LOGIN_OR_REGISTER, response.getMsgThis(),
                "Response should contain not logged in message");
    }

    @Test
    public void testSaveGameNotPlaying() {
        when(storageMock.getApiKey(hostId)).thenReturn(hostApiKey);
        when(storageMock.getPlayerStatus(hostId)).thenReturn(PlayerStatus.ONLINE);
        CommandResponseDTO response = SaveGame.execute(storageMock, hostId, gson);

        assertEquals(NOT_IN_GAME, response.getMsgThis(), "Response should contain not playing message");
    }

    @Test
    public void testSaveGamePending() {
        when(storageMock.getApiKey(hostId)).thenReturn(hostApiKey);
        when(storageMock.getPlayerStatus(hostId)).thenReturn(PlayerStatus.HOST);
        when(storageMock.getCurrentGame(hostId)).thenReturn(gameMock);
        when(gameMock.getStatus()).thenReturn(GameStatus.PENDING);

        CommandResponseDTO response = SaveGame.execute(storageMock, hostId, gson);

        assertEquals(PENDING_GAME_SAVE, response.getMsgThis(),
                "Response should contain permissions denied for saving pending game message");
    }

    @Test
    public void testSaveGameAlreadyHaveSavedGameWithThisName() {
        when(storageMock.getApiKey(hostId)).thenReturn(hostApiKey);
        when(storageMock.getPlayerStatus(hostId)).thenReturn(PlayerStatus.HOST);
        when(storageMock.getCurrentGame(hostId)).thenReturn(gameMock);
        when(gameMock.getStatus()).thenReturn(GameStatus.IN_PROGRESS);

        when(gameMock.getGameName()).thenReturn("my-game");

        when(storageMock.containsGameName(hostApiKey, "my-game")).thenReturn(true);

        CommandResponseDTO response = SaveGame.execute(storageMock, hostId, gson);

        assertEquals(String.format(SAVE_GAME_EXISTING_NAME, "my-game"), response.getMsgThis(),
                "Response should contain message indicating already saved game with that name");
    }

    @Test
    public void testSaveGame() {
        when(storageMock.getApiKey(hostId)).thenReturn(hostApiKey);
        when(storageMock.getPlayerStatus(hostId)).thenReturn(PlayerStatus.HOST);
        when(storageMock.getCurrentGame(hostId)).thenReturn(gameMock);
        when(gameMock.getStatus()).thenReturn(GameStatus.IN_PROGRESS);

        when(gameMock.getHostPlayer()).thenReturn(hostPlayerMock);
        when(gameMock.getGuestPlayer()).thenReturn(guestPlayerMock);

        Board hostBoardStub = new Board();
        Board guestBoardStub = new Board();

        hostBoardStub.attack("A 1");
        guestBoardStub.attack("J 10");

        when(hostPlayerMock.board()).thenReturn(hostBoardStub);
        when(hostPlayerMock.board()).thenReturn(guestBoardStub);

        when(hostPlayerMock.id()).thenReturn(hostId);
        when(gameMock.getGameName()).thenReturn("my-game");

        CommandResponseDTO response = SaveGame.execute(storageMock, hostId, gson);

        assertEquals(String.format(GAME_SAVED, "my-game"), response.getMsgThis(),
                "Response should contain game saved message");
    }
}
