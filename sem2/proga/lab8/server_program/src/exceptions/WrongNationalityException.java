package exceptions;

public class WrongNationalityException extends IllegalArgumentException {
    public WrongNationalityException() { super("Error in nationality value"); }
    public WrongNationalityException(String message) { super(message); }
}
