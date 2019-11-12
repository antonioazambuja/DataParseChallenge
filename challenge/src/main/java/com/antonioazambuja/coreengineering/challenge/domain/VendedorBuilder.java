package com.antonioazambuja.coreengineering.challenge.domain;

public class VendedorBuilder {
	private Vendedor vendedor;
	
	public VendedorBuilder() {
		this.vendedor = new Vendedor();
	}
	
	public static VendedorBuilder builder() {
		return new VendedorBuilder();
	}
	
	public VendedorBuilder comCpf(Long cpf) {
		this.vendedor.setCpf(cpf);
		return this;
	}
	
	public VendedorBuilder comNome(String nome) {
		 this.vendedor.setNome(nome);
		 return this;
	}
	
	public VendedorBuilder comSalario(Double salario) {
		 this.vendedor.setSalario(salario);
		 return this;
	}
	
	public Vendedor build() {
		return this.vendedor;
	}
}
