package visao;

import java.util.ArrayList;
import java.util.List;

import entidades.Pedido;
import entidades.Venda;
import persistencia.ArmazenamentoArquivo;
import servicos.Loja;
import servicos.ServicoPedido;
import servicos.ServicoVenda;

/**
 * Menu de vendas: paga pedidos pendentes, cancela vendas e chama o ServicoVenda.
 * Cada ação devolve true se alterou os dados com sucesso.
 */
public class MenuVendas extends Menu {

	private ServicoVenda servicoVenda;
	private ServicoPedido servicoPedido;

	public MenuVendas(Console console, Loja loja, ArmazenamentoArquivo armazenamento) {
		super(console, loja, armazenamento);
		this.servicoVenda = loja.getServicoVenda();
		this.servicoPedido = loja.getServicoPedido();
	}

	@Override
	protected String getTitulo() {
		return "VENDAS";
	}

	@Override
	protected String[] getOpcoes() {
		return new String[] { "Registrar venda (pagar pedido pendente)", "Cancelar venda (devolve o estoque)",
				"Excluir venda cancelada", "Listar", "Buscar", "Listar ordenado por valor" };
	}

	@Override
	protected boolean tratarOpcao(int opcao) {
		switch (opcao) {
		case 1:
			return registrar();
		case 2:
			return cancelar();
		case 3:
			return excluir();
		case 4:
			servicoVenda.listarTabela(servicoVenda.listarTodas());
			return false;
		case 5:
			buscar();
			return false;
		case 6:
			servicoVenda.listarTabela(servicoVenda.listarOrdenadoPorValor());
			return false;
		default:
			return false;
		}
	}

	private boolean registrar() {
		Integer numero = console.lerInt("Número do pedido a vender: ");
		Pedido pedido = servicoPedido.buscarPorNumero(numero);
		if (pedido == null) {
			System.out.println("Erro: pedido " + numero + " não encontrado.");
			return false;
		}
		Venda venda = servicoVenda.registrar(pedido);
		if (venda == null) {
			return false;
		}
		System.out.printf("Venda %d registrada. Total: R$ %.2f. Pedido %d agora está %s.%n", venda.getNumero(),
				venda.getValorTotal(), pedido.getNumero(), pedido.getStatus());
		return true;
	}

	private boolean cancelar() {
		boolean ok = servicoVenda.cancelar(console.lerInt("Número da venda: "));
		if (ok) {
			System.out.println("Venda cancelada e estoque devolvido.");
		}
		return ok;
	}

	private boolean excluir() {
		boolean ok = servicoVenda.remover(console.lerInt("Número da venda: "));
		if (ok) {
			System.out.println("Venda excluída.");
		}
		return ok;
	}

	private void buscar() {
		int tipo = console.lerInt("Buscar por (1-Número, 2-Nome do cliente): ");
		List<Venda> resultado = new ArrayList<>();
		if (tipo == 1) {
			Venda venda = servicoVenda.buscarPorNumero(console.lerInt("Número: "));
			if (venda != null) {
				resultado.add(venda);
			}
		} else if (tipo == 2) {
			resultado = servicoVenda.buscarPorCliente(console.lerTexto("Nome (ou parte): "));
		} else {
			System.out.println("Erro: opção inválida.");
			return;
		}
		servicoVenda.listarTabela(resultado);
	}

}
