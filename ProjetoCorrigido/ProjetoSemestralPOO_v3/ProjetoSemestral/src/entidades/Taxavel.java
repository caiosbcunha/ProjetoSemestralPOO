package entidades;

/**
 * Contrato para tudo que paga imposto.
 */
public interface Taxavel {

	// Sem corpo: cada classe define a sua alíquota (ex.: 0.18 = 18%)
	Double getAliquotaImposto();

	// default method: implementação pronta, as classes não precisam reescrever
	default Double calcularImposto(Double valor) {
		return valor * getAliquotaImposto();
	}
}
