package springbook.exception;

public class SqlRetrievalFailureException extends RuntimeException {
    public SqlRetrievalFailureException(Throwable e) {
        super(e);
    }

	public SqlRetrievalFailureException(String message, Throwable cause) {
		super(message, cause);
	}
}
