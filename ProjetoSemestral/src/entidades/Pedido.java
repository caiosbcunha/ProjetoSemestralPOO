package entidades;

import java.util.ArrayList;

import entidadesEnumerada.StatusPedido;

public class Pedido {
	private Integer numero;
	private Cliente cliente;
	private String formaPagamento;
	private StatusPedido status;
	private ArrayList<ItemPedido> itens;

	public Pedido(Integer numero, Cliente cliente, String formaPagamento) {
		super();
		this.numero = numero;
		this.cliente = cliente;
		this.formaPagamento = formaPagamento;
		this.status = StatusPedido.PENDENTE;
		this.itens = new ArrayList<>();
	}

	public Integer getNumero() {
		return numero;
	}

	public void setNumero(Integer numero) {
		this.numero = numero;
	}

	public Cliente getCliente() {
		return cliente;
	}

	public void setCliente(Cliente cliente) {
		this.cliente = cliente;
	}

	public String getFormaPagamento() {
		return formaPagamento;
	}

	public void setFormaPagamento(String formaPagamento) {
		this.formaPagamento = formaPagamento;
	}

	public StatusPedido getStatus() {
		return status;
	}

	public ArrayList<ItemPedido> getItens() {
		return itens;
	}

	public boolean adicionarItem(Produto produto, Integer quantidade) {
		if (!produto.saidaEstoque(quantidade)) {
			return false; // sem estoque suficiente
		}
		itens.add(new ItemPedido(produto, quantidade));
		return true;
	}

	// Remove o item do produto e devolve a quantidade ao estoque
	public boolean removerItem(Integer codigoProduto) {
		for (ItemPedido item : itens) {
			if (item.getProduto().getCodigo().equals(codigoProduto)) {
				item.getProduto().entradaEstoque(item.getQuantidade());
				itens.remove(item);
				return true; // sai logo após o remove
			}
		}
		return false; // produto não está no pedido
	}

	// Soma os subtotais de todos os itens
	public Double calcularTotal() {
		Double total = 0.0;
		for (ItemPedido item : itens) {
			total += item.getSubtotal();
		}
		return total;
	}

	// Pedido finalizado (entregue ou cancelado) não muda mais de status
	public boolean alterarStatus(StatusPedido novoStatus) {
		if (status == StatusPedido.ENTREGUE || status == StatusPedido.CANCELADO) {
			return false;
		}
		this.status = novoStatus;
		return true;
	}

}
