package com.antonioazambuja.coreengineering.challenge.domain;

public class Produto {
	private Integer id;
	private Integer quant;
	private Double preco;
	
	public Produto(Integer id, Integer quant, Double preco) {
		this.id = id;
		this.quant = quant;
		this.preco = preco;
	}
	
	public Produto() { }
	
	public Integer getId() { return id; }
	
	public void setId(Integer id) { this.id = id; }
	
	public Integer getQuant() { return quant; }
	
	public void setQuant(Integer quant) { this.quant = quant; }
	
	public Double getPreco() { return preco; }
	
	public void setPreco(Double preco) { this.preco = preco; }
}
