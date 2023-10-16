package exceptions;

public class WrongCommandException extends IllegalArgumentException {
    public WrongCommandException() { super("Error in command"); }
    public WrongCommandException(String message) { super(message); }
}
