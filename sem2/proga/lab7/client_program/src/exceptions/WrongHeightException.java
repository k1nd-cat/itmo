package exceptions;

public class WrongHeightException extends IllegalArgumentException {
    public WrongHeightException() { super("Error in height value"); }
    public WrongHeightException(String message) { super(message); }
}
