package commands;

import command_management.CommandManager;
import net.client.Client;
import net.data.Request;
import net.data.ServerCommand;

import java.io.BufferedReader;
import java.util.List;

import static worker.PersonStorage.getPersons;

/**
 * class-command which print info about person collection
 */
public class Info implements Command {
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
            var command = ServerCommand.info();
            String info = Client.getClient().sendRequest(new Request(command)).getBody(String.class);
            if (manager.isFromFile) Thread.sleep(50);
            System.out.println(info);
        } catch (Exception exc) {
            System.err.println("Error in creating person\nproblem: " + exc.getMessage());
            if (manager.isFromFile) {
                manager.shouldExit = true;
            }
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