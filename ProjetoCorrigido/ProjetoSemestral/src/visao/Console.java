package visao;

import java.util.Scanner;

import entidadesEnumerada.Tamanho;

/**
 * Entrada de dados pelo teclado. Todos os menus compartilham a mesma instância
 * (um único Scanner). Os dados são lidos como texto e convertidos para número;
 * se o usuário digitar algo que não é número, a exceção NumberFormatException
 * é tratada com try/catch e a pergunta é repetida.
 */
public class Console {

	private Scanner sc = new Scanner(System.in);

	// Lê uma linha de texto (sem espaços nas pontas).
	// Assim que o usuário aperta Enter, a tela do console é limpa.
	public String lerTexto(String mensagem) {
		System.out.print(mensagem);
		String texto = sc.nextLine().trim();
		limparTela();
		return texto;
	}

	// Limpa o console. Em um terminal real (cmd, PowerShell, bash...) apaga a tela;
	// em consoles de IDE (ex.: Eclipse), que não aceitam comandos de limpeza,
	// empurra o conteúdo antigo para fora da vista com linhas em branco.
	public void limparTela() {
		try {
			if (executandoEmTerminal()) {
				if (System.getProperty("os.name").toLowerCase().contains("win")) {
					new ProcessBuilder("cmd", "/c", "cls").inheritIO().start().waitFor();
				} else {
					System.out.print("\033[H\033[2J\033[3J"); // sequência ANSI: limpa a tela e o histórico
					System.out.flush();
				}
				return;
			}
		} catch (Exception e) {
			// se não foi possível limpar o terminal, usa o plano B abaixo
			if (e instanceof InterruptedException) {
				Thread.currentThread().interrupt();
			}
		}
		for (int i = 0; i < 50; i++) {
			System.out.println();
		}
	}

	// Verifica se o programa está rodando em um terminal de verdade (e não no console de uma IDE)
	private boolean executandoEmTerminal() {
		java.io.Console terminal = System.console();
		if (terminal == null) {
			return false;
		}
		try {
			// Java 22+: System.console() nunca é nulo, então é preciso perguntar se é um terminal
			return (boolean) java.io.Console.class.getMethod("isTerminal").invoke(terminal);
		} catch (ReflectiveOperationException e) {
			return true; // Java mais antigo: console não nulo já significa terminal
		}
	}

	// Lê um inteiro; repete a pergunta enquanto o usuário digitar algo inválido
	public int lerInt(String mensagem) {
		while (true) {
			try {
				return Integer.parseInt(lerTexto(mensagem));
			} catch (NumberFormatException e) {
				System.out.println("Valor inválido. Digite um número inteiro.");
			}
		}
	}

	// Lê um decimal (aceita vírgula ou ponto); repete enquanto for inválido
	public double lerDouble(String mensagem) {
		while (true) {
			try {
				double valor = Double.parseDouble(lerTexto(mensagem).replace(',', '.'));
				if (Double.isFinite(valor)) { // rejeita NaN e Infinity, que o parseDouble aceita
					return valor;
				}
			} catch (NumberFormatException e) {
				// cai na mensagem abaixo
			}
			System.out.println("Valor inválido. Digite um número (ex.: 25,90).");
		}
	}

	// Lê um decimal opcional: Enter devolve null (usado nas alterações)
	public Double lerDoubleOuNulo(String mensagem) {
		while (true) {
			String texto = lerTexto(mensagem);
			if (texto.isEmpty()) {
				return null;
			}
			try {
				double valor = Double.parseDouble(texto.replace(',', '.'));
				if (Double.isFinite(valor)) { // rejeita NaN e Infinity, que o parseDouble aceita
					return valor;
				}
			} catch (NumberFormatException e) {
				// cai na mensagem abaixo
			}
			System.out.println("Valor inválido. Digite um número ou Enter para manter.");
		}
	}

	// Lê o tamanho de uma peça; converte o texto para o enum Tamanho com valueOf
	public Tamanho lerTamanho() {
		while (true) {
			try {
				return Tamanho.valueOf(lerTexto("Tamanho (PP, P, M, G, GG): ").toUpperCase());
			} catch (IllegalArgumentException e) { // valueOf lança se o texto não for um valor do enum
				System.out.println("Tamanho inválido. Use PP, P, M, G ou GG.");
			}
		}
	}

	// Fecha o Scanner
	public void fechar() {
		sc.close();
	}

}
