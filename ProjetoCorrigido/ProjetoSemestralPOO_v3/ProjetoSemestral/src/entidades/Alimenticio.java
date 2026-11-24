package entidades;

/**
 * Produto alimentício: tem dias para vencer e alíquota de imposto de 7%.
 * Se faltarem menos de 7 dias para vencer, o preço final recebe 30% de desconto.
 */
public class Alimenticio extends Produto {
	private Integer diasParaVencer;

	public Alimenticio(Integer codigo, String nome, Double precoBase, Integer quantidadeEstoque,
			Integer diasParaVencer) {
		super(codigo, nome, precoBase, quantidadeEstoque);
		this.diasParaVencer = diasParaVencer;
	}

	public Integer getDiasParaVencer() {
		return diasParaVencer;
	}

	public void setDiasParaVencer(Integer diasParaVencer) {
		this.diasParaVencer = diasParaVencer;
	}

	// Alíquota de 7% (contrato da interface Taxavel)
	@Override
	public Double getAliquotaImposto() {
		return 0.07;
	}

	// Preço base + imposto, com desconto de 30% quando o vencimento está próximo
	@Override
	public Double calcularPrecoFinal() {
		Double preco = getPrecoBase() + calcularImposto(getPrecoBase());
		if (diasParaVencer < 7) {
			preco = preco * 0.70; // desconto de 30%
		}
		return preco;
	}

	@Override
	public String getDetalhes() {
		return "Dias para o vencimento: " + diasParaVencer;
	}

}
