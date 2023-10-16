package exceptions;

public class RecursionFileException extends IllegalArgumentException {
    public RecursionFileException() { super("File has god recursion"); }
    public RecursionFileException(String message) { super(message); }
}
