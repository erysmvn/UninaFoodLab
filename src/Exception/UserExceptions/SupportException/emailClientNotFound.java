package Exception.UserExceptions.SupportException;

public class emailClientNotFound extends RuntimeException {
    public emailClientNotFound() {super();}
    public emailClientNotFound(String message) {
        super(message);
    }
}
