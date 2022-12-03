package cargo.delivery.epam.com.project.infrastructure.web.exception;

/**
 * Custom unchecked exception for this web application.
 *
 * @see java.lang.RuntimeException
 */
public class AppException extends RuntimeException {
    public AppException(String s) {
        super(s);
    }
}
