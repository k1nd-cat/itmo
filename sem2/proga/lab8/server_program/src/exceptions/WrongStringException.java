package exceptions;

public class WrongStringException extends IllegalArgumentException {
    public WrongStringException() {super("Error in str"); }
    public WrongStringException(String message) { super(message); }
}
