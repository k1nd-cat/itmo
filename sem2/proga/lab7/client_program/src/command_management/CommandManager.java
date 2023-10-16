package command_management;

import commands.*;
import exceptions.NullStringException;
import lombok.Getter;
import lombok.Setter;

import java.io.BufferedReader;
import java.io.IOException;
import java.util.*;

/**
 * class for management commands
 */
public class CommandManager {
    private static final Map<String, Command> commands = new HashMap<String, Command>() {{
        put("help", new Help());
        put("info", new Info());
        put("show", new Show());
        put("add", new Add());
        put("update", new Update());
        put("remove_by_id", new RemoveById());
        put("clear", new Clear());
        put("execute_script", new ExecuteScript());
        put("exit", new Exit());
        put("add_if_max", new AddIfMax());
        put("add_if_min", new AddIfMin());
        put("remove_greater", new RemoveGreater());
        put("average_of_height", new AverageOfHeight());
        put("min_by_location", new MinByLocation());
        put("print_unique_hair_color", new PrintUniqueHairColor());
    }};

    @Getter
    @Setter
    private static ArrayList<String> scriptFiles = new ArrayList<>();

    /**
     * shouldExit parameter for exception in execute script
     * isFromFile parameter for check if input not from console
     */
    public boolean shouldExit = false;
    public boolean isFromFile = false;

    /**
     * @return map all commands
     */
    public static Map<String, Command> getCommands() {
        return commands;
    }

    /**
     * read commands from main menu
     * @param in input reader
     * @param isFromFile parameter for check if input from console or file
     */
    public void readCommand(BufferedReader in, boolean isFromFile) {
        this.isFromFile = isFromFile;
        while (!shouldExit) {
            try {
                String line = in.readLine();
                if (line == null && isFromFile) return;
                if (line == null && !isFromFile) throw new NullStringException();
                if (line == null || line.isBlank()) continue;
                List<String> consoleCommand = Arrays.stream(line.split(" ")).toList();
                if (consoleCommand.isEmpty()) continue;
                if (!getCommands().containsKey(consoleCommand.get(0))) {
                    System.err.println("Error in command syntax");
                    continue;
                }
                commands.get(consoleCommand.get(0)).execute(consoleCommand, in, this);
            } catch (IOException exc) {
                System.err.println("Error in command\nproblem: " + exc.getMessage());
            } catch (NullStringException exc) {
                System.err.println("Fatal error, forced shutdown\n" + exc);
                System.exit(-1);
            }
        }
    }
}
