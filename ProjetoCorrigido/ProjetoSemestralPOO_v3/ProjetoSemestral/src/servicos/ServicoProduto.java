package servicos;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

import entidades.Alimenticio;
import entidades.Eletronico;
import entidades.Produto;
import entidades.Vestuario;

/**
 * Serviço de produtos: guarda a lista e faz cadastro, alteração, remoção,
 * busca, ordenação e listagem em tabela. Os métodos que alteram dados devolvem
 * true se deu certo; se não deu, mostram o motivo na tela e devolvem false.
 */
public class ServicoProduto {

	private ArrayList<Produto> produtos = new ArrayList<>();
	// Dependências injetadas pelo construtor (injeção de dependência), usadas na remoção
	private ServicoPedido servicoPedido;
	private ServicoCompra servicoCompra;

	public ServicoProduto(ServicoPedido servicoPedido, ServicoCompra servicoCompra) {
		this.servicoPedido = servicoPedido;
		this.servicoCompra = servicoCompra;
	}

	// Cadastra um produto depois de validar os dados e a unicidade do código
	public boolean cadastrar(Produto produto) {
		if (produto == null) {
			System.out.println("Erro: produto não informado.");
			return false;
		}
		if (produto.getCodigo() == null || produto.getCodigo() <= 0) {
			System.out.println("Erro: o código deve ser um número maior que zero.");
			return false;
		}
		if (buscarPorCodigo(produto.getCodigo()) != null) {
			System.out.println("Erro: já existe um produto com o código " + produto.getCodigo() + ".");
			return false;
		}
		if (produto.getNome() == null || produto.getNome().isEmpty()) {
			System.out.println("Erro: o nome não pode ficar em branco.");
			return false;
		}
		if (produto.getPrecoBase() == null || !Double.isFinite(produto.getPrecoBase()) || produto.getPrecoBase() <= 0) {
			System.out.println("Erro: o preço base deve ser um número finito maior que zero.");
			return false;
		}
		if (produto.getQuantidadeEstoque() == null || produto.getQuantidadeEstoque() < 0) {
			System.out.println("Erro: o estoque não pode ser negativo.");
			return false;
		}
		if (produto instanceof Vestuario) {
			Vestuario v = (Vestuario) produto;
			if (v.getCor() == null || v.getCor().isEmpty()) {
				System.out.println("Erro: a cor não pode ficar em branco.");
				return false;
			}
			if (v.getTamanho() == null) {
				System.out.println("Erro: o tamanho não foi informado.");
				return false;
			}
		}
		produtos.add(produto);
		return true;
	}

	// Busca por código; devolve null se não encontrar (stream + filter + findFirst)
	public Produto buscarPorCodigo(Integer codigo) {
		return produtos.stream().filter(p -> p.getCodigo().equals(codigo)).findFirst().orElse(null);
	}

	// Busca por trecho do nome, ignorando maiúsculas e minúsculas
	public List<Produto> buscarPorNome(String trecho) {
		return produtos.stream().filter(p -> p.getNome().toLowerCase().contains(trecho.toLowerCase()))
				.collect(Collectors.toList());
	}

	/**
	 * Altera nome, preço e o detalhe específico do subtipo. Valores nulos ou em
	 * branco mantêm o dado atual. Usa instanceof + downcasting para chegar ao
	 * atributo do subtipo (garantia, dias para vencer ou cor).
	 */
	public boolean alterar(Integer codigo, String novoNome, Double novoPreco, String novoDetalhe) {
		Produto produto = buscarPorCodigo(codigo);
		if (produto == null) {
			System.out.println("Erro: produto " + codigo + " não encontrado.");
			return false;
		}
		if (novoPreco != null && (!Double.isFinite(novoPreco) || novoPreco <= 0)) {
			System.out.println("Erro: o preço base deve ser um número finito maior que zero.");
			return false;
		}

		// Garantia e dias para vencer são números: converte antes de alterar qualquer coisa
		boolean temDetalhe = novoDetalhe != null && !novoDetalhe.isEmpty();
		Integer valorNumerico = null;
		if (temDetalhe && (produto instanceof Eletronico || produto instanceof Alimenticio)) {
			try {
				valorNumerico = Integer.parseInt(novoDetalhe);
			} catch (NumberFormatException e) {
				System.out.println("Erro: o detalhe deve ser um número inteiro.");
				return false;
			}
			if (valorNumerico < 0) {
				System.out.println("Erro: o valor não pode ser negativo.");
				return false;
			}
		}

		if (novoNome != null && !novoNome.isEmpty()) {
			produto.setNome(novoNome);
		}
		if (novoPreco != null) {
			produto.setPrecoBase(novoPreco);
		}
		if (temDetalhe) {
			if (produto instanceof Eletronico) {
				((Eletronico) produto).setGarantiaMeses(valorNumerico); // downcasting
			} else if (produto instanceof Alimenticio) {
				((Alimenticio) produto).setDiasParaVencer(valorNumerico); // downcasting
			} else if (produto instanceof Vestuario) {
				((Vestuario) produto).setCor(novoDetalhe); // downcasting
			}
		}
		return true;
	}

	// Remove o produto, desde que não exista pedido nem compra ligados a ele
	public boolean remover(Integer codigo) {
		Produto produto = buscarPorCodigo(codigo);
		if (produto == null) {
			System.out.println("Erro: produto " + codigo + " não encontrado.");
			return false;
		}
		if (servicoPedido.existePedidoComProduto(produto) || servicoCompra.existeCompraDoProduto(produto)) {
			System.out.println("Erro: o produto " + codigo + " possui pedidos ou compras e não pode ser excluído.");
			return false;
		}
		produtos.remove(produto);
		return true;
	}

	// Cópia da lista (quem chama não consegue alterar a lista interna)
	public List<Produto> listarTodos() {
		return new ArrayList<>(produtos);
	}

	// Ordena por nome usando o compareTo de Produto (interface Comparable)
	public List<Produto> listarOrdenadoPorNome() {
		List<Produto> copia = new ArrayList<>(produtos); // copia para não bagunçar a ordem original
		Collections.sort(copia);
		return copia;
	}

	// Imprime a lista em formato de tabela
	public void listarTabela(List<Produto> lista) {
		if (lista.isEmpty()) {
			System.out.println("Nenhum registro encontrado.");
			return;
		}
		System.out.printf("%-6s %-24s %-12s %10s %6s  %s%n", "COD", "NOME", "TIPO", "PRECO", "ESTQ", "DETALHES");
		for (Produto p : lista) {
			System.out.printf("%-6d %-24s %-12s %10.2f %6d  %s%n", p.getCodigo(), p.getNome(), tipo(p),
					p.calcularPrecoFinal(), p.getQuantidadeEstoque(), p.getDetalhes());
		}
	}

	// Descobre o tipo do produto com instanceof
	private String tipo(Produto p) {
		if (p instanceof Eletronico) {
			return "Eletrônico";
		} else if (p instanceof Alimenticio) {
			return "Alimentício";
		} else if (p instanceof Vestuario) {
			return "Vestuário";
		}
		return "Produto";
	}

	// Apaga todos os produtos (usado ao restaurar os dados de teste)
	public void limpar() {
		produtos.clear();
	}

}
