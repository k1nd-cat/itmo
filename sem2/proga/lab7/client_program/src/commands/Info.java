package commands;

import command_management.CommandManager;
import net.client.Client;
import net.data.Request;
import net.data.Response;
import net.data.ServerCommand;
import users.Identification;

import java.io.BufferedReader;
import java.util.List;
import java.util.logging.Logger;

import static worker.PersonStorage.getPersons;

/**
 * class-command which print info about person collection
 */
public class Info implements Command {

    private static Logger logger = Logger.getLogger("lab6");

    /**
     * @param line    command from main menu
     * @param in      parameter for input
     * @param manager command manager for commands menu
     */
    @Override
    public void execute(List<String> line, BufferedReader in, CommandManager manager) {
        if (line.size() != 1) {
            System.err.println("Error in command syntax");
            return;
        }
        try {
            var command = ServerCommand.info(Identification.getIdentification().getUserInfo().getId());
            Response response = Client.getClient().sendRequest(new Request(command));
            System.out.println("INFO from server: " + response.getBody(String.class));
        } catch (Exception exc) {
            logger.info("Error in clearing collection" + exc);
        }
    }

    /**
     * @return this command description
     */
    @Override
    public String description() {
        return "info    Information about collection of persons";
    }
}