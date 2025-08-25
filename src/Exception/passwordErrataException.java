package Exception;

import java.sql.SQLException;

public class passwordErrataException extends RuntimeException {
    public passwordErrataException(){super();}
    public passwordErrataException(String message) {
        super(message);
    }
}
