package commands;

import command_management.CommandManager;
import models.Person;
import net.client.Client;
import net.data.Request;
import net.data.ServerCommand;
import worker.PersonStorage;

import java.io.BufferedReader;
import java.util.Comparator;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * class command which find and print person with min location value
 */
public class MinByLocation implements Command {
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
            var command = ServerCommand.minByLocation();
            String result = Client.getClient().sendRequest(new Request(command)).getBody(String.class);
            System.out.println(result);
        } catch (Exception exc) {
            logger.log(Level.WARNING, "Error in command: min_by_location", exc);
            System.exit(-1);
        }
    }

    /**
     * @return this command description
     */
    @Override
    public String description() {
        return "Display the person with a minimum value of location";
    }
}
