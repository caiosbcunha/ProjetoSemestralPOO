package entidades;

public abstract class Produto implements Taxavel {
	private Integer codigo;
	private String nome;
	private Double precoBase;
	private Integer quantidadeEstoque;

	public Produto(Integer codigo, String nome, Double precoBase, Integer quantidadeEstoque) {
		super();
		this.codigo = codigo;
		this.nome = nome;
		this.precoBase = precoBase;
		this.quantidadeEstoque = quantidadeEstoque;
	}

	public Integer getCodigo() {
		return codigo;
	}

	public void setCodigo(Integer codigo) {
		this.codigo = codigo;
	}

	public String getNome() {
		return nome;
	}

	public void setNome(String nome) {
		this.nome = nome;
	}

	public Double getPrecoBase() {
		return precoBase;
	}

	public void setPrecoBase(Double precoBase) {
		this.precoBase = precoBase;
	}

	public Integer getQuantidadeEstoque() {
		return quantidadeEstoque;
	}

	public void setQuantidadeEstoque(Integer quantidadeEstoque) {
		this.quantidadeEstoque = quantidadeEstoque;
	}

	public abstract Double calcularPrecoFinal();

	public abstract String getDetalhes();

	public void entradaEstoque(int quantidade) {
		if (quantidade > 0) {
			quantidadeEstoque += quantidade;
		}
	}

	public boolean saidaEstoque(int quantidade) {
		if (quantidade > 0 && quantidade <= quantidadeEstoque) {
			quantidadeEstoque -= quantidade;
			return true;
		} else {
			return false;
		}
	}

	@Override
	public String toString() {
		return String.format("Produto: %s %nCódigo: %d %nPreço: R$%.2f %nQuantidade em estoque: %d", nome, codigo,
				calcularPrecoFinal(), quantidadeEstoque);
	}

}
