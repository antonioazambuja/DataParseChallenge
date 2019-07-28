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
import java.util.stream.Stream;

import com.antonioazambuja.coreengineering.tema_final.exceptions.CriarArquivoException;
import com.antonioazambuja.coreengineering.tema_final.exceptions.GravarArquivoException;
import com.antonioazambuja.coreengineering.tema_final.exceptions.LeituraArquivoException;
import com.antonioazambuja.coreengineering.tema_final.exceptions.MonitoracaoDiretorioEntradaException;

public class AnaliseDados{
	private final Path HOMEPATH = Paths.get(System.getProperty("user.home"));
	private final Path PATH_ENTRADA = Paths.get(HOMEPATH + "/data/in");
	private final Path PATH_SAIDA = Paths.get(HOMEPATH + "/data/out");
	private final Path PATH_SAIDA_ARQUIVO = Paths.get(PATH_SAIDA + "/" + "{dados_saida}.done.dat");
	private final String SEPARADOR_DADOS = "ç";
	private List<Vendedor> listaVendedores = new ArrayList<Vendedor>();
	private List<String> listaVendedoresString = new ArrayList<String>();
	private List<Cliente> listaClientes = new ArrayList<Cliente>();
	private List<String> listaClientesString = new ArrayList<String>();
	private List<Venda> listaVendas = new ArrayList<Venda>();
	private List<String> listaVendasString = new ArrayList<String>();
	private List<String> listaPaths = new ArrayList<String>();
	
	public AnaliseDados() {
		try {
			File arquivo = new File(PATH_SAIDA_ARQUIVO.toString());
			if (!Files.exists(PATH_SAIDA_ARQUIVO)) {
				arquivo.getParentFile().mkdirs();
				arquivo.createNewFile();
			}
			arquivo = new File(PATH_ENTRADA.toString());
			if (!Files.exists(PATH_ENTRADA)) {
				arquivo.mkdirs();
			}
		} catch (IOException e) {
			throw new CriarArquivoException("Erro ao criar arquivo para executar análise de dados!" + e);
		}
	}
	
	public void executar(){
		lerPastaDados();
		verificarNovosArquivos();
	}
	
	private void lerNovoArquivo(String pathNovoArquivo) {
		this.listaClientesString.clear();
		this.listaVendedores.clear();
		try (BufferedReader dadosArquivo = new BufferedReader(new FileReader(PATH_ENTRADA + "/" + pathNovoArquivo)); 
				BufferedReader dadosArquivo2 = new BufferedReader(new FileReader(PATH_ENTRADA + "/" + pathNovoArquivo));
				BufferedReader dadosArquivo3 = new BufferedReader(new FileReader(PATH_ENTRADA + "/" + pathNovoArquivo))){
			listaVendedoresString.addAll(dadosArquivo.lines()
					.filter(linha -> linha.startsWith("001"))
					.filter(linha -> !listaVendedoresString.contains(linha))
					.collect(Collectors.toList()));
			listaClientesString.addAll(dadosArquivo2.lines()
					.filter(linha -> linha.startsWith("002"))
					.collect(Collectors.toList()));
			listaVendasString.addAll(dadosArquivo3.lines()
					.filter(linha -> linha.startsWith("003"))
					.collect(Collectors.toList()));
		} catch (IOException e) {
			throw new LeituraArquivoException("Erro ao ler novo arquivo!" + e);
		}
		gravarArquivo();
	}
	
	private void verificarNovosArquivos() {
		try(WatchService service = FileSystems.getDefault().newWatchService()){
			Map<WatchKey, Path> keyMap = new HashMap<>();
			Path path = PATH_ENTRADA.toAbsolutePath();
			keyMap.put(path.register(service,
					StandardWatchEventKinds.ENTRY_CREATE),
					path);
			WatchKey watchKey;
			
			do {
				watchKey = service.take();
				Path eventDir = keyMap.get(watchKey);
				for (WatchEvent<?> event : watchKey.pollEvents()) {
					WatchEvent.Kind<?> kind = event.kind();
					Path eventPath = (Path)event.context();
					if (eventPath.toString().endsWith(".dat")) {
						System.out.println(eventDir + ": " + kind + ": " + eventPath);
						lerNovoArquivo(eventPath.toString());
						listaPaths = Files.list(PATH_ENTRADA)
								.map(path -> path.toString())
								.filter(path -> !listaPaths.contains(path))
								.collect(Collectors.toList());
						lerPastaDados(listaPaths);
					}
				}
			}while (watchKey.reset());
		} catch (IOException | InterruptedException e) {
			throw new MonitoracaoDiretorioEntradaException("Erro ao verificar alterações no diretório de entrada!" + e);
		}
	}
	
	private void lerPastaDados(List<String> listaPaths) {
		try(){
			listaPaths = streamPaths.map(pathArquivo -> pathArquivo.toString())
					.filter(pathArquivo -> pathArquivo.endsWith(".dat"))
					.collect(Collectors.toList());
			if (!listaPaths.isEmpty()) {
				for(String path: listaPaths) {
					try (BufferedReader dadosArquivo = new BufferedReader(new FileReader(path)); 
							BufferedReader dadosArquivo2 = new BufferedReader(new FileReader(path));
							BufferedReader dadosArquivo3 = new BufferedReader(new FileReader(path))){
						listaVendedoresString.addAll(dadosArquivo.lines()
								.filter(linha -> linha.startsWith("001"))
								.filter(linha -> !listaVendedoresString.contains(linha))
								.collect(Collectors.toList()));
						listaClientesString.addAll(dadosArquivo2.lines()
								.filter(linha -> linha.startsWith("002"))
								.collect(Collectors.toList()));
						listaVendasString.addAll(dadosArquivo3.lines()
								.filter(linha -> linha.startsWith("003"))
								.collect(Collectors.toList()));
					}
				}
				gravarArquivo();
			}
		} catch (IOException e1) {
			throw new LeituraArquivoException("Erro ao ler arquivos existentes na pasta de entrada de dados!");
		}
	}
	
	private void gravarArquivo() {
		try {
			FileWriter arquivoSaida = new FileWriter(PATH_SAIDA_ARQUIVO.toString());
			arquivoSaida.write(analiseClientes()
					+ SEPARADOR_DADOS
					+ analiseVendedores()
					+ SEPARADOR_DADOS
					+ analiseVendas()
					+ SEPARADOR_DADOS
					+ buscarPiorVendedor());
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
			Vendedor vendedor = buscarVendedor(dados.split(SEPARADOR_DADOS)[3]);
			if (!vendedor.equals(null)) {
				Venda venda = VendaBuilder.builder()
						.comId(Integer.parseInt(dados.split(SEPARADOR_DADOS)[1]))
						.comListaProdutos(dados.split(SEPARADOR_DADOS)[2])
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
