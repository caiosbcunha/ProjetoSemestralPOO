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

	public void setStatus(StatusPedido status) {
		this.status = status;
	}

	public ArrayList<ItemPedido> getItens() {
		return itens;
	}

	public void setItens(ArrayList<ItemPedido> itens) {
		this.itens = itens;
	}

	public boolean adicionarItem(Produto produto, Integer quantidade) {
		if (!produto.saidaEstoque(quantidade)) {
			return false; // sem estoque suficiente
		}
		itens.add(new ItemPedido(produto, quantidade));
		return true;
	}

	
	
}
