package entidades;

/**
 * Classe abstrata que representa um produto da loja.
 * Guarda o que é comum a todos os tipos (código, nome, preço base e estoque).
 * Não existe "produto genérico": só Eletronico, Alimenticio e Vestuario são instanciados.
 * Implementa Taxavel (contrato de imposto) e Comparable (ordenação natural por nome).
 */
public abstract class Produto implements Taxavel, Comparable<Produto> {
	private Integer codigo;
	private String nome;
	private Double precoBase;
	private Integer quantidadeEstoque;

	// Construtor: recebe os dados comuns a todos os produtos
	public Produto(Integer codigo, String nome, Double precoBase, Integer quantidadeEstoque) {
		super();
		this.codigo = codigo;
		this.nome = nome;
		this.precoBase = precoBase;
		this.quantidadeEstoque = quantidadeEstoque;
	}

	public Integer getCodigo() {
		return codigo;
	}

	public void setCodigo(Integer codigo) {
		this.codigo = codigo;
	}

	public String getNome() {
		return nome;
	}

	public void setNome(String nome) {
		this.nome = nome;
	}

	public Double getPrecoBase() {
		return precoBase;
	}

	public void setPrecoBase(Double precoBase) {
		this.precoBase = precoBase;
	}

	public Integer getQuantidadeEstoque() {
		return quantidadeEstoque;
	}

	public void setQuantidadeEstoque(Integer quantidadeEstoque) {
		this.quantidadeEstoque = quantidadeEstoque;
	}

	// Cada subtipo calcula o preço final do seu jeito (polimorfismo)
	public abstract Double calcularPrecoFinal();

	// Cada subtipo devolve o texto com o seu atributo específico (garantia, validade, tamanho)
	public abstract String getDetalhes();

	// Soma ao estoque; quantidades inválidas (zero ou negativas) são ignoradas
	public void entradaEstoque(int quantidade) {
		if (quantidade > 0) {
			quantidadeEstoque += quantidade;
		}
	}

	// Retira do estoque; devolve false se a quantidade for inválida ou maior que o saldo
	public boolean saidaEstoque(int quantidade) {
		if (quantidade > 0 && quantidade <= quantidadeEstoque) {
			quantidadeEstoque -= quantidade;
			return true;
		} else {
			return false;
		}
	}

	// Ordenação natural (usada pelo Collections.sort): por nome, ignorando maiúsculas e minúsculas.
	// Retorna negativo se este vem antes, zero se igual e positivo se vem depois.
	@Override
	public int compareTo(Produto outro) {
		return this.nome.compareToIgnoreCase(outro.nome);
	}

	@Override
	public String toString() {
		return String.format("Produto: %s %nCódigo: %d %nPreço: R$%.2f %nQuantidade em estoque: %d", nome, codigo,
				calcularPrecoFinal(), quantidadeEstoque);
	}

}
