package design.kfu.sunrise.filerepository.exception;

/**
 * @author Daniyar Zakiev
 */
public class FileServiceException extends RuntimeException {
    public FileServiceException(String message) {
        super(message);
    }

    public FileServiceException(String message, Throwable cause) {
        super(message, cause);
    }

    public FileServiceException(Throwable cause) {
        super(cause);
    }
}
