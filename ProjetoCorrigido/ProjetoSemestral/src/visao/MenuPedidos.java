package visao;

import java.util.ArrayList;
import java.util.List;

import entidades.Cliente;
import entidades.Pedido;
import entidades.Produto;
import entidadesEnumerada.StatusPedido;
import persistencia.ArmazenamentoArquivo;
import servicos.Loja;
import servicos.ServicoCliente;
import servicos.ServicoPedido;
import servicos.ServicoProduto;

/**
 * Menu de pedidos: cria pedidos, mexe nos itens e no status e chama o ServicoPedido.
 * O pagamento de um pedido é feito no menu de Vendas.
 * Cada ação devolve true se alterou os dados com sucesso.
 */
public class MenuPedidos extends Menu {

	private ServicoPedido servicoPedido;
	private ServicoCliente servicoCliente;
	private ServicoProduto servicoProduto;

	public MenuPedidos(Console console, Loja loja, ArmazenamentoArquivo armazenamento) {
		super(console, loja, armazenamento);
		this.servicoPedido = loja.getServicoPedido();
		this.servicoCliente = loja.getServicoCliente();
		this.servicoProduto = loja.getServicoProduto();
	}

	@Override
	protected String getTitulo() {
		return "PEDIDOS";
	}

	@Override
	protected String[] getOpcoes() {
		return new String[] { "Novo pedido", "Adicionar item", "Remover item", "Alterar forma de pagamento",
				"Alterar status (enviar, entregar, cancelar)", "Excluir pedido pendente", "Listar",
				"Detalhar (ver itens)", "Buscar", "Listar ordenado por total" };
	}

	@Override
	protected boolean tratarOpcao(int opcao) {
		switch (opcao) {
		case 1:
			return novo();
		case 2:
			return adicionarItem();
		case 3:
			return removerItem();
		case 4:
			return alterarFormaPagamento();
		case 5:
			return alterarStatus();
		case 6:
			return excluir();
		case 7:
			servicoPedido.listarTabela(servicoPedido.listarTodos());
			return false;
		case 8:
			detalhar();
			return false;
		case 9:
			buscar();
			return false;
		case 10:
			servicoPedido.listarTabela(servicoPedido.listarOrdenadoPorTotal());
			return false;
		default:
			return false;
		}
	}

	private boolean novo() {
		String cpf = console.lerTexto("CPF do cliente: ");
		Cliente cliente = servicoCliente.buscarPorCpf(cpf);
		if (cliente == null) {
			System.out.println("Erro: cliente com CPF " + cpf + " não encontrado.");
			return false;
		}
		String forma = console.lerTexto("Forma de pagamento (ex.: PIX, Cartão, Dinheiro): ");
		Pedido pedido = servicoPedido.criar(cliente, forma);
		if (pedido == null) {
			return false;
		}
		System.out.println("Pedido " + pedido.getNumero() + " criado (PENDENTE). Adicione itens no menu.");
		return true;
	}

	private boolean adicionarItem() {
		Integer numero = console.lerInt("Número do pedido: ");
		Integer codigo = console.lerInt("Código do produto: ");
		Produto produto = servicoProduto.buscarPorCodigo(codigo);
		if (produto == null) {
			System.out.println("Erro: produto " + codigo + " não encontrado.");
			return false;
		}
		Integer quantidade = console.lerInt("Quantidade: ");
		boolean ok = servicoPedido.adicionarItem(numero, produto, quantidade);
		if (ok) {
			System.out.println("Item adicionado. Estoque restante: " + produto.getQuantidadeEstoque());
		}
		return ok;
	}

	private boolean removerItem() {
		Integer numero = console.lerInt("Número do pedido: ");
		boolean ok = servicoPedido.removerItem(numero, console.lerInt("Código do produto: "));
		if (ok) {
			System.out.println("Item removido e estoque devolvido.");
		}
		return ok;
	}

	private boolean alterarFormaPagamento() {
		Integer numero = console.lerInt("Número do pedido: ");
		boolean ok = servicoPedido.alterarFormaPagamento(numero, console.lerTexto("Nova forma de pagamento: "));
		if (ok) {
			System.out.println("Forma de pagamento alterada.");
		}
		return ok;
	}

	private boolean alterarStatus() {
		Integer numero = console.lerInt("Número do pedido: ");
		int opcao = console.lerInt("Novo status (1-ENVIADO, 2-ENTREGUE, 3-CANCELADO): ");
		StatusPedido novo;
		if (opcao == 1) {
			novo = StatusPedido.ENVIADO;
		} else if (opcao == 2) {
			novo = StatusPedido.ENTREGUE;
		} else if (opcao == 3) {
			novo = StatusPedido.CANCELADO;
		} else {
			System.out.println("Erro: opção de status inválida.");
			return false;
		}
		boolean ok = servicoPedido.alterarStatus(numero, novo);
		if (ok) {
			System.out.println("Status alterado para " + novo + ".");
		}
		return ok;
	}

	private boolean excluir() {
		boolean ok = servicoPedido.remover(console.lerInt("Número do pedido: "));
		if (ok) {
			System.out.println("Pedido excluído e estoque devolvido.");
		}
		return ok;
	}

	private void detalhar() {
		Integer numero = console.lerInt("Número do pedido: ");
		Pedido pedido = servicoPedido.buscarPorNumero(numero);
		if (pedido == null) {
			System.out.println("Erro: pedido " + numero + " não encontrado.");
			return;
		}
		servicoPedido.detalhar(pedido);
	}

	private void buscar() {
		int tipo = console.lerInt("Buscar por (1-Número, 2-Nome do cliente): ");
		List<Pedido> resultado = new ArrayList<>();
		if (tipo == 1) {
			Pedido pedido = servicoPedido.buscarPorNumero(console.lerInt("Número: "));
			if (pedido != null) {
				resultado.add(pedido);
			}
		} else if (tipo == 2) {
			resultado = servicoPedido.buscarPorCliente(console.lerTexto("Nome (ou parte): "));
		} else {
			System.out.println("Erro: opção inválida.");
			return;
		}
		servicoPedido.listarTabela(resultado);
	}

}
