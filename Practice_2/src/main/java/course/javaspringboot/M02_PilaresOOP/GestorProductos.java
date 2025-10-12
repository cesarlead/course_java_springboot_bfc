package course.javaspringboot.M02_PilaresOOP;

public class GestorProductos {
    public static void main(String[] args) {
        ProductoFinanciero p1 = new CuentaDeInversion("INV-001", "Ana", 10000, NivelRiesgo.MEDIO);
        ProductoFinanciero p2 = new TarjetaDeCredito("TDC-456", "Ana", 5000);

        // La variable es de tipo ProductoFinanciero, pero apunta a un objeto TarjetaDeCredito
        ((TarjetaDeCredito) p2).realizarCompra(1000);

        // Polimorfismo en acción
        System.out.println("Coste mensual de P1: " + p1.calcularCosteMensual()); // Llama a la versión de ProductoFinanciero -> 0.0
        System.out.println("Coste mensual de P2: " + p2.calcularCosteMensual());
    }

}
