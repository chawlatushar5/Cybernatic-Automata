package automata.exception;

public class DependencyException extends Exception {

    private static final long serialVersionUID = -8940196742313994740L;

    public DependencyException() {
        super();
    }

    protected DependencyException(String message, Throwable cause) {
        super(message, cause);
    }
}
