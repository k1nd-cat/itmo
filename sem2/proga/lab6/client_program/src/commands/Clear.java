package commands;

import command_management.CommandManager;
import net.client.Client;
import net.data.Request;
import net.data.ServerCommand;

import java.io.BufferedReader;
import java.util.List;
import java.util.logging.Logger;

/**
 * class-command clear person collection
 */
public class Clear implements Command {
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
            var command = ServerCommand.clear();
            Client.getClient().sendRequest(new Request(command));
            if (manager.isFromFile) Thread.sleep(50);
            System.out.println("----- Collection cleared -----");
        } catch (Exception exc) {
            logger.info("Error in clearing collection" + exc);
        }
    }

    /**
     * @return this command description
     */
    @Override
    public String description() {
        return "clear   Delete all persons from collection";
    }
}
