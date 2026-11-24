package servicos;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

import entidades.Cliente;
import entidades.ItemPedido;
import entidades.Pedido;
import entidades.Produto;
import entidadesEnumerada.StatusPedido;

/**
 * Serviço de pedidos: criação, itens, status, remoção, busca, ordenação e listagem.
 * Cuida do ciclo de vida PENDENTE -> PAGO -> ENVIADO -> ENTREGUE (ou CANCELADO).
 * O pagamento (PAGO) é feito por ServicoVenda, ao registrar a venda.
 * Os métodos que alteram dados devolvem true se deu certo; se não, mostram o
 * motivo e devolvem false.
 */
public class ServicoPedido {

	private ArrayList<Pedido> pedidos = new ArrayList<>();
	private int proximoNumero = 1; // numeração automática dos pedidos

	// Cria um pedido PENDENTE para o cliente; devolve null se os dados forem inválidos
	public Pedido criar(Cliente cliente, String formaPagamento) {
		if (cliente == null) {
			System.out.println("Erro: cliente do pedido não informado.");
			return null;
		}
		if (formaPagamento == null || formaPagamento.isEmpty()) {
			System.out.println("Erro: a forma de pagamento não pode ficar em branco.");
			return null;
		}
		Pedido pedido = new Pedido(proximoNumero, cliente, formaPagamento);
		proximoNumero++;
		pedidos.add(pedido);
		return pedido;
	}

	// Busca por número; devolve null se não encontrar
	public Pedido buscarPorNumero(Integer numero) {
		return pedidos.stream().filter(p -> p.getNumero().equals(numero)).findFirst().orElse(null);
	}

	// Busca por trecho do nome do cliente
	public List<Pedido> buscarPorCliente(String trecho) {
		return pedidos.stream()
				.filter(p -> p.getCliente().getNome().toLowerCase().contains(trecho.toLowerCase()))
				.collect(Collectors.toList());
	}

	// Adiciona um item ao pedido, dando baixa no estoque
	public boolean adicionarItem(Integer numeroPedido, Produto produto, Integer quantidade) {
		if (produto == null) {
			System.out.println("Erro: produto não informado.");
			return false;
		}
		if (quantidade == null) {
			System.out.println("Erro: quantidade não informada.");
			return false;
		}
		Pedido pedido = buscarPorNumero(numeroPedido);
		if (pedido == null) {
			System.out.println("Erro: pedido " + numeroPedido + " não encontrado.");
			return false;
		}
		if (pedido.getStatus() != StatusPedido.PENDENTE) {
			System.out.println("Erro: só é possível alterar itens de um pedido pendente.");
			return false;
		}
		if (!pedido.adicionarItem(produto, quantidade)) {
			System.out.println("Erro: quantidade inválida ou estoque insuficiente (disponível: "
					+ produto.getQuantidadeEstoque() + ").");
			return false;
		}
		return true;
	}

	// Remove o item do pedido, devolvendo a quantidade ao estoque
	public boolean removerItem(Integer numeroPedido, Integer codigoProduto) {
		Pedido pedido = buscarPorNumero(numeroPedido);
		if (pedido == null) {
			System.out.println("Erro: pedido " + numeroPedido + " não encontrado.");
			return false;
		}
		if (pedido.getStatus() != StatusPedido.PENDENTE) {
			System.out.println("Erro: só é possível alterar itens de um pedido pendente.");
			return false;
		}
		if (!pedido.removerItem(codigoProduto)) {
			System.out.println("Erro: o produto " + codigoProduto + " não está no pedido " + numeroPedido + ".");
			return false;
		}
		return true;
	}

	// Altera a forma de pagamento (só enquanto o pedido está pendente)
	public boolean alterarFormaPagamento(Integer numero, String novaForma) {
		Pedido pedido = buscarPorNumero(numero);
		if (pedido == null) {
			System.out.println("Erro: pedido " + numero + " não encontrado.");
			return false;
		}
		if (novaForma == null || novaForma.isEmpty()) {
			System.out.println("Erro: a forma de pagamento não pode ficar em branco.");
			return false;
		}
		if (pedido.getStatus() != StatusPedido.PENDENTE) {
			System.out.println("Erro: a forma de pagamento só pode mudar em pedido pendente.");
			return false;
		}
		pedido.setFormaPagamento(novaForma);
		return true;
	}

