package entidades;

/**
 * Representa um cliente da loja. CPF e telefone são String porque são
 * identificadores (não se faz conta com eles) e podem começar com zero.
 * Implementa Comparable para ordenar clientes por nome.
 */
public class Cliente implements Comparable<Cliente> {
	private String nome;
	private String email;
	private String cpf;
	private String telefone;

	public Cliente(String nome, String email, String cpf, String telefone) {
		super();
		this.nome = nome;
		this.email = email;
		this.cpf = cpf;
		this.telefone = telefone;
	}

	public String getNome() {
		return nome;
	}

	public void setNome(String nome) {
		this.nome = nome;
	}

	public String getEmail() {
		return email;
	}

	public void setEmail(String email) {
		this.email = email;
	}

	public String getCpf() {
		return cpf;
	}

	public void setCpf(String cpf) {
		this.cpf = cpf;
	}

	public String getTelefone() {
		return telefone;
	}

	public void setTelefone(String telefone) {
		this.telefone = telefone;
	}

	// Ordenação natural: por nome, ignorando maiúsculas e minúsculas
	@Override
	public int compareTo(Cliente outro) {
		return this.nome.compareToIgnoreCase(outro.nome);
	}

	@Override
	public String toString() {
		return "Cliente: " + nome + " - Email: " + email + " - CPF: " + cpf + " - Telefone: " + telefone;
	}

}
