package Exception.UserExceptions.SupportException;

public class emailClientNotFoundException extends RuntimeException {
    public emailClientNotFoundException() {super();}
    public emailClientNotFoundException(String message) {
        super(message);
    }
}
