package exceptions;

import java.util.NoSuchElementException;

public class NullStringException extends NoSuchElementException {
    public NullStringException() { super("Null entered value"); }
    public NullStringException(String message) { super(message); }
}
