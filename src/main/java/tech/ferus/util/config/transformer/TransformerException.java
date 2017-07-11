package tech.ferus.util.config.transformer;

public class TransformerException extends UnsupportedOperationException {

    public TransformerException(final String message) {
        super(message);
    }

    public TransformerException(final Throwable cause) {
        super(cause);
    }

    public TransformerException(final String message, final Throwable cause) {
        super(message, cause);
    }
}
