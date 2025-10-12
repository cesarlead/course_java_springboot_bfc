package course.javaspringboot.M04_Excepciones;

public class CuentaBancaria {

    public double saldo;
    public String id;

    public CuentaBancaria(String id, double saldo) {
        this.id = id;
        this.saldo = saldo;
    }

    public void retirar(double monto) {
        this.saldo -= monto;
    }

    public void depositar(double monto) {
        this.saldo += monto;
    }
}
