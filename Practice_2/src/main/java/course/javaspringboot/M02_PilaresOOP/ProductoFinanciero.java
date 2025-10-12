package course.javaspringboot.M02_PilaresOOP;

public class ProductoFinanciero {

    private String idProducto;
    private String clienteAsociado;
    protected double valor; // protected permite acceso a subclases

    public ProductoFinanciero(String idProducto, String clienteAsociado, double valorInicial) {
        this.idProducto = idProducto;
        this.clienteAsociado = clienteAsociado;
        this.valor = valorInicial;
    }

    public double calcularCosteMensual() {
        return 0.0; // Un producto genérico no tiene coste
    }

    // Getters para acceso controlado (Encapsulamiento)
    public String getIdProducto() {
        return idProducto;
    }

    public String getClienteAsociado() {
        return clienteAsociado;
    }

    public double getValor() {
        return valor;
    }
}
