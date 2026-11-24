package visao;

import java.util.ArrayList;
import java.util.List;

import entidades.Compra;
import entidades.Produto;
import persistencia.ArmazenamentoArquivo;
import servicos.Loja;
import servicos.ServicoCompra;
import servicos.ServicoProduto;

/**
 * Menu de compras (reposição de estoque): chama o ServicoCompra.
 * Cada ação devolve true se alterou os dados com sucesso.
 */
public class MenuCompras extends Menu {

	private ServicoCompra servicoCompra;
	private ServicoProduto servicoProduto;

	public MenuCompras(Console console, Loja loja, ArmazenamentoArquivo armazenamento) {
		super(console, loja, armazenamento);
		this.servicoCompra = loja.getServicoCompra();
		this.servicoProduto = loja.getServicoProduto();
	}

	@Override
	protected String getTitulo() {
		return "COMPRAS";
	}

	@Override
	protected String[] getOpcoes() {
		return new String[] { "Registrar compra", "Alterar fornecedor/custo", "Excluir compra (estorna o estoque)",
				"Listar", "Buscar", "Listar ordenado por total" };
	}

	@Override
	protected boolean tratarOpcao(int opcao) {
		switch (opcao) {
		case 1:
			return registrar();
		case 2:
			return alterar();
		case 3:
			return excluir();
		case 4:
			servicoCompra.listarTabela(servicoCompra.listarTodas());
			return false;
		case 5:
			buscar();
			return false;
		case 6:
			servicoCompra.listarTabela(servicoCompra.listarOrdenadoPorTotal());
			return false;
		default:
			return false;
		}
	}

	private boolean registrar() {
		String fornecedor = console.lerTexto("Fornecedor: ");
		Integer codigo = console.lerInt("Código do produto: ");
		Produto produto = servicoProduto.buscarPorCodigo(codigo);
		if (produto == null) {
			System.out.println("Erro: produto " + codigo + " não encontrado.");
			return false;
		}
		Integer quantidade = console.lerInt("Quantidade comprada: ");
		Double custo = console.lerDouble("Custo unitário: ");
		Compra compra = servicoCompra.registrar(fornecedor, produto, quantidade, custo);
		if (compra == null) {
			return false;
		}
		System.out.printf("Compra %d registrada. Total: R$ %.2f. Estoque atual: %d%n", compra.getNumero(),
				compra.getTotal(), produto.getQuantidadeEstoque());
		return true;
	}

	private boolean alterar() {
		Integer numero = console.lerInt("Número da compra: ");
		String fornecedor = console.lerTexto("Novo fornecedor (Enter mantém): ");
		Double custo = console.lerDoubleOuNulo("Novo custo unitário (Enter mantém): ");
		boolean ok = servicoCompra.alterar(numero, fornecedor, custo);
		if (ok) {
			System.out.println("Compra alterada.");
		}
		return ok;
	}

	private boolean excluir() {
		boolean ok = servicoCompra.remover(console.lerInt("Número da compra: "));
		if (ok) {
			System.out.println("Compra excluída e estoque estornado.");
		}
		return ok;
	}

	private void buscar() {
		int tipo = console.lerInt("Buscar por (1-Número, 2-Fornecedor): ");
		List<Compra> resultado = new ArrayList<>();
		if (tipo == 1) {
			Compra compra = servicoCompra.buscarPorNumero(console.lerInt("Número: "));
			if (compra != null) {
				resultado.add(compra);
			}
		} else if (tipo == 2) {
			resultado = servicoCompra.buscarPorFornecedor(console.lerTexto("Fornecedor (ou parte): "));
		} else {
			System.out.println("Erro: opção inválida.");
			return;
		}
		servicoCompra.listarTabela(resultado);
	}

}
