package aplicacao;

import persistencia.ArmazenamentoArquivo;
import servicos.CargaInicial;
import servicos.Loja;
import visao.Console;
import visao.MenuPrincipal;

/**
 * Classe principal do sistema de loja e estoque. Só monta as peças e inicia o menu:
 * os serviços (regras e listas), a persistência (arquivos) e a visão (menus).
 * Na primeira execução cadastra automaticamente os dados de teste (7 objetos de
 * cada classe) e os grava; nas seguintes, lê os dados gravados na pasta "dados".
 */
public class Programa {

	// Pasta (relativa ao diretório do projeto) onde os arquivos de dados ficam
	private static final String PASTA_DADOS = "dados";

	public static void main(String[] args) {
		Console console = new Console();
		try {
			Loja loja = new Loja();
			ArmazenamentoArquivo armazenamento = new ArmazenamentoArquivo(PASTA_DADOS, loja);
			if (iniciarDados(loja, armazenamento)) {
				new MenuPrincipal(console, loja, armazenamento).abrir();
				armazenamento.salvar(); // grava uma última vez ao sair
				System.out.println("Dados gravados. Até logo!");
			} else {
				System.out.println("Corrija o arquivo ou apague a pasta '" + PASTA_DADOS
						+ "' e abra o programa novamente.");
			}
		} finally {
			console.fechar(); // o finally executa com ou sem exceção
		}
	}

	// Primeira execução: cadastra os dados de teste. Demais execuções: lê os arquivos.
	// Devolve false se os arquivos não puderam ser lidos ou gravados.
	private static boolean iniciarDados(Loja loja, ArmazenamentoArquivo armazenamento) {
		if (armazenamento.existemDados()) {
			if (!armazenamento.carregar()) {
				return false;
			}
			System.out.println("Dados carregados da pasta '" + PASTA_DADOS + "'.");
			return true;
		}
		CargaInicial.carregar(loja);
		if (!armazenamento.salvar()) {
			return false;
		}
		System.out.println("Primeira execução: dados de teste cadastrados e gravados na pasta '" + PASTA_DADOS + "'.");
		return true;
	}

}
