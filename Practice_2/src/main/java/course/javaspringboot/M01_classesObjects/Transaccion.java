package course.javaspringboot.M01_classesObjects;

import java.time.LocalDateTime;
import java.util.UUID;

public class Transaccion {

    // Atributos que definen el ESTADO de una transacción
    String id;
    double monto;
    LocalDateTime fechaHora;
    TipoTransaccion tipo;
    String cuentaOrigen; // Puede ser null para depósitos
    String cuentaDestino; // Puede ser null para retiros

    // Constructor para inicializar los objetos
    public Transaccion(double monto, TipoTransaccion tipo, String origen, String destino) {
        this.id = UUID.randomUUID().toString(); // Genera un ID único
        this.monto = monto;
        this.fechaHora = LocalDateTime.now(); // Captura el momento exacto
        this.tipo = tipo;
        this.cuentaOrigen = origen;
        this.cuentaDestino = destino;
    }

    public void mostrarDetalles() {
        System.out.println("------------------------------------");
        System.out.println("ID Transacción: " + this.id);
        System.out.println("Tipo: " + this.tipo);
        System.out.println("Monto: " + String.format("%.2f", this.monto) + " EUR");
        System.out.println("Fecha y Hora: " + this.fechaHora);
        if (cuentaOrigen != null) {
            System.out.println("Cuenta Origen: " + this.cuentaOrigen);
        }
        if (cuentaDestino != null) {
            System.out.println("Cuenta Destino: " + this.cuentaDestino);
        }
        System.out.println("------------------------------------");
    }
}
