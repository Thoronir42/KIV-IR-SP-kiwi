package cz.zcu.students.kiwi.utils;

public class SerDesException extends Exception {
    public SerDesException(String message) {
        super(message);
    }

    public SerDesException(String message, Throwable cause) {
        super(message, cause);
    }

    public SerDesException(Throwable cause) {
        super(cause);
    }
}
