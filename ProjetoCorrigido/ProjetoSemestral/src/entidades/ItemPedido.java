package entidades;

/**
 * Item de um pedido: um produto, a quantidade e o preço unitário no momento da venda.
 * O preço é "congelado" no construtor, então o histórico do pedido não muda
 * se o preço do produto for alterado depois.
 */
public class ItemPedido {
	private Produto produto;
	private Integer quantidade;
	private Double precoUnitario;

	public ItemPedido(Produto produto, Integer quantidade) {
		super();
		this.produto = produto;
		this.quantidade = quantidade;
		this.precoUnitario = produto.calcularPrecoFinal(); // congela o preço da venda
	}

	// Construtor sobrecarregado, usado pela leitura de arquivo: recebe o preço que foi
	// congelado na época da venda, em vez de recalcular com o preço atual do produto
	public ItemPedido(Produto produto, Integer quantidade, Double precoUnitario) {
		super();
		this.produto = produto;
		this.quantidade = quantidade;
		this.precoUnitario = precoUnitario;
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

	// Subtotal = preço congelado x quantidade
	public Double getSubtotal() {
		return precoUnitario * quantidade;
	}
}
