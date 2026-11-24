package persistencia;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import entidades.Alimenticio;
import entidades.Cliente;
import entidades.Compra;
import entidades.Eletronico;
import entidades.ItemPedido;
import entidades.Pedido;
import entidades.Produto;
import entidades.Venda;
import entidades.Vestuario;
import entidadesEnumerada.StatusPedido;
import entidadesEnumerada.Tamanho;
import servicos.Loja;

/**
 * Grava e lê os dados da loja em arquivos de texto, uma linha por registro,
 * com os campos separados por ponto e vírgula. Arquivos criados na pasta informada:
 * produtos.txt, clientes.txt, pedidos.txt, itens.txt, compras.txt e vendas.txt.
 * Os objetos se ligam por chaves: item -> código do produto e número do pedido,
 * pedido -> CPF do cliente, compra -> código do produto, venda -> número do pedido.
 * O estoque gravado em produtos.txt já inclui compras, vendas e cancelamentos,
 * por isso a leitura NÃO refaz essas operações: só recria os objetos.
 * Os métodos de gravação e leitura devolvem true se deu certo; se houver erro
 * de arquivo (IOException) ou de dado, mostram a mensagem e devolvem false.
 */
public class ArmazenamentoArquivo {

	private static final String PRODUTOS = "produtos.txt";
	private static final String CLIENTES = "clientes.txt";
	private static final String PEDIDOS = "pedidos.txt";
	private static final String ITENS = "itens.txt";
	private static final String COMPRAS = "compras.txt";
	private static final String VENDAS = "vendas.txt";

	private String pasta;
	private Loja loja;

	public ArmazenamentoArquivo(String pasta, Loja loja) {
		this.pasta = pasta;
		this.loja = loja;
	}

	// Caminho completo de um arquivo dentro da pasta de dados
	private String caminho(String nomeArquivo) {
		return pasta + "/" + nomeArquivo;
	}

	// Há dados gravados? (se não houver, é a primeira execução)
	public boolean existemDados() {
		return new File(caminho(PRODUTOS)).exists();
	}

	// O ';' é o separador de campos, então é trocado por ',' nos textos digitados
	private String campo(String texto) {
		return texto.replace(';', ',');
	}

	// ======================== GRAVAÇÃO ========================

	// Grava todos os arquivos; para no primeiro que falhar
	public boolean salvar() {
		new File(pasta).mkdir(); // cria a pasta se ainda não existir
		return salvarProdutos() && salvarClientes() && salvarPedidos() && salvarItens() && salvarCompras()
				&& salvarVendas();
	}

	// try-with-resources fecha o arquivo ao final do bloco, com ou sem erro
	private boolean salvarProdutos() {
		try (BufferedWriter bw = new BufferedWriter(new FileWriter(caminho(PRODUTOS)))) {
			for (Produto p : loja.getServicoProduto().listarTodos()) {
				String base = p.getCodigo() + ";" + campo(p.getNome()) + ";" + p.getPrecoBase() + ";"
						+ p.getQuantidadeEstoque();
				String linha;
				if (p instanceof Eletronico) {
					linha = "ELETRONICO;" + base + ";" + ((Eletronico) p).getGarantiaMeses();
				} else if (p instanceof Alimenticio) {
					linha = "ALIMENTICIO;" + base + ";" + ((Alimenticio) p).getDiasParaVencer();
				} else {
					Vestuario v = (Vestuario) p;
					linha = "VESTUARIO;" + base + ";" + v.getTamanho() + ";" + campo(v.getCor());
				}
				bw.write(linha);
				bw.newLine();
			}
			return true;
		} catch (IOException e) {
			System.out.println("Erro ao gravar " + PRODUTOS + ": " + e.getMessage());
			return false;
		}
	}

	private boolean salvarClientes() {
		try (BufferedWriter bw = new BufferedWriter(new FileWriter(caminho(CLIENTES)))) {
			for (Cliente c : loja.getServicoCliente().listarTodos()) {
				bw.write(campo(c.getNome()) + ";" + campo(c.getEmail()) + ";" + c.getCpf() + ";" + c.getTelefone());
				bw.newLine();
			}
			return true;
		} catch (IOException e) {
			System.out.println("Erro ao gravar " + CLIENTES + ": " + e.getMessage());
			return false;
		}
	}

