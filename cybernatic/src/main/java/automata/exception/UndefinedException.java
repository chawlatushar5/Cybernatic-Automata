package automata.exception;

public class UndefinedException extends DependencyException {

    private static final long serialVersionUID = -8940196742313994740L;

    public UndefinedException(String message, Throwable cause) {
        super(message, cause);
    }
}
