package commands;

import utils.InfoEntry;
import utils.Numeric;

import java.util.Scanner;

public class Add implements Command {
    @Override
    public void execute() {

    }

    @Override
    public void execute(String line) throws Exception {
        String[] params = line.split(" ");
        String height = params[2];
        if (params.length != 3 || Numeric.isNumeric(params[3])) throw new Exception("Incorrect arguments in the command");
        Scanner in = InfoEntry.scanner();


    }

    @Override
    public String descr() {
        return "add {name height}   Add new Person in collection";
    }
}
