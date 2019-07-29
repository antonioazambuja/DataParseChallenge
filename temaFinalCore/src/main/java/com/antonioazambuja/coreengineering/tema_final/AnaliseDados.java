package com.antonioazambuja.coreengineering.tema_final;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.nio.file.FileSystems;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardWatchEventKinds;
import java.nio.file.WatchEvent;
import java.nio.file.WatchKey;
import java.nio.file.WatchService;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import com.antonioazambuja.coreengineering.tema_final.exceptions.CriarArquivoException;
import com.antonioazambuja.coreengineering.tema_final.exceptions.GravarArquivoException;
import com.antonioazambuja.coreengineering.tema_final.exceptions.LeituraDadosException;

public class AnaliseDados{
	private final Path HOMEPATH = Paths.get(System.getProperty("user.home"));
	private final Path PATH_PASTA_ENTRADA = Paths.get(HOMEPATH + "/data/in");
	private final Path PATH_ARQUIVO_SAIDA = Paths.get(HOMEPATH + "/data/out" + "/" + "{dados_saida}.done.dat");
	private final String SEPARADOR_DADOS = "ç";
	private	Map<WatchKey, Path> keyMap = new HashMap<>();
	private List<Vendedor> listaVendedores = new ArrayList<>();
	private List<String> listaVendedoresString = new ArrayList<>();
	private List<Cliente> listaClientes = new ArrayList<>();
	private List<String> listaClientesString = new ArrayList<>();
	private List<Venda> listaVendas = new ArrayList<>();
	private List<String> listaVendasString = new ArrayList<>();
	private List<String> listaPaths = new ArrayList<>();
	
	public AnaliseDados() {
		try {
			File arquivo = new File(PATH_ARQUIVO_SAIDA.toString());
			if (!Files.exists(PATH_ARQUIVO_SAIDA)) {
				arquivo.getParentFile().mkdirs();
				arquivo.createNewFile();
			}
			arquivo = new File(PATH_PASTA_ENTRADA.toString());
			if (!Files.exists(PATH_PASTA_ENTRADA))
				arquivo.mkdirs();
		} catch (IOException e) {
			throw new CriarArquivoException("Erro ao criar diretórios e arquivos para executar análise de dados!" + e);
		}
		System.out.println("Aguardando novos arquivos em " + PATH_PASTA_ENTRADA + " com extensão '.dat'...");
	}
	
	public void executar(){
		lerPastaDados();
		monitorarDiretoriosDados();
	}
	
	private void monitorarDiretoriosDados() {
		try(WatchService watchService = FileSystems.getDefault().newWatchService()){
			this.keyMap.put(PATH_PASTA_ENTRADA.toAbsolutePath().register(watchService,
					StandardWatchEventKinds.ENTRY_CREATE,
					StandardWatchEventKinds.ENTRY_DELETE,
					StandardWatchEventKinds.ENTRY_MODIFY),
					PATH_PASTA_ENTRADA.toAbsolutePath());
			WatchKey watchKey;
			while(true) {
				watchKey = watchService.take();
				Path eventDir = keyMap.get(watchKey);
				for (WatchEvent<?> evento : watchKey.pollEvents()) {
					WatchEvent.Kind<?> kind = evento.kind();
					Path pathEvento = (Path)evento.context();
					if (pathEvento.toString().endsWith(".dat")) {
						System.out.println(eventDir + ": " + kind + ": " + pathEvento);
						lerPastaDados();
					}
				}
				watchKey.reset();
			}
		} catch (IOException | InterruptedException e) {
			throw new LeituraDadosException("Erro ao verificar alterações no diretório de entrada!" + e);
			
		}
	}
	
