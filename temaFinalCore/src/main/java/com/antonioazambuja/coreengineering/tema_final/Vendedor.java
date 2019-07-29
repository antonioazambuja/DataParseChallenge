package com.antonioazambuja.coreengineering.tema_final;

public class Vendedor {
	private Long cpf;
	private String nome;
	private Double salario;
	private Double totalVendas = 0D;
	
	public Vendedor(Long cpf, String nome, Double salario) {
		this.cpf = cpf;
		this.nome = nome;
		this.salario = salario;
	}
	
	public Vendedor() { }

	public Long getCpf() { return cpf; }
	
	public void setCpf(Long cpf) { this.cpf = cpf; }
	
	public String getNome() { return nome; }
	
	public void setNome(String nome) { this.nome = nome; }
	
	public Double getSalario() { return salario; }
	
	public void setSalario(Double salario) { this.salario = salario; }

	public Double getTotalVendasRealizadas() { return totalVendas; }

	public void adicionarVendaRealizada(Double vendaRealizada) { this.totalVendas += vendaRealizada; }
	
	@Override
	public String toString() {
		return "[" + getCpf() + ","
				+ getNome() + ","
				+ getSalario() + ","
				+ getTotalVendasRealizadas() + "]";
	}
}
