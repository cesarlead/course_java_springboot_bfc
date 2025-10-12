package course.javaspringboot.M04_Excepciones;

import java.util.Map;

public class ServicioTransferencias {

    private Map<String, CuentaBancaria> repositorioCuentas;

    public ServicioTransferencias(Map<String, CuentaBancaria> repositorio) {
        this.repositorioCuentas = repositorio;
    }

    public void realizarTransferencia(String idOrigen, String idDestino, double monto)
            throws CuentaNoEncontradaException, SaldoInsuficienteException, TransferenciaInvalidaException {

        // Regla 1: Validar monto
        if (monto <= 0) {
            throw new TransferenciaInvalidaException("El monto de la transferencia debe ser positivo.");
        }

        // Regla 2: Verificar existencia de cuentas
        CuentaBancaria origen = repositorioCuentas.get(idOrigen);
        if (origen == null) {
            throw new CuentaNoEncontradaException("La cuenta de origen '" + idOrigen + "' no existe.");
        }
        CuentaBancaria destino = repositorioCuentas.get(idDestino);
        if (destino == null) {
            throw new CuentaNoEncontradaException("La cuenta de destino '" + idDestino + "' no existe.");
        }

        // Regla 3: Verificar fondos
        if (origen.saldo < monto) {
            throw new SaldoInsuficienteException("La cuenta de origen no tiene fondos suficientes.");
        }

        // Si todas las validaciones pasan, se ejecuta la operación
        origen.retirar(monto);
        destino.depositar(monto);
        System.out.println("¡Transferencia exitosa!");
    }
}