	private void lerPastaDados() {
		limparDadosAntigos();
		try {
			this.listaPaths.addAll(Files.list(PATH_PASTA_ENTRADA)
					.map(pathArquivo -> pathArquivo.toString())
					.filter(pathArquivo -> pathArquivo.endsWith(".dat"))
					.collect(Collectors.toList()));
			if(!listaPaths.isEmpty()) {
				for (String pathArquivo : this.listaPaths) {
					try(BufferedReader arquivo = new BufferedReader(new FileReader(pathArquivo))){
						List<String> conteudoArquivo = arquivo.lines()
								.collect(Collectors.toList());
						listaVendedoresString.addAll(conteudoArquivo.stream()
								.filter(linha -> linha.startsWith("001") && !listaVendedoresString.contains(linha))
								.collect(Collectors.toList()));
						listaClientesString.addAll(conteudoArquivo.stream()
								.filter(linha -> linha.startsWith("002"))
								.collect(Collectors.toList()));
						listaVendasString.addAll(conteudoArquivo.stream()
								.filter(linha -> linha.startsWith("003"))
								.collect(Collectors.toList()));
					}
				}
				gravarArquivo(analiseClientes()
					+ SEPARADOR_DADOS
					+ analiseVendedores()
					+ SEPARADOR_DADOS
					+ analiseVendas()
					+ SEPARADOR_DADOS
					+ buscarPiorVendedor());
			}else
				gravarArquivo("");
		} catch (IOException e) {
			throw new LeituraDadosException("Erro ao ler arquivos existentes na pasta de entrada de dados!" + e);
		}
	}
	
	private void limparDadosAntigos() {
		this.listaClientesString.clear();
		this.listaClientes.clear();
		this.listaVendedoresString.clear();
		this.listaVendedores.clear();
		this.listaVendasString.clear();
		this.listaVendas.clear();
		this.listaPaths.clear();
	}
	
	private void gravarArquivo(String relatorioDados) {
		try {
			FileWriter arquivoSaida = new FileWriter(PATH_ARQUIVO_SAIDA.toString());
			arquivoSaida.write(relatorioDados);
			arquivoSaida.flush();
			arquivoSaida.close();
		} catch (IOException e) {
			throw new GravarArquivoException("Erro ao gravar arquivo de saída!" + e);
		}
	}
	
	private Integer analiseClientes() {
		for (String dados: this.listaClientesString) {
			String[] dadosCliente = dados.split(SEPARADOR_DADOS);
			Cliente cliente = ClienteBuilder.builder()
					.comCnpj(Long.parseLong(dadosCliente[1]))
					.comNome(dadosCliente[2])
					.comAreaNegocio(dadosCliente[3])
					.build();
			listaClientes.add(cliente);
		}
		return listaClientes.size();
	}
	
	private Integer analiseVendedores() {
		for (String dados: this.listaVendedoresString) {
			String[] dadosVendedor = dados.split(SEPARADOR_DADOS);
			Vendedor vendedor = VendedorBuilder.builder()
					.comCpf(Long.parseLong(dadosVendedor[1]))
					.comNome(dadosVendedor[2])
					.comSalario(Double.parseDouble(dadosVendedor[3]))
					.build();
			listaVendedores.add(vendedor);
		}
		return this.listaVendedores.size();
	}
	
	private Integer analiseVendas(){
		for (String dados: this.listaVendasString) {
			String[] dadosVenda = dados.split(SEPARADOR_DADOS);
			Vendedor vendedor = buscarVendedor(dadosVenda[3]);
			if (!vendedor.equals(null)) {
				Venda venda = VendaBuilder.builder()
						.comId(Integer.parseInt(dadosVenda[1]))
						.comListaProdutos(dadosVenda[2])
						.comVendedor(vendedor.getNome())
						.build();
				vendedor.adicionarVendaRealizada(venda.valorVenda());
				listaVendas.add(venda);
			}
		}
		return this.listaVendas.stream()
				.max(Comparator.comparing(Venda::valorVenda))
				.get()
				.getIdVenda();
	}
	
	private Vendedor buscarPiorVendedor() {
		return listaVendedores.stream()
				.min(Comparator.comparing(Vendedor::getTotalVendasRealizadas))
				.get();
	}
	
	private Vendedor buscarVendedor(String nomeVendedor) {
		return listaVendedores.stream()
				.filter(vendedor -> vendedor.getNome().equals(nomeVendedor))
				.findAny()
				.get();
	}
}
