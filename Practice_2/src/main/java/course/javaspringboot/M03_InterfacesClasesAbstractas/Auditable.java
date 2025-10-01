package course.javaspringboot.M03_InterfacesClasesAbstractas;

public interface Auditable {
    String getIdentificadorUnico(); // Contrato para obtener un ID

    String generarLog(String mensaje);
}
