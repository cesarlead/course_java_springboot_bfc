package course.javaspringboot.M04_Excepciones;

public class CuentaNoEncontradaException extends Exception {
    public CuentaNoEncontradaException(String mensaje) {
        super(mensaje);
    }

}
