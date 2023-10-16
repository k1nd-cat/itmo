package exceptions;

public class WrongCoordinatesException extends IllegalArgumentException {
    public WrongCoordinatesException() { super("Error in coordinates"); }
    public WrongCoordinatesException(String message) { super(message); }
}
