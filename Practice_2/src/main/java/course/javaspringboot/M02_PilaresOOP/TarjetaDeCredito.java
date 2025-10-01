package course.javaspringboot.M02_PilaresOOP;

public class TarjetaDeCredito extends ProductoFinanciero {

    private double limiteCredito;
    private double deudaActual;
    private final double TASA_INTERES_MENSUAL = 0.03; // 3%

    public TarjetaDeCredito(String id, String cliente, double limite) {
        super(id, cliente, 0); // La deuda inicial es 0
        this.limiteCredito = limite;
        this.deudaActual = 0;
    }

    public void realizarCompra(double monto) {
        if (deudaActual + monto <= limiteCredito) {
            this.deudaActual += monto;
            System.out.println("Compra aprobada.");
        } else {
            System.out.println("Límite de crédito excedido.");
        }
    }

    // Polimorfismo: Sobrescritura del método de la superclase
    @Override
    public double calcularCosteMensual() {
        return this.deudaActual * TASA_INTERES_MENSUAL;
    }

    public double getLimiteCredito() {
        return limiteCredito;
    }

    public double getDeudaActual() {
        return deudaActual;
    }
}