	private boolean salvarPedidos() {
		try (BufferedWriter bw = new BufferedWriter(new FileWriter(caminho(PEDIDOS)))) {
			for (Pedido p : loja.getServicoPedido().listarTodos()) {
				bw.write(p.getNumero() + ";" + p.getCliente().getCpf() + ";" + campo(p.getFormaPagamento()) + ";"
						+ p.getStatus());
				bw.newLine();
			}
			return true;
		} catch (IOException e) {
			System.out.println("Erro ao gravar " + PEDIDOS + ": " + e.getMessage());
			return false;
		}
	}

	// Um registro por item, ligado ao pedido e ao produto pelas chaves
	private boolean salvarItens() {
		try (BufferedWriter bw = new BufferedWriter(new FileWriter(caminho(ITENS)))) {
			for (Pedido p : loja.getServicoPedido().listarTodos()) {
				for (ItemPedido item : p.getItens()) {
					bw.write(p.getNumero() + ";" + item.getProduto().getCodigo() + ";" + item.getQuantidade() + ";"
							+ item.getPrecoUnitario());
					bw.newLine();
				}
			}
			return true;
		} catch (IOException e) {
			System.out.println("Erro ao gravar " + ITENS + ": " + e.getMessage());
			return false;
		}
	}

	private boolean salvarCompras() {
		try (BufferedWriter bw = new BufferedWriter(new FileWriter(caminho(COMPRAS)))) {
			for (Compra c : loja.getServicoCompra().listarTodas()) {
				bw.write(c.getNumero() + ";" + campo(c.getFornecedor()) + ";" + c.getProduto().getCodigo() + ";"
						+ c.getQuantidade() + ";" + c.getCustoUnitario());
				bw.newLine();
			}
			return true;
		} catch (IOException e) {
			System.out.println("Erro ao gravar " + COMPRAS + ": " + e.getMessage());
			return false;
		}
	}

	private boolean salvarVendas() {
		try (BufferedWriter bw = new BufferedWriter(new FileWriter(caminho(VENDAS)))) {
			for (Venda v : loja.getServicoVenda().listarTodas()) {
				bw.write(v.getNumero() + ";" + v.getPedido().getNumero());
				bw.newLine();
			}
			return true;
		} catch (IOException e) {
			System.out.println("Erro ao gravar " + VENDAS + ": " + e.getMessage());
			return false;
		}
	}

	// ======================== LEITURA ========================

	// Lê os arquivos na ordem das dependências (produtos e clientes primeiro);
	// para no primeiro que falhar
	public boolean carregar() {
		return carregarProdutos() && carregarClientes() && carregarPedidos() && carregarItens() && carregarCompras()
				&& carregarVendas();
	}

	// Lê todas as linhas não vazias de um arquivo; devolve null se não conseguir ler
	private List<String> lerLinhas(String nomeArquivo) {
		List<String> linhas = new ArrayList<>();
		try (BufferedReader br = new BufferedReader(new FileReader(caminho(nomeArquivo)))) {
			String linha = br.readLine();
			while (linha != null) {
				if (!linha.trim().equals("")) {
					linhas.add(linha);
				}
				linha = br.readLine();
			}
			return linhas;
		} catch (IOException e) {
			System.out.println("Erro ao ler " + nomeArquivo + ": " + e.getMessage());
			return null;
		}
	}

	// Mensagem padrão para uma linha de arquivo com problema
	private boolean linhaInvalida(String nomeArquivo, int numeroLinha) {
		System.out.println("Erro: linha " + numeroLinha + " de " + nomeArquivo + " está inválida.");
		return false;
	}

	private boolean carregarProdutos() {
		List<String> linhas = lerLinhas(PRODUTOS);
		if (linhas == null) {
			return false;
		}
		for (int i = 0; i < linhas.size(); i++) {
			String[] c = linhas.get(i).split(";");
			if (c.length < 6) {
				return linhaInvalida(PRODUTOS, i + 1);
			}
			try {
				Integer codigo = Integer.parseInt(c[1]);
				Double preco = Double.parseDouble(c[3]);
				Integer estoque = Integer.parseInt(c[4]);
				Produto produto;
				if (c[0].equals("ELETRONICO")) {
					produto = new Eletronico(codigo, c[2], preco, estoque, Integer.parseInt(c[5]));
				} else if (c[0].equals("ALIMENTICIO")) {
					produto = new Alimenticio(codigo, c[2], preco, estoque, Integer.parseInt(c[5]));
				} else if (c[0].equals("VESTUARIO") && c.length >= 7) {
					produto = new Vestuario(codigo, c[2], preco, estoque, Tamanho.valueOf(c[5]), c[6]);
				} else {
					return linhaInvalida(PRODUTOS, i + 1);
				}
				if (!loja.getServicoProduto().cadastrar(produto)) {
					return linhaInvalida(PRODUTOS, i + 1);
				}
			} catch (IllegalArgumentException e) {
				// NumberFormatException (número inválido) e valueOf (tamanho inválido)
				// são IllegalArgumentException
				return linhaInvalida(PRODUTOS, i + 1);
			}
		}
		return true;
	}

