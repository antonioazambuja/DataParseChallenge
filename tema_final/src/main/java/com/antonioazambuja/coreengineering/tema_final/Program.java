package com.antonioazambuja.coreengineering.tema_final;

public class Program {

	public static void main(String[] args) {
		AnaliseDados analiseDados = new AnaliseDados();
		System.out.println("Número de clientes: " + analiseDados.analiseClientes());
		System.out.println("Número de vendedores : " + analiseDados.analiseVendedores());
		System.out.println("ID da maior venda realizada: " + analiseDados.analiseVendas());
		analiseDados.listarVendedores().forEach(System.out::println);
		System.out.println("Pior vendedor de todos os tempos: " + analiseDados.buscarPiorVendedor());
		System.out.println("Melhor vendedor de todos os tempos: " + analiseDados.buscarMelhorVendedor());
	}
}
