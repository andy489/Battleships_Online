package bg.sofia.uni.fmi.mjt.battleships.online.server.command.mode.playing.attack;

import bg.sofia.uni.fmi.mjt.battleships.online.server.command.response.AttackResponseDTO;
import bg.sofia.uni.fmi.mjt.battleships.online.server.command.response.CommandResponseDTO;
import bg.sofia.uni.fmi.mjt.battleships.online.server.game.Game;
import bg.sofia.uni.fmi.mjt.battleships.online.server.game.GameStatus;
import bg.sofia.uni.fmi.mjt.battleships.online.server.game.player.Player;
import bg.sofia.uni.fmi.mjt.battleships.online.server.game.player.PlayerStatus;
import bg.sofia.uni.fmi.mjt.battleships.online.server.game.player.board.Board;
import bg.sofia.uni.fmi.mjt.battleships.online.server.storage.Storage;
import bg.sofia.uni.fmi.mjt.battleships.online.server.storage.UserIn;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.nio.channels.SocketChannel;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
public class AttackTest {
    private static final String BLUE = "\u001B[34m";
    private static final String DEFAULT = "\u001B[0m";

    private static final String ATTACK_USAGE = "Not enough arguments. Usage: attack <[A-J]> <[1-10]>";
    private static final String NOT_IN_GAME = "No current game";
    private static final String SINGLE_PLAYER_MODE_MSG = "Cannot start a single player mode";
    private static final String PLAYER_NOT_PRESSED_START = "One of the players did not press start";
    private static final String NOT_YOUR_TURN = "It's not your turn";
    private static final String WIN_MSG = "All enemy ships are destroyed. %sYOU WIN!%s";
    private static final String LOST_MSG = "All your ships have been destroyed. %sYOU LOST!%s";

    private final String hostId = "host-id";
    private final String guestId = "guest-id";

    private final String guestUsername = "guest-username";

    @Mock
    private Storage storageMock;

    @Mock
    private Game gameMock;

    @Mock
    private Player hostPlayerMock;

    @Mock
    private Player guestPlayerMock;

    @Mock
    private SocketChannel hostPlayerSocketChannelMock;

    @Mock
    private SocketChannel guestPlayerSocketChannelMock;

    @Mock
    private Board hostPlayerBoardMock;

    @Mock
    private Board guestPlayerBoardMock;

    @Mock
    private AttackResponseDTO attackResponseDTOMock;

    @Mock
    private UserIn userInHostMock;

    @Mock
    private UserIn userInGuestMock;

    @Test
    public void testAttackNotTwoArguments() {
        CommandResponseDTO response = Attack.execute(storageMock, hostId, "A");

        assertEquals(ATTACK_USAGE, response.getMsgThis(), "Response should contain attack usage message");
    }

    @Test
    public void testAttackWhenNotInGame() {
        when(storageMock.getPlayerStatus(hostId)).thenReturn(PlayerStatus.ONLINE);

        CommandResponseDTO response = Attack.execute(storageMock, hostId, "A", "3", "DOWN", "2");

        assertEquals(NOT_IN_GAME, response.getMsgThis(), "Response should contain not in game message");
    }

    @Test
    public void testAttackGameNotInProgress() {
        when(storageMock.getPlayerStatus(hostId)).thenReturn(PlayerStatus.HOST);
        when(storageMock.getCurrentGame(hostId)).thenReturn(gameMock);
        when(gameMock.getStatus()).thenReturn(GameStatus.PENDING);

        CommandResponseDTO response = Attack.execute(storageMock, hostId, "A", "3", "DOWN", "2");

        assertEquals(SINGLE_PLAYER_MODE_MSG, response.getMsgThis(),
                "Response should contain not in game message");
    }

    @Test
    public void testAttackGameNotReady() {
        when(storageMock.getPlayerStatus(hostId)).thenReturn(PlayerStatus.HOST);
        when(storageMock.getCurrentGame(hostId)).thenReturn(gameMock);
        when(gameMock.getStatus()).thenReturn(GameStatus.IN_PROGRESS);
        when(gameMock.notReadyToStart()).thenReturn(true);

        CommandResponseDTO response = Attack.execute(storageMock, hostId, "A", "3", "DOWN", "2");

        assertEquals(PLAYER_NOT_PRESSED_START, response.getMsgThis(),
                "Response should contain one of the players not ready to start message");
    }

    @Test
    public void testAttackHostNotYourTurn() {
        when(storageMock.getPlayerStatus(hostId)).thenReturn(PlayerStatus.HOST);
        when(storageMock.getCurrentGame(hostId)).thenReturn(gameMock);
        when(gameMock.getStatus()).thenReturn(GameStatus.IN_PROGRESS);
        when(gameMock.notReadyToStart()).thenReturn(false);
        when(gameMock.getHostPlayer()).thenReturn(hostPlayerMock);
        when(hostPlayerMock.id()).thenReturn(hostId);
        when(gameMock.getHostTurn()).thenReturn(false);

        CommandResponseDTO response = Attack.execute(storageMock, hostId, "A", "3", "DOWN", "2");

        assertEquals(NOT_YOUR_TURN, response.getMsgThis(),
                "Response should contain one of the players not ready to start message");
    }

