package commands;

public class RemoveGreater implements Command {
    @Override
    public void execute() {

    }

    @Override
    public void execute(String line) throws Exception {

    }

    @Override
    public String descr() {
        return "remove_greater {element}    delete all persons with height exceeds the specified";
    }
}