	private boolean carregarClientes() {
		List<String> linhas = lerLinhas(CLIENTES);
		if (linhas == null) {
			return false;
		}
		for (int i = 0; i < linhas.size(); i++) {
			String[] c = linhas.get(i).split(";");
			if (c.length < 4 || !loja.getServicoCliente().cadastrar(new Cliente(c[0], c[1], c[2], c[3]))) {
				return linhaInvalida(CLIENTES, i + 1);
			}
		}
		return true;
	}

	private boolean carregarPedidos() {
		List<String> linhas = lerLinhas(PEDIDOS);
		if (linhas == null) {
			return false;
		}
		for (int i = 0; i < linhas.size(); i++) {
			String[] c = linhas.get(i).split(";");
			if (c.length < 4) {
				return linhaInvalida(PEDIDOS, i + 1);
			}
			Cliente cliente = loja.getServicoCliente().buscarPorCpf(c[1]);
			if (cliente == null) {
				return linhaInvalida(PEDIDOS, i + 1);
			}
			try {
				Pedido pedido = new Pedido(Integer.parseInt(c[0]), cliente, c[2]);
				pedido.restaurarStatus(StatusPedido.valueOf(c[3]));
				loja.getServicoPedido().restaurar(pedido);
			} catch (IllegalArgumentException e) {
				return linhaInvalida(PEDIDOS, i + 1);
			}
		}
		return true;
	}

	private boolean carregarItens() {
		List<String> linhas = lerLinhas(ITENS);
		if (linhas == null) {
			return false;
		}
		for (int i = 0; i < linhas.size(); i++) {
			String[] c = linhas.get(i).split(";");
			if (c.length < 4) {
				return linhaInvalida(ITENS, i + 1);
			}
			try {
				Pedido pedido = loja.getServicoPedido().buscarPorNumero(Integer.parseInt(c[0]));
				Produto produto = loja.getServicoProduto().buscarPorCodigo(Integer.parseInt(c[1]));
				if (pedido == null || produto == null) {
					return linhaInvalida(ITENS, i + 1);
				}
				// usa o preço gravado (congelado na venda), não o preço atual do produto
				pedido.restaurarItem(new ItemPedido(produto, Integer.parseInt(c[2]), Double.parseDouble(c[3])));
			} catch (IllegalArgumentException e) {
				return linhaInvalida(ITENS, i + 1);
			}
		}
		return true;
	}

	private boolean carregarCompras() {
		List<String> linhas = lerLinhas(COMPRAS);
		if (linhas == null) {
			return false;
		}
		for (int i = 0; i < linhas.size(); i++) {
			String[] c = linhas.get(i).split(";");
			if (c.length < 5) {
				return linhaInvalida(COMPRAS, i + 1);
			}
			try {
				Produto produto = loja.getServicoProduto().buscarPorCodigo(Integer.parseInt(c[2]));
				if (produto == null) {
					return linhaInvalida(COMPRAS, i + 1);
				}
				Compra compra = new Compra(Integer.parseInt(c[0]), c[1], produto, Integer.parseInt(c[3]),
						Double.parseDouble(c[4]));
				loja.getServicoCompra().restaurar(compra); // não mexe no estoque
			} catch (IllegalArgumentException e) {
				return linhaInvalida(COMPRAS, i + 1);
			}
		}
		return true;
	}

	private boolean carregarVendas() {
		List<String> linhas = lerLinhas(VENDAS);
		if (linhas == null) {
			return false;
		}
		for (int i = 0; i < linhas.size(); i++) {
			String[] c = linhas.get(i).split(";");
			if (c.length < 2) {
				return linhaInvalida(VENDAS, i + 1);
			}
			try {
				Pedido pedido = loja.getServicoPedido().buscarPorNumero(Integer.parseInt(c[1]));
				if (pedido == null) {
					return linhaInvalida(VENDAS, i + 1);
				}
				loja.getServicoVenda().restaurar(new Venda(Integer.parseInt(c[0]), pedido));
			} catch (IllegalArgumentException e) {
				return linhaInvalida(VENDAS, i + 1);
			}
		}
		return true;
	}

}
