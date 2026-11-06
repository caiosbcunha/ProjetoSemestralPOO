package entidades;

public interface Taxavel {

	Double getAliquotaImposto();

	default Double calcularImposto(Double valor) {
		return valor * getAliquotaImposto();
	}
}