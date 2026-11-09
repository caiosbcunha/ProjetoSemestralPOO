package entidades;

import entidadesEnumerada.Tamanho;

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

	@Override
	public Double getAliquotaImposto() {
		return 0.12;
	}

	@Override
	public Double calcularPrecoFinal() {
		return getPrecoBase() + calcularImposto(getPrecoBase());
	}

	@Override
	public String getDetalhes() {
		return "Tamanho: " + tamanho + " Cor: " + cor;
	}

}
