package commands;

import command_management.CommandManager;
import models.Person;
import net.client.Client;
import net.data.Request;
import net.data.ServerCommand;

import java.io.BufferedReader;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * class-command average height value
 */
public class AverageOfHeight implements Command {
    private static Logger logger = Logger.getLogger("lab6");
    /**
     * @param line    command from main menu
     * @param in      parameter for input
     * @param manager command manager for commands menu
     */
    @Override
    public void execute(List<String> line, BufferedReader in, CommandManager manager) {
        if (line.size() != 1) {
            System.err.println("Incorrect command");
            return;
        }

        try {
            ServerCommand command = ServerCommand.getAverageValue();
            float averageValue = Client.getClient().sendRequest(new Request(command)).getBody(float.class);
            System.out.println("Average height = " + averageValue);
        } catch (Exception exc) {
            logger.log(Level.WARNING, "Error in command: average_of_height", exc);
        }
    }

    /**
     * @return this command description
     */
    @Override
    public String description() {
        return "average_of_height   Display the average value of the height";
    }
}
