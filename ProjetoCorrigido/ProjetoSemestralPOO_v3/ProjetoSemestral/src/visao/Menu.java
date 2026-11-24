package visao;

import persistencia.ArmazenamentoArquivo;
import servicos.Loja;

/**
 * Classe abstrata que define como todo menu funciona (método abrir): mostra o título
 * e as opções, lê a escolha e repete até o usuário escolher 0. Cada menu concreto
 * só informa o título, os textos das opções e o que fazer em cada uma.
 * Depois de cada ação que alterou dados com sucesso, os dados são gravados em arquivo.
 */
public abstract class Menu {

	protected Console console;
	protected Loja loja;
	private ArmazenamentoArquivo armazenamento;

	public Menu(Console console, Loja loja, ArmazenamentoArquivo armazenamento) {
		this.console = console;
		this.loja = loja;
		this.armazenamento = armazenamento;
	}

	// Título mostrado no topo do menu
	protected abstract String getTitulo();

	// Textos das opções; a numeração (1, 2, 3...) é automática
	protected abstract String[] getOpcoes();

	// O que fazer em cada opção (de 1 até o número de opções).
	// Devolve true se a ação alterou os dados com sucesso (para gravar em arquivo).
	protected abstract boolean tratarOpcao(int opcao);

	// Texto da opção 0 (o menu principal usa "Sair")
	protected String getTextoSaida() {
		return "Voltar";
	}

	// Mostra o menu e repete até o usuário escolher 0
	public void abrir() {
		int opcao;
		do {
			exibir();
			opcao = console.lerInt("Opção: ");
			if (opcao >= 1 && opcao <= getOpcoes().length) {
				if (tratarOpcao(opcao)) {
					armazenamento.salvar(); // grava depois de uma alteração bem-sucedida
				}
			} else if (opcao != 0) {
				System.out.println("Opção inválida.");
			}
		} while (opcao != 0);
	}

	private void exibir() {
		System.out.println("\n--- " + getTitulo() + " ---");
		String[] opcoes = getOpcoes();
		for (int i = 0; i < opcoes.length; i++) {
			System.out.println((i + 1) + " - " + opcoes[i]);
		}
		System.out.println("0 - " + getTextoSaida());
	}

}
