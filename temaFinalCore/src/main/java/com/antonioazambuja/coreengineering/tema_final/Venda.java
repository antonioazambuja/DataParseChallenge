package com.antonioazambuja.coreengineering.tema_final;

import java.util.List;

public class Venda {
	private Integer idVenda;
	private String nomeVendedor;
	private List<Produto> listaProdutos;
	
	public Venda(Integer idVenda, String nomeVendedor, List<Produto> listaProdutos) {
		this.idVenda = idVenda;
		this.nomeVendedor = nomeVendedor;
		this.listaProdutos = listaProdutos;
	}
	
	public Venda() { }

	public Integer getIdVenda() { return idVenda; }
	
	public void setIdVenda(Integer idVenda) { this.idVenda = idVenda; }
	
	public String getNomeVendedor() { return nomeVendedor; }
	
	public void setVendedor(String nomeVendedor) { this.nomeVendedor = nomeVendedor; }
	
	public List<Produto> getListaProdutos() { return listaProdutos; }
	
	public void setListaProdutos(List<Produto> listaProdutos) { this.listaProdutos = listaProdutos; }
	
	public Double valorVenda() {
		return this.listaProdutos.stream()
				.mapToDouble(produto -> produto.getPreco() * produto.getQuant())
				.sum();
	}
}
