import command_management.CommandManager;
import users.Identification;

import java.io.BufferedReader;
import java.io.InputStreamReader;

public class Main {
    public static void main(String[] args) {
        Identification.getIdentification().readInfo(new BufferedReader(new InputStreamReader(System.in)));
        new CommandManager().readCommand(new BufferedReader(new InputStreamReader(System.in)), false);
    }
}