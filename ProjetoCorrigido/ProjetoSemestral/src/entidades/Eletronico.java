package entidades;

/**
 * Produto eletrônico: tem garantia em meses e alíquota de imposto de 18%.
 */
public class Eletronico extends Produto {
	private Integer garantiaMeses;

	public Eletronico(Integer codigo, String nome, Double precoBase, Integer quantidadeEstoque, Integer garantiaMeses) {
		super(codigo, nome, precoBase, quantidadeEstoque); // dados comuns vão para a superclasse
		this.garantiaMeses = garantiaMeses;
	}

	public Integer getGarantiaMeses() {
		return garantiaMeses;
	}

	public void setGarantiaMeses(Integer garantiaMeses) {
		this.garantiaMeses = garantiaMeses;
	}

	// Alíquota de 18% (contrato da interface Taxavel)
	@Override
	public Double getAliquotaImposto() {
		return 0.18;
	}

	// Preço base + imposto (calcularImposto vem pronto do default method de Taxavel)
	@Override
	public Double calcularPrecoFinal() {
		return getPrecoBase() + calcularImposto(getPrecoBase());
	}

	@Override
	public String getDetalhes() {
		return "Garantia: " + garantiaMeses + " meses";
	}

}
