package servicos;

/**
 * Reúne os cinco serviços já ligados entre si (injeção de dependência).
 * Quem precisa dos serviços recebe uma Loja em vez de cinco parâmetros.
 */
public class Loja {

	// A ordem importa: os serviços que são injetados nos outros vêm primeiro
	private ServicoPedido servicoPedido = new ServicoPedido();
	private ServicoCompra servicoCompra = new ServicoCompra();
	private ServicoVenda servicoVenda = new ServicoVenda();
	private ServicoCliente servicoCliente = new ServicoCliente(servicoPedido);
	private ServicoProduto servicoProduto = new ServicoProduto(servicoPedido, servicoCompra);

	public ServicoProduto getServicoProduto() {
		return servicoProduto;
	}

	public ServicoCliente getServicoCliente() {
		return servicoCliente;
	}

	public ServicoPedido getServicoPedido() {
		return servicoPedido;
	}

	public ServicoCompra getServicoCompra() {
		return servicoCompra;
	}

	public ServicoVenda getServicoVenda() {
		return servicoVenda;
	}

	// Apaga todos os dados em memória (usado ao restaurar os dados de teste)
	public void limparTudo() {
		servicoVenda.limpar();
		servicoPedido.limpar();
		servicoCompra.limpar();
		servicoProduto.limpar();
		servicoCliente.limpar();
	}

}
