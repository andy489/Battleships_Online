package bg.sofia.uni.fmi.mjt.battleships.online.server.command.mode.playing.attack;

import bg.sofia.uni.fmi.mjt.battleships.online.server.command.response.AttackResponseDTO;
import bg.sofia.uni.fmi.mjt.battleships.online.server.command.response.CommandResponseDTO;
import bg.sofia.uni.fmi.mjt.battleships.online.server.game.Game;
import bg.sofia.uni.fmi.mjt.battleships.online.server.game.GameStatus;
import bg.sofia.uni.fmi.mjt.battleships.online.server.game.player.Player;
import bg.sofia.uni.fmi.mjt.battleships.online.server.game.player.PlayerStatus;
import bg.sofia.uni.fmi.mjt.battleships.online.server.game.player.board.Board;
import bg.sofia.uni.fmi.mjt.battleships.online.server.storage.Storage;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.nio.channels.SocketChannel;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
public class AttackAllTest {
    private static final String ATTACK_ALL_USAGE = "Not enough arguments. Usage: attack-all <[A-J]>";
    private static final String INVALID_ATTACK_ALL_USAGE = "Invalid argument. Usage: attack-all <[A-J]>";
    private static final String NOT_IN_GAME = "No current game";
    private static final String SINGLE_PLAYER_MODE_MSG = "Cannot start a single player mode";

    private static final String PLAYER_NOT_PRESSED_START = "One of the players did not press start";
    private static final String NOT_YOUR_TURN = "It's not your turn";

    private final String hostId = "host-id";

    @Mock
    private Storage storageMock;

    @Mock
    private Game gameMock;

    @Mock
    private Player hostPlayerMock;

    @Mock
    private Player guestPlayerMock;

    @Mock
    private SocketChannel guestPlayerSocketChannelMock;

    @Mock
    private Board guestPlayerBoard;

    @Mock
    private AttackResponseDTO attackResponseDTOMock;

    @Test
    public void testAttackAllNoArguments() {
        CommandResponseDTO response = AttackAll.execute(storageMock, hostId);

        assertEquals(ATTACK_ALL_USAGE, response.getMsgThis(),
                "Response should contain attack-all usage message");
    }

    @Test
    public void testAttackAllWhenNotInGame() {
        when(storageMock.getPlayerStatus(hostId)).thenReturn(PlayerStatus.ONLINE);
        CommandResponseDTO response = AttackAll.execute(storageMock, hostId, "A", "B", "C");

        assertEquals(NOT_IN_GAME, response.getMsgThis(), "Response should contain not in game message");
    }

    @Test
    public void testAttackAllGameNotInProgress() {
        when(storageMock.getPlayerStatus(hostId)).thenReturn(PlayerStatus.HOST);
        when(storageMock.getCurrentGame(hostId)).thenReturn(gameMock);
        when(gameMock.getStatus()).thenReturn(GameStatus.PENDING);

        CommandResponseDTO response = AttackAll.execute(storageMock, hostId, "A", "B", "C");

        assertEquals(SINGLE_PLAYER_MODE_MSG, response.getMsgThis(),
                "Response should contain not in game message");
    }

    @Test
    public void testAttackAllGameNotReady() {
        when(storageMock.getPlayerStatus(hostId)).thenReturn(PlayerStatus.HOST);
        when(storageMock.getCurrentGame(hostId)).thenReturn(gameMock);
        when(gameMock.getStatus()).thenReturn(GameStatus.IN_PROGRESS);
        when(gameMock.notReadyToStart()).thenReturn(true);

        CommandResponseDTO response = AttackAll.execute(storageMock, hostId, "H", "I", "J");

        assertEquals(PLAYER_NOT_PRESSED_START, response.getMsgThis(),
                "Response should contain one of the players not ready to start message");
    }

    @Test
    public void testAttackAllInvalidArgument() {
        when(storageMock.getPlayerStatus(hostId)).thenReturn(PlayerStatus.HOST);
        when(storageMock.getCurrentGame(hostId)).thenReturn(gameMock);
        when(gameMock.getStatus()).thenReturn(GameStatus.IN_PROGRESS);
        when(gameMock.notReadyToStart()).thenReturn(false);

        CommandResponseDTO response = AttackAll.execute(storageMock, hostId, "_");

        assertEquals(INVALID_ATTACK_ALL_USAGE, response.getMsgThis(),
                "Response should contain invalid argument message and command usage message");
    }

    @Test
    public void testAttackAllHostNotYourTurn() {
        when(storageMock.getPlayerStatus(hostId)).thenReturn(PlayerStatus.HOST);
        when(storageMock.getCurrentGame(hostId)).thenReturn(gameMock);
        when(gameMock.getStatus()).thenReturn(GameStatus.IN_PROGRESS);
        when(gameMock.notReadyToStart()).thenReturn(false);
        when(gameMock.getHostPlayer()).thenReturn(hostPlayerMock);
        when(hostPlayerMock.id()).thenReturn(hostId);
        when(gameMock.getHostTurn()).thenReturn(false);

        CommandResponseDTO response = AttackAll.execute(storageMock, hostId, "A");

        assertEquals(NOT_YOUR_TURN, response.getMsgThis(),
                "Response should contain one of the players not ready to start message");
    }

    @Test
    public void testAttackAllGuestNotYourTurn() {
        when(storageMock.getPlayerStatus(hostId)).thenReturn(PlayerStatus.HOST);
        when(storageMock.getCurrentGame(hostId)).thenReturn(gameMock);
        when(gameMock.getStatus()).thenReturn(GameStatus.IN_PROGRESS);
        when(gameMock.notReadyToStart()).thenReturn(false);
        when(gameMock.getHostPlayer()).thenReturn(hostPlayerMock);
        String guestId = "guest-id";
        when(hostPlayerMock.id()).thenReturn(guestId);
        when(gameMock.getHostTurn()).thenReturn(true);

        CommandResponseDTO response = AttackAll.execute(storageMock, hostId, "A");

        assertEquals(NOT_YOUR_TURN, response.getMsgThis(),
                "Response should contain one of the players not ready to start message");
    }

    @Test
    public void testAttackAllHostInvalidAttack() {
        when(storageMock.getPlayerStatus(hostId)).thenReturn(PlayerStatus.HOST);
        when(storageMock.getCurrentGame(hostId)).thenReturn(gameMock);
        when(gameMock.getStatus()).thenReturn(GameStatus.IN_PROGRESS);
        when(gameMock.notReadyToStart()).thenReturn(false);
        when(gameMock.getHostPlayer()).thenReturn(hostPlayerMock);
        when(hostPlayerMock.id()).thenReturn(hostId);
        when(gameMock.getHostTurn()).thenReturn(true);
        when(gameMock.getGuestPlayer()).thenReturn(guestPlayerMock);
        when(guestPlayerMock.socketChannel()).thenReturn(guestPlayerSocketChannelMock);
        when(guestPlayerMock.board()).thenReturn(guestPlayerBoard);
        char[] columns = new char[]{'a'};
        when(guestPlayerBoard.attackAll(columns)).thenReturn(attackResponseDTOMock);
        when(attackResponseDTOMock.getValidAttack()).thenReturn(false);

        CommandResponseDTO response = AttackAll.execute(storageMock, hostId, "A");

        assertEquals(INVALID_ATTACK_ALL_USAGE, response.getMsgThis(), "Expected message-attacker message");
    }
}
