package entidades;

import entidadesEnumerada.Tamanho;

/**
 * Produto de vestuário: tem tamanho (enum) e cor, e alíquota de imposto de 12%.
 */
public class Vestuario extends Produto {
	private Tamanho tamanho;
	private String cor;

	public Vestuario(Integer codigo, String nome, Double precoBase, Integer quantidadeEstoque, Tamanho tamanho,
			String cor) {
		super(codigo, nome, precoBase, quantidadeEstoque);
		this.tamanho = tamanho;
		this.cor = cor;
	}

	public Tamanho getTamanho() {
		return tamanho;
	}

	public void setTamanho(Tamanho tamanho) {
		this.tamanho = tamanho;
	}

	public String getCor() {
		return cor;
	}

	public void setCor(String cor) {
		this.cor = cor;
	}

	// Alíquota de 12% (contrato da interface Taxavel)
	@Override
	public Double getAliquotaImposto() {
		return 0.12;
	}

	// Preço base + imposto
	@Override
	public Double calcularPrecoFinal() {
		return getPrecoBase() + calcularImposto(getPrecoBase());
	}

	@Override
	public String getDetalhes() {
		return "Tamanho: " + tamanho + " Cor: " + cor;
	}

}
