package exceptions;

public class WrongLocationException extends IllegalArgumentException {
    public WrongLocationException() { super("Error in nationality value"); }
    public WrongLocationException(String message) { super(message); }
}
