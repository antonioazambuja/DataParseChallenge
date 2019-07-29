package com.antonioazambuja.coreengineering.tema_final;

public class ClienteBuilder {
	private Cliente cliente;
	
	public ClienteBuilder() {
		this.cliente = new Cliente();
	}
	
	public static ClienteBuilder builder() {
		return new ClienteBuilder();
	}
	
	public ClienteBuilder comCnpj(long cnpj) {
		this.cliente.setCnpj(cnpj);
		return this;
	}
	
	public ClienteBuilder comNome(String nome) {
		 this.cliente.setNome(nome);
		 return this;
	}
	
	public ClienteBuilder comAreaNegocio(String areaNegocio) {
		 this.cliente.setAreaNegocio(areaNegocio);
		 return this;
	}
	
	public Cliente build() {
		return this.cliente;
	}
}
