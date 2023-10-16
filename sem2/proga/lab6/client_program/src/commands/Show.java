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
 * class-command witch show info about all people
 */
public class Show implements Command {
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
            var command = ServerCommand.show();
            String show = Client.getClient().sendRequest(new Request(command)).getBody(String.class);
            System.out.println(show);
            if (manager.isFromFile) Thread.sleep(50);
        } catch (Exception exc) {
            logger.log(Level.WARNING, "Error in command: show", exc);
            System.exit(-1);
        }

//        System.out.println("----------------------------------------------");
//        for (Person person : getPersons()) {
//            System.out.println(person.toString() + "\n----------\n");
//        }
    }

    /**
     * @return this command description
     */
    @Override
    public String description() {
        return "show    Information about all persons";
    }
}
