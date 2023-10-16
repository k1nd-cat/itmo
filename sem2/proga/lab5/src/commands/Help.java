package commands;

public class Help implements Command {
    @Override
    public void execute() {
        for (Command c: Invoker.getCommands().values())
            System.out.println(c.descr());

    }

    @Override
    public void execute(String line) throws Exception {

    }

    @Override
    public String descr() {
        return "help    Info about all commands";
    }
}
