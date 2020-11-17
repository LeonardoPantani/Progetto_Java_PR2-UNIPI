package exception;

// Leonardo Pantani | Java project for PR2 course at UNIPI

public class TextLengthException extends Exception {
    public TextLengthException(int n) {
        super("Text size exceeds " + n + " characters!");
    }
}