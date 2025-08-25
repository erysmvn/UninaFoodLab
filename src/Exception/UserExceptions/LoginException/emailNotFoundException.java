package Exception.UserExceptions.LoginException;

public class emailNotFoundException extends RuntimeException {
    public emailNotFoundException() {
        super();
    }

    public  emailNotFoundException(String messaggio) {
        super(messaggio);
    }
}
