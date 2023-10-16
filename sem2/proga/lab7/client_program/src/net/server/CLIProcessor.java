package net.server;

import worker.ServerDispatcher;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.util.logging.Logger;

/**
 * With multithreading.
 */
public class CLIProcessor {

    private static final Logger logger = Logger.getLogger("lab6");

    private static final String CLI_COMMAND_EXIT = "exit";
    private static final String CLI_COMMAND_SAVE = "save";

    public void executeCli() {
        try {
            while (true) {
                if (System.in.available() > 0) {
                    BufferedReader reader = new BufferedReader(new InputStreamReader(System.in));
                    var command = reader.readLine();
                    if (command.equals(CLI_COMMAND_EXIT)) {
                        logger.info("Server is stopped by user, good-bye!");
                        ServerDispatcher.getDispatcher().performSave();
                        System.exit(1);
                    } else if (command.equals(CLI_COMMAND_SAVE)) {
                        logger.info("Save data");
                        ServerDispatcher.getDispatcher().performSave();
                    } else {
                        logger.info("Unknown server command: " + command);
                    }
                }
                Thread.sleep(100);
            }
        } catch (Exception exc) {
            throw new RuntimeException(exc);
        }
    }
}
