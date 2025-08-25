package Exception;

import java.sql.SQLException;

public class emailNotFoundException extends RuntimeException {
    public emailNotFoundException() {
        super();
    }

    public  emailNotFoundException(String messaggio) {
        super(messaggio);
    }
}
