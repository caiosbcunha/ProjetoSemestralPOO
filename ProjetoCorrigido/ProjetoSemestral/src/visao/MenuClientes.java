package visao;

import java.util.ArrayList;
import java.util.List;

import entidades.Cliente;
import persistencia.ArmazenamentoArquivo;
import servicos.Loja;
import servicos.ServicoCliente;

/**
 * Menu de clientes: lê os dados do teclado e chama o ServicoCliente.
 * Cada ação devolve true se alterou os dados com sucesso.
 */
public class MenuClientes extends Menu {

	private ServicoCliente servicoCliente;

	public MenuClientes(Console console, Loja loja, ArmazenamentoArquivo armazenamento) {
		super(console, loja, armazenamento);
		this.servicoCliente = loja.getServicoCliente();
	}

	@Override
	protected String getTitulo() {
		return "CLIENTES";
	}

	@Override
	protected String[] getOpcoes() {
		return new String[] { "Incluir", "Alterar", "Excluir", "Listar", "Buscar", "Listar ordenado por nome" };
	}

	@Override
	protected boolean tratarOpcao(int opcao) {
		switch (opcao) {
		case 1:
			return incluir();
		case 2:
			return alterar();
		case 3:
			return excluir();
		case 4:
			servicoCliente.listarTabela(servicoCliente.listarTodos());
			return false;
		case 5:
			buscar();
			return false;
		case 6:
			servicoCliente.listarTabela(servicoCliente.listarOrdenadoPorNome());
			return false;
		default:
			return false;
		}
	}

	private boolean incluir() {
		String nome = console.lerTexto("Nome: ");
		String email = console.lerTexto("E-mail: ");
		String cpf = console.lerTexto("CPF (11 dígitos, só números): ");
		String telefone = console.lerTexto("Telefone com DDD (só números): ");
		boolean ok = servicoCliente.cadastrar(new Cliente(nome, email, cpf, telefone));
		if (ok) {
			System.out.println("Cliente cadastrado.");
		}
		return ok;
	}

	private boolean alterar() {
		String cpf = console.lerTexto("CPF do cliente: ");
		Cliente atual = servicoCliente.buscarPorCpf(cpf);
		if (atual == null) {
			System.out.println("Erro: cliente com CPF " + cpf + " não encontrado.");
			return false;
		}
		System.out.println("Cliente atual: " + atual);
		String nome = console.lerTexto("Novo nome (Enter mantém): ");
		String email = console.lerTexto("Novo e-mail (Enter mantém): ");
		String telefone = console.lerTexto("Novo telefone (Enter mantém): ");
		boolean ok = servicoCliente.alterar(cpf, nome, email, telefone);
		if (ok) {
			System.out.println("Cliente alterado.");
		}
		return ok;
	}

	private boolean excluir() {
		boolean ok = servicoCliente.remover(console.lerTexto("CPF do cliente: "));
		if (ok) {
			System.out.println("Cliente excluído.");
		}
		return ok;
	}

	private void buscar() {
		int tipo = console.lerInt("Buscar por (1-CPF, 2-Nome): ");
		List<Cliente> resultado = new ArrayList<>();
		if (tipo == 1) {
			Cliente cliente = servicoCliente.buscarPorCpf(console.lerTexto("CPF: "));
			if (cliente != null) {
				resultado.add(cliente);
			}
		} else if (tipo == 2) {
			resultado = servicoCliente.buscarPorNome(console.lerTexto("Nome (ou parte): "));
		} else {
			System.out.println("Erro: opção inválida.");
			return;
		}
		servicoCliente.listarTabela(resultado);
	}

}
