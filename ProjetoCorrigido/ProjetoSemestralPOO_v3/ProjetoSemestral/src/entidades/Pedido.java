package entidades;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import entidadesEnumerada.StatusPedido;

/**
 * Pedido: agrupa um cliente e vários itens (composição).
 * Ciclo de vida: PENDENTE -> PAGO -> ENVIADO -> ENTREGUE (ou CANCELADO).
 * Itens só mudam enquanto o pedido está PENDENTE.
 * Implementa Comparable: a ordenação natural é pelo total do pedido.
 */
public class Pedido implements Comparable<Pedido> {
	private Integer numero;
	private Cliente cliente;
	private String formaPagamento;
	private StatusPedido status;
	private ArrayList<ItemPedido> itens;

	// Todo pedido nasce PENDENTE e sem itens
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

	// Devolve uma visão somente leitura: quem chama não consegue incluir nem remover
	// itens por fora (isso só pode ser feito por adicionarItem e removerItem, que
	// controlam o estoque e o status do pedido).
	public List<ItemPedido> getItens() {
		return Collections.unmodifiableList(itens);
	}

	// Dá baixa no estoque e só então inclui o item. Se o produto já está no pedido,
	// soma a quantidade ao item existente. Devolve false se o pedido não estiver
	// pendente, se o produto ou a quantidade não forem informados, ou se a quantidade
	// for inválida ou maior que o estoque.
	public boolean adicionarItem(Produto produto, Integer quantidade) {
		if (status != StatusPedido.PENDENTE || produto == null || quantidade == null) {
			return false;
		}
		if (!produto.saidaEstoque(quantidade)) {
			return false; // quantidade inválida ou sem estoque suficiente
		}
		for (ItemPedido item : itens) {
			if (item.getProduto().getCodigo().equals(produto.getCodigo())) {
				item.setQuantidade(item.getQuantidade() + quantidade);
				return true;
			}
		}
		itens.add(new ItemPedido(produto, quantidade));
		return true;
	}

	// Remove o item do produto e devolve a quantidade ao estoque.
	// Devolve false se o pedido não estiver pendente ou se o produto não estiver nele.
	public boolean removerItem(Integer codigoProduto) {
		if (status != StatusPedido.PENDENTE) {
			return false;
		}
		for (ItemPedido item : itens) {
			if (item.getProduto().getCodigo().equals(codigoProduto)) {
				item.getProduto().entradaEstoque(item.getQuantidade());
				itens.remove(item);
				return true; // sai logo após o remove (evita ConcurrentModificationException)
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

	// Cancela o pedido e devolve ao estoque todos os itens.
	// Pedido já entregue ou cancelado não pode ser cancelado (devolve false).
	public boolean cancelar() {
		if (status == StatusPedido.ENTREGUE || status == StatusPedido.CANCELADO) {
			return false;
		}
		for (ItemPedido item : itens) {
			item.getProduto().entradaEstoque(item.getQuantidade()); // devolve o estoque
		}
		status = StatusPedido.CANCELADO;
		return true;
	}

	// Só aceita a transição seguinte do ciclo de vida:
	// PENDENTE -> PAGO (precisa ter itens) -> ENVIADO -> ENTREGUE.
	// CANCELADO é permitido até antes de ENTREGUE e passa por cancelar(), para o
	// estoque ser devolvido. Pedido entregue ou cancelado não muda mais de status.
	// Devolve false (sem alterar nada) se a transição não for permitida.
	public boolean alterarStatus(StatusPedido novoStatus) {
		if (novoStatus == null) {
			return false;
		}
		switch (novoStatus) {
		case CANCELADO:
			return cancelar();
		case PAGO:
			if (status != StatusPedido.PENDENTE || itens.isEmpty()) {
				return false;
			}
			break;
		case ENVIADO:
			if (status != StatusPedido.PAGO) {
				return false;
			}
			break;
		case ENTREGUE:
			if (status != StatusPedido.ENVIADO) {
				return false;
			}
			break;
		default: // PENDENTE: o pedido nunca volta a ser pendente
			return false;
		}
		this.status = novoStatus;
		return true;
	}

	// Ordenação natural: pelo total do pedido
	@Override
	public int compareTo(Pedido outro) {
		return this.calcularTotal().compareTo(outro.calcularTotal());
	}

	// ---- Usados SOMENTE pela leitura de arquivo (persistência) ----
	// Não mexem no estoque: o estoque gravado no arquivo já reflete o estado do pedido.

	// Define o status lido do arquivo
	public void restaurarStatus(StatusPedido statusLido) {
		this.status = statusLido;
	}

	// Inclui um item lido do arquivo (com o preço congelado na época da venda)
	public void restaurarItem(ItemPedido item) {
		itens.add(item);
	}

}
