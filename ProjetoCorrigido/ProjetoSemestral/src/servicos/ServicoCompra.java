package servicos;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

import entidades.Compra;
import entidades.Produto;

/**
 * Serviço de compras (reposição de estoque). Registrar uma compra dá entrada
 * no estoque do produto; remover uma compra estorna essa entrada.
 * Os métodos que alteram dados mostram o motivo na tela quando não dão certo.
 */
public class ServicoCompra {

	private ArrayList<Compra> compras = new ArrayList<>();
	private int proximoNumero = 1; // numeração automática das compras

	// Valida os dados, registra a compra e dá entrada no estoque; devolve null se inválida
	public Compra registrar(String fornecedor, Produto produto, Integer quantidade, Double custoUnitario) {
		if (fornecedor == null || fornecedor.isEmpty()) {
			System.out.println("Erro: o fornecedor não pode ficar em branco.");
			return null;
		}
		if (produto == null) {
			System.out.println("Erro: produto da compra não encontrado.");
			return null;
		}
		if (quantidade == null || quantidade <= 0) {
			System.out.println("Erro: a quantidade comprada deve ser maior que zero.");
			return null;
		}
		if (custoUnitario == null || custoUnitario <= 0) {
			System.out.println("Erro: o custo unitário deve ser maior que zero.");
			return null;
		}
		Compra compra = new Compra(proximoNumero, fornecedor, produto, quantidade, custoUnitario);
		produto.entradaEstoque(quantidade);
		proximoNumero++;
		compras.add(compra);
		return compra;
	}

	// Busca por número; devolve null se não encontrar
	public Compra buscarPorNumero(Integer numero) {
		return compras.stream().filter(c -> c.getNumero().equals(numero)).findFirst().orElse(null);
	}

	// Busca por trecho do nome do fornecedor
	public List<Compra> buscarPorFornecedor(String trecho) {
		return compras.stream().filter(c -> c.getFornecedor().toLowerCase().contains(trecho.toLowerCase()))
				.collect(Collectors.toList());
	}

	// Altera fornecedor e custo unitário; valores nulos ou em branco mantêm o atual.
	// A quantidade não muda, porque já gerou entrada no estoque.
	public boolean alterar(Integer numero, String novoFornecedor, Double novoCusto) {
		Compra compra = buscarPorNumero(numero);
		if (compra == null) {
			System.out.println("Erro: compra " + numero + " não encontrada.");
			return false;
		}
		if (novoCusto != null && novoCusto <= 0) {
			System.out.println("Erro: o custo unitário deve ser maior que zero.");
			return false;
		}
		if (novoCusto != null) {
			compra.setCustoUnitario(novoCusto);
		}
		if (novoFornecedor != null && !novoFornecedor.isEmpty()) {
			compra.setFornecedor(novoFornecedor);
		}
		return true;
	}

	// Remove a compra e estorna a entrada no estoque.
	// Se parte do estoque já foi vendida, não dá para estornar.
	public boolean remover(Integer numero) {
		Compra compra = buscarPorNumero(numero);
		if (compra == null) {
			System.out.println("Erro: compra " + numero + " não encontrada.");
			return false;
		}
		if (!compra.getProduto().saidaEstoque(compra.getQuantidade())) {
			System.out.println("Erro: não é possível excluir, parte do estoque desta compra já foi vendida.");
			return false;
		}
		compras.remove(compra);
		return true;
	}

	// Usado pela regra de remoção de produtos
	public boolean existeCompraDoProduto(Produto produto) {
		for (Compra compra : compras) {
			if (compra.getProduto() == produto) {
				return true;
			}
		}
		return false;
	}

	public List<Compra> listarTodas() {
		return new ArrayList<>(compras);
	}

	// Ordena pelo total gasto usando o compareTo de Compra (interface Comparable)
	public List<Compra> listarOrdenadoPorTotal() {
		List<Compra> copia = new ArrayList<>(compras);
		Collections.sort(copia);
		return copia;
	}

	// Imprime a lista em formato de tabela
	public void listarTabela(List<Compra> lista) {
		if (lista.isEmpty()) {
			System.out.println("Nenhum registro encontrado.");
			return;
		}
		System.out.printf("%-5s %-20s %-24s %5s %12s %12s%n", "NUM", "FORNECEDOR", "PRODUTO", "QTD", "CUSTO UN.",
				"TOTAL");
		for (Compra c : lista) {
			System.out.printf("%-5d %-20s %-24s %5d %12.2f %12.2f%n", c.getNumero(), c.getFornecedor(),
					c.getProduto().getNome(), c.getQuantidade(), c.getCustoUnitario(), c.getTotal());
		}
	}

	// Usado pela leitura de arquivo: devolve a compra à lista SEM dar entrada no estoque
	// (o estoque gravado no arquivo já inclui essa compra)
	public void restaurar(Compra compra) {
		compras.add(compra);
		if (compra.getNumero() >= proximoNumero) {
			proximoNumero = compra.getNumero() + 1;
		}
	}

	// Apaga todas as compras e reinicia a numeração (restaurar dados de teste)
	public void limpar() {
		compras.clear();
		proximoNumero = 1;
	}

}
