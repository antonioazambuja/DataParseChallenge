package com.antonioazambuja.coreengineering.challenge.domain;

public class Cliente {
	
	private Long cnpj;
	private String nome;
	private String areaNegocio;
	
	public Cliente(Long cnpj, String nome, String areaNegocio) {
		this.cnpj = cnpj;
		this.nome = nome;
		this.areaNegocio = areaNegocio;
	}
	
	public Cliente() { }
	
	public Long getCnpj() { return cnpj; }
	
	public void setCnpj(Long cnpj) { this.cnpj = cnpj; }
	
	public String getNome() { return nome; }
	
	public void setNome(String nome) { this.nome = nome; }
	
	public String getAreaNegocio() { return areaNegocio; }
	
	public void setAreaNegocio(String areaNegocio) { this.areaNegocio = areaNegocio; }
}
