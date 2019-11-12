package com.antonioazambuja.coreengineering.challenge;

import static org.junit.Assert.assertEquals;

import org.junit.Test;

import com.antonioazambuja.coreengineering.challenge.domain.Cliente;
import com.antonioazambuja.coreengineering.challenge.domain.Venda;
import com.antonioazambuja.coreengineering.challenge.domain.Vendedor;
import com.antonioazambuja.coreengineering.challenge.service.AnaliseDados;

public class AnaliseDadosTest {
	@Test
	public void clienteBuilderTest() {
		String dadoCliente = "002ç2345675434544345çJose da SilvaçRural";
		AnaliseDados analiseDados = new AnaliseDados();
		Cliente cliente = analiseDados.analiseClientes(dadoCliente);
		assertEquals("Jose da Silva", cliente.getNome());
	}
	
	@Test
	public void vendedorBuilderTest() {
		String dadoVendedor = "001ç3245678865434çAntonioç3999.99";
		AnaliseDados analiseDados = new AnaliseDados();
		Vendedor vendedor = analiseDados.analiseVendedores(dadoVendedor);
		assertEquals("Antonio", vendedor.getNome());
	}
	
	@Test
	public void vendaBuilderTest() {
		String dadoVendedor = "001ç1234567891234çDiegoç50000";
		String dadoVenda = "003ç10ç[1-10-10000,2-30-2.50,3-40-3.10]çDiego";
		AnaliseDados analiseDados = new AnaliseDados();
		Vendedor vendedor = analiseDados.analiseVendedores(dadoVendedor);
		Venda venda = analiseDados.analiseVendas(dadoVenda);
		assertEquals(new Integer(10), venda.getIdVenda());
		assertEquals("Diego", vendedor.getNome());
	}
	
	@Test
	public void quantidadeVendedoresTest() {
		String dadoVendedor = "001ç3245678865434çAntonioç3999.99";
		AnaliseDados analiseDados = new AnaliseDados();
		analiseDados.analiseVendedores(dadoVendedor);
		analiseDados.analiseVendedores(dadoVendedor);
		analiseDados.analiseVendedores(dadoVendedor);
		assertEquals(new Integer(3), analiseDados.quantidadeVendedores());
	}
	
	@Test
	public void quantidadeClientesTest() {
		String dadoCliente = "002ç2345675434544345çJose da SilvaçRural";
		AnaliseDados analiseDados = new AnaliseDados();
		analiseDados.analiseClientes(dadoCliente);
		analiseDados.analiseClientes(dadoCliente);
		analiseDados.analiseClientes(dadoCliente);
		analiseDados.analiseClientes(dadoCliente);
		analiseDados.analiseClientes(dadoCliente);
		analiseDados.analiseClientes(dadoCliente);
		assertEquals(new Integer(6), analiseDados.quantidadeClientes());
	}
	
	@Test
	public void melhorVendaTest() {
		AnaliseDados analiseDados = new AnaliseDados();
		String dadoVendedor1 = "001ç3245678865434çAntonioç3999.99";
		String dadoVendedor2 = "001ç3245678865434çRenatoç40000.99";
		String dadoVendedor3 = "001ç1234567891234çDiegoç50000";
		String dadoVenda1 = "003ç10ç[1-10-10000,2-30-2.50,3-40-3.10]çDiego";
		String dadoVenda2 = "003ç19ç[1-34-1000,2-33-1.50,3-40-0.10]çAntonio";
		String dadoVenda3 = "003ç29ç[1-34-1000,2-33-1.50,3-40-100.10]çRenato";
		analiseDados.analiseVendedores(dadoVendedor1);
		analiseDados.analiseVendedores(dadoVendedor2);
		analiseDados.analiseVendedores(dadoVendedor3);
		analiseDados.analiseVendas(dadoVenda1);
		analiseDados.analiseVendas(dadoVenda2);
		analiseDados.analiseVendas(dadoVenda3);
		assertEquals(new Integer(10), analiseDados.buscarMelhorVenda());
	}
	
	@Test
	public void buscarPiorVendedorTest() {
		AnaliseDados analiseDados = new AnaliseDados();
		String dadoVendedor1 = "001ç3245678865434çAntonioç3999.99";
		String dadoVendedor2 = "001ç3245678865434çRenatoç40000.99";
		String dadoVendedor3 = "001ç1234567891234çDiegoç50000";
		String dadoVenda1 = "003ç10ç[1-10-10000,2-30-2.50,3-40-3.10]çDiego";
		String dadoVenda2 = "003ç19ç[1-34-1000,2-33-1.50,3-40-0.10]çAntonio";
		String dadoVenda3 = "003ç29ç[1-34-1000,2-33-1.50,3-40-100.10]çRenato";
		analiseDados.analiseVendedores(dadoVendedor1);
		analiseDados.analiseVendedores(dadoVendedor2);
		analiseDados.analiseVendedores(dadoVendedor3);
		analiseDados.analiseVendas(dadoVenda1);
		analiseDados.analiseVendas(dadoVenda2);
		analiseDados.analiseVendas(dadoVenda3);
		assertEquals(new Long("3245678865434"), analiseDados.buscarPiorVendedor().getCpf());
	}
}