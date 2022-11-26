package net.diegoqueres.opennlptagger.domain.exception;

public class InvalidInputException extends RuntimeException {
    public static final String DEFAULT_USER_ADVICE = "Check input data and try again";

    private final String userAdvice;

    public InvalidInputException(String message) {
        this(message, DEFAULT_USER_ADVICE);
    }

    public InvalidInputException(String message, String userAdvice) {
        super(message);
        this.userAdvice = userAdvice;
    }

    public InvalidInputException(String message, Throwable cause) {
        this(message, DEFAULT_USER_ADVICE, cause);
    }

    public InvalidInputException(String message, String userAdvice, Throwable cause) {
        super(message, cause);
        this.userAdvice = userAdvice;
    }

    public String getUserAdvice() {
        return userAdvice;
    }

}
