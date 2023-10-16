package exceptions;

public class WrongColorException extends IllegalArgumentException {
    public WrongColorException() { super("Error in color value"); }
    public WrongColorException(String message) { super(message); }
}
