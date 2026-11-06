package entidades;

public interface Taxavel {
	public Double getAliquotaImposto();
	
	public Double calcularImposto(Double precoBase);
}
