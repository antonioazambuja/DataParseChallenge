package com.antonioazambuja.coreengineering.tema_final;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import com.antonioazambuja.coreengineering.tema_final.exceptions.LeituraArquivoException;

public class AnaliseDados {
	private final String PATH = "data/in/";
	private List<Vendedor> listaVendedores = new ArrayList<Vendedor>();
	private List<String> listaVendedoresString = new ArrayList<String>();
	private List<Cliente> listaClientes = new ArrayList<Cliente>();
	private List<String> listaClientesString = new ArrayList<String>();
	private List<Venda> listaVendas = new ArrayList<Venda>();
	private List<String> listaVendasString = new ArrayList<String>();
	
	public AnaliseDados() {
		lerArquivo();
	}
	
	private void lerArquivo() {
		try(Stream<Path> listaPaths = Files.walk(Paths.get(PATH))){
			for (String path: listaPaths.map(pathArquivo -> pathArquivo.toString())
					.filter(pathArquivo -> pathArquivo.endsWith(".dat"))
					.collect(Collectors.toList())) {
				try (BufferedReader dadosArquivo = new BufferedReader(new FileReader(path)); 
						BufferedReader dadosArquivo2 = new BufferedReader(new FileReader(path));
						BufferedReader dadosArquivo3 = new BufferedReader(new FileReader(path))){
					listaVendedoresString.addAll(dadosArquivo.lines()
							.filter(linha -> linha.startsWith("001") && !listaVendedoresString.contains(linha))
							.collect(Collectors.toList()));
//					analiseVendedores();
					listaClientesString.addAll(dadosArquivo2.lines()
							.filter(linha -> linha.startsWith("002"))
							.collect(Collectors.toList()));
//					analiseClientes();
					listaVendasString.addAll(dadosArquivo3.lines()
							.filter(linha -> linha.startsWith("003"))
							.collect(Collectors.toList()));
//					analiseVendas();
				} catch (IOException e) {
					throw new LeituraArquivoException("Erro ao ler o arquivo de dados!"); 
				}
			}
		} catch (IOException e) {
			throw new LeituraArquivoException("Erro ao ler o arquivo de entrada!" + e);
		}
	}
	
	public List<Vendedor> listarVendedores(){
		return listaVendedores;
	}
	
	public int analiseClientes() {
		for (String dados: this.listaClientesString) {
			String[] dadosCliente = dados.split("ç");
			Cliente cliente = ClienteBuilder.builder()
					.comCnpj(Long.parseLong(dadosCliente[1]))
					.comNome(dadosCliente[2])
					.comAreaNegocio(dadosCliente[3])
					.build();
			listaClientes.add(cliente);
		}
		return this.listaClientes.size();
	}
	
	public int analiseVendedores() {
		for (String dados: this.listaVendedoresString) {
			String[] dadosVendedor = dados.split("ç");
			Vendedor vendedor = VendedorBuilder.builder()
					.comCpf(Long.parseLong(dadosVendedor[1]))
					.comNome(dadosVendedor[2])
					.comSalario(Double.valueOf(dadosVendedor[3]))
					.build();
			listaVendedores.add(vendedor);
		}
		return listaVendedores.size();
	}
	
	public int analiseVendas(){
		for (String dados: this.listaVendasString) {
			Vendedor vendedor = buscarVendedor(dados.split("ç")[3]);
			Venda venda = VendaBuilder.builder()
					.comId(Integer.parseInt(dados.split("ç")[1]))
					.comListaProdutos(dados.split("ç")[2])
					.comVendedor(vendedor)
					.build();
			vendedor.adicionarVendaRealizada(venda.valorVenda());
			listaVendas.add(venda);
		}
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
	
	public Vendedor buscarMelhorVendedor() {
		return listaVendedores.stream()
				.max(Comparator.comparing(Vendedor::getTotalVendasRealizadas))
				.get();
	}
	
	private Vendedor buscarVendedor(String nomeVendedor) {
		return listaVendedores.stream()
				.filter(vendedor -> vendedor.getNome().equals(nomeVendedor))
				.findAny()
				.get();
	}
}
