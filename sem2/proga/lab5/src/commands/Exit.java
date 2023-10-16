package commands;

public class Exit implements Command {
    @Override
    public void execute() {

    }

    @Override
    public void execute(String line) throws Exception {

    }

    @Override
    public String descr() {
        return "exit    Terminate the program (without saving to a file)";
    }
}
