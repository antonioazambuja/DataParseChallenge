package com.antonioazambuja.coreengineering.tema_final;

import java.util.ArrayList;
import java.util.List;

public class VendaBuilder {
	private Venda venda;
	private List<Produto> listaProdutos = new ArrayList<Produto>();

	public VendaBuilder() {
		this.venda = new Venda();
	}

	public static VendaBuilder builder() {
		return new VendaBuilder();
	}

	public VendaBuilder comId(Integer id) {
		this.venda.setIdVenda(id);
		return this;
	}

	public VendaBuilder comListaProdutos(String listaProduto) {
		for (String produto : listaProduto.substring(listaProduto.indexOf("[") + 1, listaProduto.indexOf("]") - 1)
				.split(",")) {
			Produto produtoCompleto = new Produto();
			produtoCompleto.setId(Integer.parseInt(produto.split("-")[0]));
			produtoCompleto.setQuant(Integer.parseInt(produto.split("-")[1]));
			produtoCompleto.setPreco(Double.parseDouble(produto.split("-")[2]));
			this.listaProdutos.add(produtoCompleto);
		}
		return this;
	}

	public VendaBuilder comVendedor(String nomeVendedor) {
		this.venda.setVendedor(nomeVendedor);
		return this;
	}

	public Venda build() {
		this.venda.setListaProdutos(listaProdutos);
		return this.venda;
	}
}
