package servicos;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

import entidades.Cliente;

/**
 * Serviço de clientes: cadastro, alteração, remoção, busca, ordenação e listagem.
 * O CPF identifica o cliente e não pode se repetir. Os métodos que alteram dados
 * devolvem true se deu certo; se não, mostram o motivo e devolvem false.
 */
public class ServicoCliente {

	private ArrayList<Cliente> clientes = new ArrayList<>();
	// Injetado pelo construtor: usado para impedir a remoção de cliente com pedidos
	private ServicoPedido servicoPedido;

	public ServicoCliente(ServicoPedido servicoPedido) {
		this.servicoPedido = servicoPedido;
	}

	// Verifica se o texto tem só dígitos (compara cada caractere entre '0' e '9')
	private boolean soNumeros(String texto) {
		for (int i = 0; i < texto.length(); i++) {
			char c = texto.charAt(i);
			if (c < '0' || c > '9') {
				return false;
			}
		}
		return true;
	}

	// Validações de cada campo; mostram o erro e devolvem false se o dado for inválido
	private boolean nomeValido(String nome) {
		if (nome == null || nome.isEmpty()) {
			System.out.println("Erro: o nome não pode ficar em branco.");
			return false;
		}
		return true;
	}

	private boolean emailValido(String email) {
		if (email == null || !email.contains("@") || !email.contains(".")) {
			System.out.println("Erro: e-mail inválido.");
			return false;
		}
		return true;
	}

	private boolean telefoneValido(String telefone) {
		if (telefone == null || !soNumeros(telefone) || telefone.length() < 10 || telefone.length() > 11) {
			System.out.println("Erro: o telefone deve ter 10 ou 11 dígitos, só números (com DDD).");
			return false;
		}
		return true;
	}

	// Cadastra um cliente depois de validar todos os dados
	public boolean cadastrar(Cliente cliente) {
		if (!nomeValido(cliente.getNome()) || !emailValido(cliente.getEmail())
				|| !telefoneValido(cliente.getTelefone())) {
			return false;
		}
		if (cliente.getCpf() == null || !soNumeros(cliente.getCpf()) || cliente.getCpf().length() != 11) {
			System.out.println("Erro: o CPF deve ter 11 dígitos, só números (sem pontos e traço).");
			return false;
		}
		if (buscarPorCpf(cliente.getCpf()) != null) {
			System.out.println("Erro: já existe um cliente com o CPF " + cliente.getCpf() + ".");
			return false;
		}
		clientes.add(cliente);
		return true;
	}

	// Busca por CPF; devolve null se não encontrar
	public Cliente buscarPorCpf(String cpf) {
		return clientes.stream().filter(c -> c.getCpf().equals(cpf)).findFirst().orElse(null);
	}

	// Busca por trecho do nome
	public List<Cliente> buscarPorNome(String trecho) {
		return clientes.stream().filter(c -> c.getNome().toLowerCase().contains(trecho.toLowerCase()))
				.collect(Collectors.toList());
	}

	// Altera os dados; campos nulos ou em branco mantêm o valor atual
	public boolean alterar(String cpf, String novoNome, String novoEmail, String novoTelefone) {
		Cliente cliente = buscarPorCpf(cpf);
		if (cliente == null) {
			System.out.println("Erro: cliente com CPF " + cpf + " não encontrado.");
			return false;
		}
		boolean temNome = novoNome != null && !novoNome.isEmpty();
		boolean temEmail = novoEmail != null && !novoEmail.isEmpty();
		boolean temTelefone = novoTelefone != null && !novoTelefone.isEmpty();
		// Valida tudo antes de alterar
		if (temEmail && !emailValido(novoEmail)) {
			return false;
		}
		if (temTelefone && !telefoneValido(novoTelefone)) {
			return false;
		}
		if (temNome) {
			cliente.setNome(novoNome);
		}
		if (temEmail) {
			cliente.setEmail(novoEmail);
		}
		if (temTelefone) {
			cliente.setTelefone(novoTelefone);
		}
		return true;
	}

	// Remove o cliente, desde que ele não tenha pedidos
	public boolean remover(String cpf) {
		Cliente cliente = buscarPorCpf(cpf);
		if (cliente == null) {
			System.out.println("Erro: cliente com CPF " + cpf + " não encontrado.");
			return false;
		}
		if (servicoPedido.existePedidoDoCliente(cliente)) {
			System.out.println("Erro: o cliente possui pedidos e não pode ser excluído.");
			return false;
		}
		clientes.remove(cliente);
		return true;
	}

	public List<Cliente> listarTodos() {
		return new ArrayList<>(clientes);
	}

	// Ordena por nome usando o compareTo de Cliente (interface Comparable)
	public List<Cliente> listarOrdenadoPorNome() {
		List<Cliente> copia = new ArrayList<>(clientes);
		Collections.sort(copia);
		return copia;
	}

	// Imprime a lista em formato de tabela
	public void listarTabela(List<Cliente> lista) {
		if (lista.isEmpty()) {
			System.out.println("Nenhum registro encontrado.");
			return;
		}
		System.out.printf("%-20s %-28s %-13s %-12s%n", "NOME", "EMAIL", "CPF", "TELEFONE");
		for (Cliente c : lista) {
			System.out.printf("%-20s %-28s %-13s %-12s%n", c.getNome(), c.getEmail(), c.getCpf(), c.getTelefone());
		}
	}

	// Apaga todos os clientes (usado ao restaurar os dados de teste)
	public void limpar() {
		clientes.clear();
	}

}
