package entidades;

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

	@Override
	public Double getAliquotaImposto() {
		return 0.07;
	}

	@Override
	public Double calcularPrecoFinal() {
		return getPrecoBase() + calcularImposto(getPrecoBase());
	}

	@Override
	public String getDetalhes() {
		return "Dias para o vencimento: " + diasParaVencer;
	}

}
