package course.javaspringboot.M02_PilaresOOP;

public class CuentaDeInversion extends ProductoFinanciero {

    private NivelRiesgo tipoDeRiesgo;

    public CuentaDeInversion(String id, String cliente, double valor, NivelRiesgo riesgo) {
        // La primera línea DEBE ser la llamada al constructor padre
        super(id, cliente, valor);
        this.tipoDeRiesgo = riesgo;
    }

    public NivelRiesgo getTipoDeRiesgo() {
        return tipoDeRiesgo;
    }
}