    @Test
    public void testAttackGuestNotYourTurn() {
        when(storageMock.getPlayerStatus(hostId)).thenReturn(PlayerStatus.HOST);
        when(storageMock.getCurrentGame(hostId)).thenReturn(gameMock);
        when(gameMock.getStatus()).thenReturn(GameStatus.IN_PROGRESS);
        when(gameMock.notReadyToStart()).thenReturn(false);
        when(gameMock.getHostPlayer()).thenReturn(hostPlayerMock);
        when(hostPlayerMock.id()).thenReturn(guestId);
        when(gameMock.getHostTurn()).thenReturn(true);

        CommandResponseDTO response = Attack.execute(storageMock, hostId, "A", "3", "DOWN", "2");

        assertEquals(NOT_YOUR_TURN, response.getMsgThis(),
                "Response should contain one of the players not ready to start message");
    }

    @Test
    public void testAttackHostInvalidAttack() {
        when(storageMock.getPlayerStatus(hostId)).thenReturn(PlayerStatus.HOST);
        when(storageMock.getCurrentGame(hostId)).thenReturn(gameMock);
        when(gameMock.getStatus()).thenReturn(GameStatus.IN_PROGRESS);
        when(gameMock.notReadyToStart()).thenReturn(false);
        when(gameMock.getHostPlayer()).thenReturn(hostPlayerMock);
        when(hostPlayerMock.id()).thenReturn(hostId);
        when(gameMock.getHostTurn()).thenReturn(true);
        when(gameMock.getGuestPlayer()).thenReturn(guestPlayerMock);
        when(guestPlayerMock.socketChannel()).thenReturn(guestPlayerSocketChannelMock);
        String hostUsername = "host-username";
        when(storageMock.getUsername(hostId)).thenReturn(hostUsername);
        when(guestPlayerMock.board()).thenReturn(guestPlayerBoardMock);
        when(guestPlayerBoardMock.attack("A 3")).thenReturn(attackResponseDTOMock);
        when(attackResponseDTOMock.getMsgDefender()).thenReturn("message-defender");
        when(attackResponseDTOMock.getMsgAttacker()).thenReturn("message-attacker");
        when(attackResponseDTOMock.getValidAttack()).thenReturn(false);

        CommandResponseDTO response = Attack.execute(storageMock, hostId, "A", "3", "DOWN", "2");

        assertEquals("message-attacker", response.getMsgThis(), "Expected message-attacker message");
    }

    @Test
    public void testAttackGuestInvalidAttack() {
        when(storageMock.getPlayerStatus(guestId)).thenReturn(PlayerStatus.GUEST);
        when(storageMock.getCurrentGame(guestId)).thenReturn(gameMock);
        when(gameMock.getStatus()).thenReturn(GameStatus.IN_PROGRESS);
        when(gameMock.notReadyToStart()).thenReturn(false);
        when(gameMock.getHostPlayer()).thenReturn(hostPlayerMock);
        when(hostPlayerMock.id()).thenReturn(hostId);
        when(gameMock.getHostTurn()).thenReturn(false);
        when(gameMock.getHostPlayer()).thenReturn(hostPlayerMock);
        when(hostPlayerMock.socketChannel()).thenReturn(hostPlayerSocketChannelMock);
        when(storageMock.getUsername(guestId)).thenReturn(guestUsername);
        when(hostPlayerMock.board()).thenReturn(hostPlayerBoardMock);
        when(hostPlayerBoardMock.attack("A 3")).thenReturn(attackResponseDTOMock);
        when(attackResponseDTOMock.getMsgDefender()).thenReturn("message-defender");
        when(attackResponseDTOMock.getMsgAttacker()).thenReturn("message-attacker");
        when(attackResponseDTOMock.getValidAttack()).thenReturn(false);

        CommandResponseDTO response = Attack.execute(storageMock, guestId, "A", "3", "DOWN", "2");

        assertEquals("message-attacker", response.getMsgThis(), "Expected message-attacker message");
    }

    @Test
    public void testAttackGuestValidAttackEndGame() {
        when(storageMock.getPlayerStatus(guestId)).thenReturn(PlayerStatus.GUEST);
        when(storageMock.getCurrentGame(guestId)).thenReturn(gameMock);
        when(gameMock.getStatus()).thenReturn(GameStatus.IN_PROGRESS);
        when(gameMock.notReadyToStart()).thenReturn(false);
        when(gameMock.getHostPlayer()).thenReturn(hostPlayerMock);
        when(hostPlayerMock.id()).thenReturn(hostId);
        when(gameMock.getHostTurn()).thenReturn(false);
        when(gameMock.getHostPlayer()).thenReturn(hostPlayerMock);
        when(hostPlayerMock.socketChannel()).thenReturn(hostPlayerSocketChannelMock);
        when(storageMock.getUsername(guestId)).thenReturn(guestUsername);
        when(hostPlayerMock.board()).thenReturn(hostPlayerBoardMock);
        when(hostPlayerBoardMock.attack("A 3")).thenReturn(attackResponseDTOMock);

        when(attackResponseDTOMock.getValidAttack()).thenReturn(true);
        when(attackResponseDTOMock.checkEndGame()).thenReturn(true);

        when(hostPlayerMock.id()).thenReturn(hostId);

        when(storageMock.getUserIn(hostId)).thenReturn(userInHostMock);
        when(storageMock.getUserIn(guestId)).thenReturn(userInGuestMock);

        CommandResponseDTO response = Attack.execute(storageMock, guestId, "A", "3", "DOWN", "2");

        assertEquals(String.format(WIN_MSG, BLUE, DEFAULT), response.getMsgThis(),
                "Expected win message for attacker");
        assertEquals(String.format(LOST_MSG, BLUE, DEFAULT), response.getMsgOther(),
                "Expected lost message for defender");
    }
}
