package hotel.command;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.BeforeEach;
import static org.junit.jupiter.api.Assertions.*;

class CommandRegistryTest {

    private CommandRegistry registry;
    private Command testCommand1;
    private Command testCommand2;

    @BeforeEach
    void setUp() {
        registry = new CommandRegistry();
        testCommand1 = () -> System.out.println("Command 1 executed");
        testCommand2 = () -> System.out.println("Command 2 executed");
    }

    @Test
    void testRegisterAndGet() {
        registry.register("test", testCommand1);

        Command retrieved = registry.get("test");

        assertSame(testCommand1, retrieved);
    }

    @Test
    void testGetNonExistentCommand() {
        Command retrieved = registry.get("nonexistent");

        assertNull(retrieved);
    }

    @Test
    void testRegisterCaseInsensitive() {
        registry.register("TEST", testCommand1);

        Command retrieved1 = registry.get("test");
        Command retrieved2 = registry.get("TEST");
        Command retrieved3 = registry.get("Test");

        assertSame(testCommand1, retrieved1);
        assertSame(testCommand1, retrieved2);
        assertSame(testCommand1, retrieved3);
    }

    @Test
    void testRegisterMultipleCommands() {
        registry.register("command1", testCommand1);
        registry.register("command2", testCommand2);

        assertSame(testCommand1, registry.get("command1"));
        assertSame(testCommand2, registry.get("command2"));
    }

    @Test
    void testRegisterOverwrite() {
        registry.register("test", testCommand1);
        registry.register("test", testCommand2);

        Command retrieved = registry.get("test");

        assertSame(testCommand2, retrieved);
        assertNotSame(testCommand1, retrieved);
    }

    @Test
    void testRegisterNullCommand() {
        registry.register("null", null);

        Command retrieved = registry.get("null");

        assertNull(retrieved);
    }

    @Test
    void testCommandExecution() {
        registry.register("test", testCommand1);

        Command retrieved = registry.get("test");
        assertNotNull(retrieved);

        // Test that the command can be executed without errors
        assertDoesNotThrow(() -> retrieved.execute());
    }
}

