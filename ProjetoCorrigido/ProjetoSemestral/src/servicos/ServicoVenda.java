package servicos;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

import entidades.Pedido;
import entidades.Venda;
import entidadesEnumerada.StatusPedido;

/**
 * Serviço de vendas: fecha (paga) pedidos pendentes, cancela vendas devolvendo
 * o estoque e mantém o histórico de vendas. Os métodos que alteram dados mostram
 * o motivo na tela quando não dão certo.
 */
public class ServicoVenda {

	private ArrayList<Venda> vendas = new ArrayList<>();
	private int proximoNumero = 1; // numeração automática das vendas

	// Registra a venda de um pedido pendente e com itens; o pedido passa para PAGO.
	// Devolve null se a venda não puder ser feita.
	public Venda registrar(Pedido pedido) {
		if (pedido == null) {
			System.out.println("Erro: pedido não encontrado.");
			return null;
		}
		if (pedido.getStatus() != StatusPedido.PENDENTE) {
			System.out.println("Erro: o pedido " + pedido.getNumero() + " não está pendente (status: "
					+ pedido.getStatus() + ").");
			return null;
		}
		if (pedido.getItens().isEmpty()) {
			System.out.println("Erro: um pedido sem itens não pode ser vendido.");
			return null;
		}
		pedido.alterarStatus(StatusPedido.PAGO);
		Venda venda = new Venda(proximoNumero, pedido); // congela o total
		proximoNumero++;
		vendas.add(venda);
		return venda;
	}

	// Busca por número; devolve null se não encontrar
	public Venda buscarPorNumero(Integer numero) {
		return vendas.stream().filter(v -> v.getNumero().equals(numero)).findFirst().orElse(null);
	}

	// Busca por trecho do nome do cliente
	public List<Venda> buscarPorCliente(String trecho) {
		return vendas.stream().filter(v -> v.getCliente().getNome().toLowerCase().contains(trecho.toLowerCase()))
				.collect(Collectors.toList());
	}

	// Cancela a venda: o pedido vira CANCELADO e o estoque dos itens volta
	public boolean cancelar(Integer numero) {
		Venda venda = buscarPorNumero(numero);
		if (venda == null) {
			System.out.println("Erro: venda " + numero + " não encontrada.");
			return false;
		}
		if (!venda.getPedido().cancelar()) {
			System.out.println("Erro: o pedido " + venda.getPedido().getNumero() + " já está "
					+ venda.getStatus() + " e não pode ser cancelado.");
			return false;
		}
		return true;
	}

	// Remove o registro de uma venda já cancelada (o pedido continua no histórico)
	public boolean remover(Integer numero) {
		Venda venda = buscarPorNumero(numero);
		if (venda == null) {
			System.out.println("Erro: venda " + numero + " não encontrada.");
			return false;
		}
		if (venda.getStatus() != StatusPedido.CANCELADO) {
			System.out.println("Erro: só é possível excluir vendas canceladas.");
			return false;
		}
		vendas.remove(venda);
		return true;
	}

	public List<Venda> listarTodas() {
		return new ArrayList<>(vendas);
	}

	// Ordena pelo valor da venda usando o compareTo de Venda (interface Comparable)
	public List<Venda> listarOrdenadoPorValor() {
		List<Venda> copia = new ArrayList<>(vendas);
		Collections.sort(copia);
		return copia;
	}

	// Imprime a lista em formato de tabela
	public void listarTabela(List<Venda> lista) {
		if (lista.isEmpty()) {
			System.out.println("Nenhum registro encontrado.");
			return;
		}
		System.out.printf("%-5s %-7s %-20s %-12s %-10s %12s%n", "NUM", "PEDIDO", "CLIENTE", "PAGAMENTO", "STATUS",
				"VALOR");
		for (Venda v : lista) {
			System.out.printf("%-5d %-7d %-20s %-12s %-10s %12.2f%n", v.getNumero(), v.getPedido().getNumero(),
					v.getCliente().getNome(), v.getPedido().getFormaPagamento(), v.getStatus(), v.getValorTotal());
		}
	}

	// Usado pela leitura de arquivo: devolve a venda à lista sem alterar o pedido
	public void restaurar(Venda venda) {
		vendas.add(venda);
		if (venda.getNumero() >= proximoNumero) {
			proximoNumero = venda.getNumero() + 1;
		}
	}

	// Apaga todas as vendas e reinicia a numeração (restaurar dados de teste)
	public void limpar() {
		vendas.clear();
		proximoNumero = 1;
	}

}
