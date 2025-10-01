package course.javaspringboot.M03_InterfacesClasesAbstractas;

public abstract class ProductoFinanciero {

    protected String idProducto;
    protected String clienteAsociado;

    public ProductoFinanciero(String id, String cliente) {
        this.idProducto = id;
        this.clienteAsociado = cliente;
    }

    // Método Abstracto: Fuerza a las subclases a implementarlo
    public abstract String getDescripcionProducto();

    // Método Concreto: Comportamiento compartido
    public String getCliente() {
        return this.clienteAsociado;
    }

}
