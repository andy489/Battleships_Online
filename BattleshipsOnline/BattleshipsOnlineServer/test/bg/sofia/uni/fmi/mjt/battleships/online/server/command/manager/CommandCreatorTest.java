package bg.sofia.uni.fmi.mjt.battleships.online.server.command.manager;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class CommandCreatorTest {
    @Test
    public void testCreateNewCommandBasicCommand() {
        Command cmd = CommandCreator.newCommand("join-game");

        assertEquals("join-game", cmd.command(), "Expected \"join-game\" command name");
        assertEquals(0, cmd.args().length, "Expected no command arguments");
    }

    @Test
    public void testCreateNewCommandWithOneArg() {
        Command cmd = CommandCreator.newCommand("create-game new-game");

        assertEquals("create-game", cmd.command(), "Expected \"create-game\" command name");
        assertEquals(1, cmd.args().length, "Expected one command argument");
        assertEquals("new-game", cmd.args()[0], "Expected \"new-game\" as argument");
    }


    @Test
    public void testCreateNewCommandWithTwoArgs() {
        Command cmd = CommandCreator.newCommand("delete-game my-game new-game");

        assertEquals("delete-game", cmd.command(), "Expected \"delete-game\" command name");
        assertEquals(2, cmd.args().length, "Expected two command arguments");
        assertEquals("my-game", cmd.args()[0], "Expected \"my-game\" as first argument");
        assertEquals("new-game", cmd.args()[1], "Expected \"new-game\" as second argument");
    }

    @Test
    public void testCreateNewCommandWithOneMultipleWordArgument() {
        Command cmd = CommandCreator.newCommand("create-game \"battle of the Vikings\"");

        assertEquals("create-game", cmd.command(), "Expected \"create-game\" command name");
        assertEquals(1, cmd.args().length, "Expected one multiple word argument");
        assertEquals("battle of the Vikings", cmd.args()[0],
                "Expected command argument to be \"battle of the Vikings\"");
    }
}
