package course.javaspringboot.M03_InterfacesClasesAbstractas;

public class TarjetaDeCredito extends ProductoFinanciero implements Auditable {
    private double limiteCredito;
    private double deudaActual;

    public TarjetaDeCredito(String id, String cliente, double limite) {
        super(id, cliente);
        this.limiteCredito = limite;
    }

    public void realizarCompra(double monto) {
        if (deudaActual + monto <= limiteCredito) {
            this.deudaActual += monto;
            System.out.println("Compra aprobada.");

            // Lógica de negocio que dispara la capacidad de auditoría
            if (this.deudaActual / this.limiteCredito > 0.8) {
                String log = generarLog("ALERTA: Límite de crédito superó el 80%.");
                System.out.println(log);
            }
        }
    }

    // Implementación obligatoria del método abstracto de la superclase
    @Override
    public String getDescripcionProducto() {
        return "Tarjeta de Crédito con límite: " + this.limiteCredito;
    }

    // Implementación obligatoria de los métodos de la interfaz Auditable
    @Override
    public String getIdentificadorUnico() {
        return this.idProducto; // Reutilizamos el ID del producto
    }

    @Override
    public String generarLog(String mensaje) {
        return "[AUDIT] [" + java.time.LocalDateTime.now() + "] [Producto: " + getIdentificadorUnico() + "] - " + mensaje;
    }
}
