package com.antonioazambuja.coreengineering.challenge.service;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;

import com.antonioazambuja.coreengineering.challenge.domain.Cliente;
import com.antonioazambuja.coreengineering.challenge.domain.ClienteBuilder;
import com.antonioazambuja.coreengineering.challenge.domain.Venda;
import com.antonioazambuja.coreengineering.challenge.domain.VendaBuilder;
import com.antonioazambuja.coreengineering.challenge.domain.Vendedor;
import com.antonioazambuja.coreengineering.challenge.domain.VendedorBuilder;

public class AnaliseDados {
	public List<Vendedor> listaVendedores = new ArrayList<>();
	public List<Cliente> listaClientes = new ArrayList<>();
	public List<Venda> listaVendas = new ArrayList<>();
	
	public Cliente analiseClientes(String dados) {
		String[] dadosCliente = dados.split(String.valueOf(dados.charAt(3)));
		Cliente cliente = ClienteBuilder.builder()
				.comCnpj(Long.parseLong(dadosCliente[1]))
				.comNome(dadosCliente[2])
				.comAreaNegocio(dadosCliente[3])
				.build();
		listaClientes.add(cliente);
		return cliente;
	}
	
	public Vendedor analiseVendedores(String dados) {
		String[] dadosVendedor = dados.split(String.valueOf(dados.charAt(3)));
		Vendedor vendedor = VendedorBuilder.builder()
				.comCpf(Long.parseLong(dadosVendedor[1]))
				.comNome(dadosVendedor[2])
				.comSalario(Double.parseDouble(dadosVendedor[3]))
				.build();
		listaVendedores.add(vendedor);
		return vendedor;
	}
	
	public Venda analiseVendas(String dados){
		String[] dadosVenda = dados.split(String.valueOf(dados.charAt(3)));
		Vendedor vendedor = buscarVendedor(dadosVenda[3]);
		Venda venda = new Venda();
		if (!vendedor.equals(null)) {
			venda = VendaBuilder.builder()
					.comId(Integer.parseInt(dadosVenda[1]))
					.comListaProdutos(dadosVenda[2])
					.comVendedor(vendedor.getNome())
					.build();
			vendedor.adicionarVendaRealizada(venda.valorVenda());
			listaVendas.add(venda);
		}
		return venda;
	}
	
	public Integer quantidadeClientes() {
		return this.listaClientes.size();
	}
	
	public Integer quantidadeVendedores() {
		return this.listaVendedores.size();
	}
	
	public Integer buscarMelhorVenda() {
		return this.listaVendas.stream()
				.max(Comparator.comparing(Venda::valorVenda))
				.get()
				.getIdVenda();
	}
	
	public Vendedor buscarPiorVendedor() {
		return listaVendedores.stream()
				.min(Comparator.comparing(Vendedor::getTotalVendasRealizadas))
				.get();
	}
	
	public Vendedor buscarVendedor(String nomeVendedor) {
		return listaVendedores.stream()
				.filter(vendedor -> vendedor.getNome().equals(nomeVendedor))
				.findAny()
				.get();
	}
}
