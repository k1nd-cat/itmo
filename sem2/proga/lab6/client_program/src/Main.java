import command_management.CommandManager;

import java.io.BufferedReader;
import java.io.InputStreamReader;

public class Main {
    public static void main(String[] args) {
        new CommandManager().readCommand(new BufferedReader(new InputStreamReader(System.in)), false);
    }
}