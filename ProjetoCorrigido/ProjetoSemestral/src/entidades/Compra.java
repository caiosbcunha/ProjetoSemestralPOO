package entidades;

/**
 * Compra de reposição de estoque: uma quantidade de um produto comprada de um fornecedor
 * por um custo unitário. Os dados são validados e a entrada no estoque é feita
 * por ServicoCompra. Implementa Comparable: a ordenação natural é pelo total gasto.
 */
public class Compra implements Comparable<Compra> {
	private Integer numero;
	private String fornecedor;
	private Produto produto;
	private Integer quantidade;
	private Double custoUnitario;

	public Compra(Integer numero, String fornecedor, Produto produto, Integer quantidade, Double custoUnitario) {
		super();
		this.numero = numero;
		this.fornecedor = fornecedor;
		this.produto = produto;
		this.quantidade = quantidade;
		this.custoUnitario = custoUnitario;
	}

	public Integer getNumero() {
		return numero;
	}

	public String getFornecedor() {
		return fornecedor;
	}

	public void setFornecedor(String fornecedor) {
		this.fornecedor = fornecedor;
	}

	public Produto getProduto() {
		return produto;
	}

	public Integer getQuantidade() {
		return quantidade;
	}

	public Double getCustoUnitario() {
		return custoUnitario;
	}

	public void setCustoUnitario(Double custoUnitario) {
		this.custoUnitario = custoUnitario;
	}

	// Valor total gasto na compra
	public Double getTotal() {
		return custoUnitario * quantidade;
	}

	// Ordenação natural: pelo total gasto
	@Override
	public int compareTo(Compra outra) {
		return this.getTotal().compareTo(outra.getTotal());
	}

	@Override
	public String toString() {
		return "Compra " + numero + " - " + fornecedor + " - " + produto.getNome() + " x" + quantidade;
	}

}
