package commands;

public interface Command {
    void execute();
    void execute(String line) throws Exception;

    String descr();
}
