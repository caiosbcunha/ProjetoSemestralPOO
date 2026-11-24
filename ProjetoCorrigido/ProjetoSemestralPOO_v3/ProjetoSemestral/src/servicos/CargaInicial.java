package servicos;

import java.util.List;

import entidades.Alimenticio;
import entidades.Cliente;
import entidades.Eletronico;
import entidades.Pedido;
import entidades.Venda;
import entidades.Vestuario;
import entidadesEnumerada.StatusPedido;
import entidadesEnumerada.Tamanho;

/**
 * Dados de teste: 7 eletrônicos, 7 alimentícios, 7 vestuários, 7 clientes,
 * 7 compras, 8 pedidos (com 15 itens) e 7 vendas. Os pedidos ficam em status
 * variados (PENDENTE, PAGO, ENVIADO, ENTREGUE e CANCELADO).
 * É executada na primeira execução (quando ainda não existem arquivos de dados)
 * ou quando o usuário pede para restaurar os dados de teste.
 */
public class CargaInicial {

	// Adiciona um item a um pedido a partir do código do produto (atalho da carga)
	private static void item(Loja loja, Pedido pedido, int codigoProduto, int quantidade) {
		loja.getServicoPedido().adicionarItem(pedido.getNumero(), loja.getServicoProduto().buscarPorCodigo(codigoProduto),
				quantidade);
	}

