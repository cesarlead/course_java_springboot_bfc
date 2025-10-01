package course.javaspringboot.M04_Excepciones;

public class TransferenciaInvalidaException extends Exception {
    public TransferenciaInvalidaException(String message) {
        super(message);
    }
}
