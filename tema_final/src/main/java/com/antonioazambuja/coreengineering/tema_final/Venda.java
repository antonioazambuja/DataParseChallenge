package com.antonioazambuja.coreengineering.tema_final;

import java.util.List;

public class Venda {
	private Integer idVenda;
	private Vendedor vendedor;
	private List<Produto> listaProdutos;
	public Venda(Integer idVenda, Vendedor vendedor, List<Produto> listaProdutos) {
		this.idVenda = idVenda;
		this.vendedor = vendedor;
		this.listaProdutos = listaProdutos;
	}
	
	public Venda() {
	}

	public Integer getIdVenda() {
		return idVenda;
	}
	public void setIdVenda(Integer idVenda) {
		this.idVenda = idVenda;
	}
	public Vendedor getVendedor() {
		return vendedor;
	}
	public void setVendedor(Vendedor vendedor) {
		this.vendedor = vendedor;
	}
	public List<Produto> getListaProdutos() {
		return listaProdutos;
	}
	public void setListaProdutos(List<Produto> listaProdutos) {
		this.listaProdutos = listaProdutos;
	}
	public Double valorVenda() {
		return this.listaProdutos.stream()
				.mapToDouble(produto -> produto.getPreco() * produto.getQuant())
				.sum();
	}
}
