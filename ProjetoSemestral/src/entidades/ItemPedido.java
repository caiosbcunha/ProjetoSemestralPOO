package entidades;

public class ItemPedido {
	private Produto produto;
	private Integer quantidade;
	private Double precoUnitario;

	public ItemPedido(Produto produto, Integer quantidade) {
		super();
		this.produto = produto;
		this.quantidade = quantidade;
		this.precoUnitario = produto.calcularPrecoFinal();
	}

	public Produto getProduto() {
		return produto;
	}

	public void setProduto(Produto produto) {
		this.produto = produto;
	}

	public Integer getQuantidade() {
		return quantidade;
	}

	public void setQuantidade(Integer quantidade) {
		this.quantidade = quantidade;
	}

	public Double getPrecoUnitario() {
		return precoUnitario;
	}

	public void setPrecoUnitario(Double precoUnitario) {
		this.precoUnitario = precoUnitario;
	}

	public Double getSubtotal() {
		return precoUnitario * quantidade;
	}
}
