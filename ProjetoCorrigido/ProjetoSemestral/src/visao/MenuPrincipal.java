package visao;

import persistencia.ArmazenamentoArquivo;
import servicos.CargaInicial;
import servicos.Loja;

/**
 * Menu principal: abre os submenus e permite restaurar os dados de teste.
 */
public class MenuPrincipal extends Menu {

	private MenuProdutos menuProdutos;
	private MenuClientes menuClientes;
	private MenuPedidos menuPedidos;
	private MenuCompras menuCompras;
	private MenuVendas menuVendas;

	public MenuPrincipal(Console console, Loja loja, ArmazenamentoArquivo armazenamento) {
		super(console, loja, armazenamento);
		this.menuProdutos = new MenuProdutos(console, loja, armazenamento);
		this.menuClientes = new MenuClientes(console, loja, armazenamento);
		this.menuPedidos = new MenuPedidos(console, loja, armazenamento);
		this.menuCompras = new MenuCompras(console, loja, armazenamento);
		this.menuVendas = new MenuVendas(console, loja, armazenamento);
	}

	@Override
	protected String getTitulo() {
		return "LOJA E ESTOQUE";
	}

	@Override
	protected String[] getOpcoes() {
		return new String[] { "Produtos", "Clientes", "Pedidos", "Compras (reposição de estoque)", "Vendas",
				"Restaurar dados de teste (apaga tudo)" };
	}

	@Override
	protected String getTextoSaida() {
		return "Sair";
	}

	// Os submenus gravam os próprios dados, então abri-los devolve false
	@Override
	protected boolean tratarOpcao(int opcao) {
		switch (opcao) {
		case 1:
			menuProdutos.abrir();
			return false;
		case 2:
			menuClientes.abrir();
			return false;
		case 3:
			menuPedidos.abrir();
			return false;
		case 4:
			menuCompras.abrir();
			return false;
		case 5:
			menuVendas.abrir();
			return false;
		case 6:
			return restaurarDadosDeTeste();
		default:
			return false;
		}
	}

	// Apaga tudo e recria os dados de teste, depois de uma confirmação
	private boolean restaurarDadosDeTeste() {
		String resposta = console
				.lerTexto("Isso apaga todos os dados atuais e recria os dados de teste. Digite SIM para confirmar: ");
		if (!resposta.equalsIgnoreCase("SIM")) {
			System.out.println("Operação cancelada.");
			return false;
		}
		loja.limparTudo();
		CargaInicial.carregar(loja);
		System.out.println("Dados de teste restaurados.");
		return true;
	}

}
