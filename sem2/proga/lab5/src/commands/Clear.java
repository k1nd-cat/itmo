package commands;

public class Clear implements Command {
    @Override
    public void execute() {

    }

    @Override
    public void execute(String line) throws Exception {

    }

    @Override
    public String descr() {
        return "clear   Delete all persons from collection";
    }
}
