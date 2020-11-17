package exception;

public class TextLengthException extends Exception {
    public TextLengthException(int n) {
        super("Text size exceeds " + n + " characters!");
    }
}