	// Altera o status respeitando a ordem: PAGO -> ENVIADO -> ENTREGUE, ou CANCELADO
	public boolean alterarStatus(Integer numero, StatusPedido novo) {
		Pedido pedido = buscarPorNumero(numero);
		if (pedido == null) {
			System.out.println("Erro: pedido " + numero + " não encontrado.");
			return false;
		}
		if (novo == StatusPedido.ENVIADO) {
			if (pedido.getStatus() != StatusPedido.PAGO) {
				System.out.println("Erro: só é possível enviar um pedido pago.");
				return false;
			}
		} else if (novo == StatusPedido.ENTREGUE) {
			if (pedido.getStatus() != StatusPedido.ENVIADO) {
				System.out.println("Erro: só é possível entregar um pedido enviado.");
				return false;
			}
		} else if (novo != StatusPedido.CANCELADO) {
			System.out.println("Erro: status não permitido aqui. Para pagar um pedido, registre uma venda.");
			return false;
		}
		if (!pedido.alterarStatus(novo)) {
			System.out.println("Erro: o pedido " + numero + " está " + pedido.getStatus() + " e não pode mudar para " + novo + ".");
			return false;
		}
		return true;
	}

	// Só pedidos pendentes podem ser excluídos; o estoque dos itens volta
	public boolean remover(Integer numero) {
		Pedido pedido = buscarPorNumero(numero);
		if (pedido == null) {
			System.out.println("Erro: pedido " + numero + " não encontrado.");
			return false;
		}
		if (pedido.getStatus() != StatusPedido.PENDENTE) {
			System.out.println("Erro: só é possível excluir pedidos pendentes. Pedidos já vendidos ficam no histórico.");
			return false;
		}
		pedido.cancelar(); // devolve o estoque
		pedidos.remove(pedido);
		return true;
	}

	// Consultas usadas pelas regras de remoção de clientes e produtos
	public boolean existePedidoDoCliente(Cliente cliente) {
		for (Pedido pedido : pedidos) {
			if (pedido.getCliente() == cliente) {
				return true;
			}
		}
		return false;
	}

	public boolean existePedidoComProduto(Produto produto) {
		for (Pedido pedido : pedidos) {
			for (ItemPedido item : pedido.getItens()) {
				if (item.getProduto() == produto) {
					return true;
				}
			}
		}
		return false;
	}

	public List<Pedido> listarTodos() {
		return new ArrayList<>(pedidos);
	}

	// Ordena pelo total do pedido usando o compareTo de Pedido (interface Comparable)
	public List<Pedido> listarOrdenadoPorTotal() {
		List<Pedido> copia = new ArrayList<>(pedidos);
		Collections.sort(copia);
		return copia;
	}

	// Imprime a lista em formato de tabela
	public void listarTabela(List<Pedido> lista) {
		if (lista.isEmpty()) {
			System.out.println("Nenhum registro encontrado.");
			return;
		}
		System.out.printf("%-5s %-20s %-12s %-10s %5s %12s%n", "NUM", "CLIENTE", "PAGAMENTO", "STATUS", "ITENS",
				"TOTAL");
		for (Pedido p : lista) {
			System.out.printf("%-5d %-20s %-12s %-10s %5d %12.2f%n", p.getNumero(), p.getCliente().getNome(),
					p.getFormaPagamento(), p.getStatus(), p.getItens().size(), p.calcularTotal());
		}
	}

	// Mostra um pedido com a tabela dos seus itens
	public void detalhar(Pedido pedido) {
		System.out.println("Pedido " + pedido.getNumero() + " | Cliente: " + pedido.getCliente().getNome()
				+ " | Pagamento: " + pedido.getFormaPagamento() + " | Status: " + pedido.getStatus());
		if (pedido.getItens().isEmpty()) {
			System.out.println("(sem itens)");
			return;
		}
		System.out.printf("%-6s %-24s %5s %12s %12s%n", "COD", "PRODUTO", "QTD", "UNITARIO", "SUBTOTAL");
		for (ItemPedido item : pedido.getItens()) {
			System.out.printf("%-6d %-24s %5d %12.2f %12.2f%n", item.getProduto().getCodigo(),
					item.getProduto().getNome(), item.getQuantidade(), item.getPrecoUnitario(), item.getSubtotal());
		}
		System.out.printf("TOTAL: R$ %.2f%n", pedido.calcularTotal());
	}

	// Usado pela leitura de arquivo: devolve um pedido à lista sem mexer no estoque
	// e ajusta a numeração para não repetir números já existentes
	public void restaurar(Pedido pedido) {
		pedidos.add(pedido);
		if (pedido.getNumero() >= proximoNumero) {
			proximoNumero = pedido.getNumero() + 1;
		}
	}

	// Apaga todos os pedidos e reinicia a numeração (restaurar dados de teste)
	public void limpar() {
		pedidos.clear();
		proximoNumero = 1;
	}

}