	public static void carregar(Loja loja) {
		ServicoProduto produtos = loja.getServicoProduto();
		ServicoCliente clientes = loja.getServicoCliente();
		ServicoPedido pedidos = loja.getServicoPedido();
		ServicoCompra compras = loja.getServicoCompra();
		ServicoVenda vendas = loja.getServicoVenda();

		// 7 eletrônicos (código, nome, preço base, estoque, garantia em meses)
		produtos.cadastrar(new Eletronico(101, "Notebook", 3000.00, 10, 12));
		produtos.cadastrar(new Eletronico(102, "Smartphone", 1800.00, 15, 12));
		produtos.cadastrar(new Eletronico(103, "Fone Bluetooth", 250.00, 30, 6));
		produtos.cadastrar(new Eletronico(104, "Smart TV", 2500.00, 8, 24));
		produtos.cadastrar(new Eletronico(105, "Mouse Gamer", 120.00, 40, 6));
		produtos.cadastrar(new Eletronico(106, "Teclado Mecânico", 300.00, 25, 12));
		produtos.cadastrar(new Eletronico(107, "Carregador USB-C", 80.00, 50, 3));

		// 7 alimentícios (código, nome, preço base, estoque, dias para vencer)
		produtos.cadastrar(new Alimenticio(201, "Arroz 5kg", 28.00, 40, 180));
		produtos.cadastrar(new Alimenticio(202, "Feijão 1kg", 9.50, 60, 150));
		produtos.cadastrar(new Alimenticio(203, "Café 500g", 18.00, 35, 90));
		produtos.cadastrar(new Alimenticio(204, "Leite Integral 1L", 6.00, 80, 5)); // perto do vencimento: 30% off
		produtos.cadastrar(new Alimenticio(205, "Biscoito Recheado", 4.50, 100, 60));
		produtos.cadastrar(new Alimenticio(206, "Chocolate 90g", 8.00, 70, 3)); // perto do vencimento: 30% off
		produtos.cadastrar(new Alimenticio(207, "Macarrão 500g", 5.50, 90, 200));

		// 7 vestuários (código, nome, preço base, estoque, tamanho, cor)
		produtos.cadastrar(new Vestuario(301, "Camiseta Básica", 50.00, 30, Tamanho.M, "Branca"));
		produtos.cadastrar(new Vestuario(302, "Calça Jeans", 150.00, 20, Tamanho.G, "Azul"));
		produtos.cadastrar(new Vestuario(303, "Jaqueta", 280.00, 12, Tamanho.GG, "Preta"));
		produtos.cadastrar(new Vestuario(304, "Vestido", 180.00, 15, Tamanho.P, "Vermelho"));
		produtos.cadastrar(new Vestuario(305, "Bermuda", 90.00, 25, Tamanho.M, "Bege"));
		produtos.cadastrar(new Vestuario(306, "Moletom", 160.00, 18, Tamanho.G, "Cinza"));
		produtos.cadastrar(new Vestuario(307, "Boné", 60.00, 40, Tamanho.M, "Preto"));

		// 7 clientes (nome, e-mail, CPF, telefone) - dados fictícios
		clientes.cadastrar(new Cliente("Ana Souza", "ana.souza@email.com", "12345678901", "71999990001"));
		clientes.cadastrar(new Cliente("Bruno Lima", "bruno.lima@email.com", "23456789012", "71999990002"));
		clientes.cadastrar(new Cliente("Carla Mendes", "carla.mendes@email.com", "34567890123", "71999990003"));
		clientes.cadastrar(new Cliente("Diego Rocha", "diego.rocha@email.com", "45678901234", "71999990004"));
		clientes.cadastrar(new Cliente("Eduarda Alves", "eduarda.alves@email.com", "56789012345", "71999990005"));
		clientes.cadastrar(new Cliente("Felipe Costa", "felipe.costa@email.com", "67890123456", "71999990006"));
		clientes.cadastrar(new Cliente("Gabriela Nunes", "gabriela.nunes@email.com", "78901234567", "71999990007"));

		// 7 compras de reposição (fornecedor, produto, quantidade, custo unitário)
		compras.registrar("TechSupply Ltda", produtos.buscarPorCodigo(101), 5, 2400.00);
		compras.registrar("TechSupply Ltda", produtos.buscarPorCodigo(102), 10, 1400.00);
		compras.registrar("Distribuidora Som", produtos.buscarPorCodigo(103), 20, 150.00);
		compras.registrar("Atacado Bom Preço", produtos.buscarPorCodigo(201), 50, 20.00);
		compras.registrar("Atacado Bom Preço", produtos.buscarPorCodigo(202), 80, 6.50);
		compras.registrar("Moda Fácil", produtos.buscarPorCodigo(301), 30, 30.00);
		compras.registrar("Moda Fácil", produtos.buscarPorCodigo(302), 15, 95.00);

		// 8 pedidos: os 7 primeiros viram vendas; o 8º fica PENDENTE para testar a venda
		List<Cliente> c = clientes.listarTodos();

		Pedido p1 = pedidos.criar(c.get(0), "PIX");
		item(loja, p1, 101, 1);
		item(loja, p1, 301, 2);
		vendas.registrar(p1); // PAGO

		Pedido p2 = pedidos.criar(c.get(1), "Cartão");
		item(loja, p2, 102, 1);
		item(loja, p2, 103, 2);
		vendas.registrar(p2);
		pedidos.alterarStatus(p2.getNumero(), StatusPedido.ENVIADO);

		Pedido p3 = pedidos.criar(c.get(2), "Dinheiro");
		item(loja, p3, 201, 3);
		item(loja, p3, 202, 5);
		vendas.registrar(p3);
		pedidos.alterarStatus(p3.getNumero(), StatusPedido.ENVIADO);
		pedidos.alterarStatus(p3.getNumero(), StatusPedido.ENTREGUE);

		Pedido p4 = pedidos.criar(c.get(3), "PIX");
		item(loja, p4, 104, 1);
		vendas.registrar(p4); // PAGO

		Pedido p5 = pedidos.criar(c.get(4), "Cartão");
		item(loja, p5, 302, 1);
		item(loja, p5, 303, 1);
		item(loja, p5, 304, 2);
		vendas.registrar(p5);
		pedidos.alterarStatus(p5.getNumero(), StatusPedido.ENVIADO);

		Pedido p6 = pedidos.criar(c.get(5), "Boleto");
		item(loja, p6, 203, 2);
		item(loja, p6, 204, 4);
		Venda v6 = vendas.registrar(p6);
		vendas.cancelar(v6.getNumero()); // CANCELADO: o estoque volta

		Pedido p7 = pedidos.criar(c.get(6), "PIX");
		item(loja, p7, 105, 2);
		item(loja, p7, 301, 1);
		vendas.registrar(p7);
		pedidos.alterarStatus(p7.getNumero(), StatusPedido.ENVIADO);
		pedidos.alterarStatus(p7.getNumero(), StatusPedido.ENTREGUE);

		Pedido p8 = pedidos.criar(c.get(0), "Cartão");
		item(loja, p8, 103, 1); // fica PENDENTE
	}

}
