package visao;

import java.util.ArrayList;
import java.util.List;

import entidades.Alimenticio;
import entidades.Eletronico;
import entidades.Produto;
import entidades.Vestuario;
import persistencia.ArmazenamentoArquivo;
import servicos.Loja;
import servicos.ServicoProduto;

/**
 * Menu de produtos: lê os dados do teclado e chama o ServicoProduto.
 * Cada ação devolve true se alterou os dados com sucesso.
 */
public class MenuProdutos extends Menu {

	private ServicoProduto servicoProduto;

	public MenuProdutos(Console console, Loja loja, ArmazenamentoArquivo armazenamento) {
		super(console, loja, armazenamento);
		this.servicoProduto = loja.getServicoProduto();
	}

	@Override
	protected String getTitulo() {
		return "PRODUTOS";
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
			servicoProduto.listarTabela(servicoProduto.listarTodos());
			return false;
		case 5:
			buscar();
			return false;
		case 6:
			servicoProduto.listarTabela(servicoProduto.listarOrdenadoPorNome());
			return false;
		default:
			return false;
		}
	}

	// Pergunta o tipo e os dados e cadastra o produto (polimorfismo: tudo vira Produto)
	private boolean incluir() {
		int tipo = console.lerInt("Tipo (1-Eletrônico, 2-Alimentício, 3-Vestuário): ");
		if (tipo < 1 || tipo > 3) {
			System.out.println("Erro: tipo de produto inválido.");
			return false;
		}
		Integer codigo = console.lerInt("Código: ");
		String nome = console.lerTexto("Nome: ");
		Double preco = console.lerDouble("Preço base: ");
		Integer estoque = console.lerInt("Estoque inicial: ");
		Produto produto;
		if (tipo == 1) {
			produto = new Eletronico(codigo, nome, preco, estoque, console.lerInt("Garantia (meses): "));
		} else if (tipo == 2) {
			produto = new Alimenticio(codigo, nome, preco, estoque, console.lerInt("Dias para vencer: "));
		} else {
			produto = new Vestuario(codigo, nome, preco, estoque, console.lerTamanho(), console.lerTexto("Cor: "));
		}
		boolean ok = servicoProduto.cadastrar(produto);
		if (ok) {
			System.out.println("Produto cadastrado.");
		}
		return ok;
	}

	private boolean alterar() {
		Integer codigo = console.lerInt("Código do produto: ");
		Produto atual = servicoProduto.buscarPorCodigo(codigo);
		if (atual == null) {
			System.out.println("Erro: produto " + codigo + " não encontrado.");
			return false;
		}
		System.out.println("Produto atual: " + atual.getNome() + " | " + atual.getDetalhes());
		String nome = console.lerTexto("Novo nome (Enter mantém): ");
		Double preco = console.lerDoubleOuNulo("Novo preço base (Enter mantém): ");
		String detalhe = console.lerTexto("Novo detalhe: garantia em meses / dias para vencer / cor (Enter mantém): ");
		boolean ok = servicoProduto.alterar(codigo, nome, preco, detalhe);
		if (ok) {
			System.out.println("Produto alterado.");
		}
		return ok;
	}

	private boolean excluir() {
		boolean ok = servicoProduto.remover(console.lerInt("Código do produto: "));
		if (ok) {
			System.out.println("Produto excluído.");
		}
		return ok;
	}

	private void buscar() {
		int tipo = console.lerInt("Buscar por (1-Código, 2-Nome): ");
		List<Produto> resultado = new ArrayList<>();
		if (tipo == 1) {
			Produto produto = servicoProduto.buscarPorCodigo(console.lerInt("Código: "));
			if (produto != null) {
				resultado.add(produto);
			}
		} else if (tipo == 2) {
			resultado = servicoProduto.buscarPorNome(console.lerTexto("Nome (ou parte): "));
		} else {
			System.out.println("Erro: opção inválida.");
			return;
		}
		servicoProduto.listarTabela(resultado);
	}

}
