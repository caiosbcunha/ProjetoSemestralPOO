package entidades;

public class Eletronico extends Produto {
	private Integer garantiaMeses;

	public Eletronico(Integer codigo, String nome, Double precoBase, Integer quantidadeEstoque, Integer garantiaMeses) {
		super(codigo, nome, precoBase, quantidadeEstoque);
		this.garantiaMeses = garantiaMeses;
	}

	@Override
	public Double getAliquotaImposto() {
		return 0.18;
	}

	@Override
	public Double calcularPrecoFinal() {
		return getPrecoBase() + calcularImposto(getPrecoBase());
	}

	@Override
	public String getDetalhes() {
		return "Garantia: " + garantiaMeses + " meses";
	}

}
