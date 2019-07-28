package com.antonioazambuja.coreengineering.tema_final;

public class Cliente {
	@Override
	public String toString() {
		return "Cliente [cnpj=" + cnpj + ", nome=" + nome + ", areaNegocio=" + areaNegocio + "]";
	}
	private Long cnpj;
	private String nome;
	private String areaNegocio;
	
	public Cliente() {
		
	}
	
	public Cliente(Long cnpj, String nome, String areaNegocio) {
		this.cnpj = cnpj;
		this.nome = nome;
		this.areaNegocio = areaNegocio;
	}
	public Long getCnpj() {
		return cnpj;
	}
	public void setCnpj(Long cnpj) {
		this.cnpj = cnpj;
	}
	public String getNome() {
		return nome;
	}
	public void setNome(String nome) {
		this.nome = nome;
	}
	public String getAreaNegocio() {
		return areaNegocio;
	}
	public void setAreaNegocio(String areaNegocio) {
		this.areaNegocio = areaNegocio;
	}
}
