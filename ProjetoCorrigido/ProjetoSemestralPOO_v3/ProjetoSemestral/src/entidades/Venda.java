package entidades;

import entidadesEnumerada.StatusPedido;

/**
 * Venda: registro do fechamento (pagamento) de um Pedido.
 * Guarda o valor total no momento da venda. O status da venda é o status do pedido,
 * que continua evoluindo (PAGO, ENVIADO, ENTREGUE) ou pode ser CANCELADO.
 * Implementa Comparable: a ordenação natural é pelo valor da venda.
 */
public class Venda implements Comparable<Venda> {
	private Integer numero;
	private Pedido pedido;
	private Double valorTotal;

	public Venda(Integer numero, Pedido pedido) {
		super();
		this.numero = numero;
		this.pedido = pedido;
		this.valorTotal = pedido.calcularTotal(); // congela o total no momento da venda
	}

	public Integer getNumero() {
		return numero;
	}

	public Pedido getPedido() {
		return pedido;
	}

	public Double getValorTotal() {
		return valorTotal;
	}

	public Cliente getCliente() {
		return pedido.getCliente();
	}

	// O status da venda acompanha o do pedido
	public StatusPedido getStatus() {
		return pedido.getStatus();
	}

	// Ordenação natural: pelo valor da venda
	@Override
	public int compareTo(Venda outra) {
		return this.valorTotal.compareTo(outra.valorTotal);
	}

	@Override
	public String toString() {
		return "Venda " + numero + " - Pedido " + pedido.getNumero() + " - R$" + String.format("%.2f", valorTotal);
	}

}
