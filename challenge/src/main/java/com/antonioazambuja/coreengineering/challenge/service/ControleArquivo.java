package com.antonioazambuja.coreengineering.challenge.service;

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
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import com.antonioazambuja.coreengineering.challenge.exceptions.CriarArquivoException;
import com.antonioazambuja.coreengineering.challenge.exceptions.GravarArquivoException;
import com.antonioazambuja.coreengineering.challenge.exceptions.LeituraDadosException;

public class ControleArquivo{
	private AnaliseDados analiseDados = new AnaliseDados();
	private final Path HOMEPATH = Paths.get(System.getProperty("user.home"));
	private final Path PATH_PASTA_ENTRADA = Paths.get(HOMEPATH + "/data/in");
	private final Path PATH_ARQUIVO_SAIDA = Paths.get(HOMEPATH + "/data/out" + "/" + "{dados_saida}.done.dat");
	private	Map<WatchKey, Path> keyMap = new HashMap<>();
	private List<String> listaPaths = new ArrayList<>();
	
	public ControleArquivo() {
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
	
	private void limparDados() {
		analiseDados.listaClientes.clear();
		analiseDados.listaVendedores.clear();
		analiseDados.listaVendas.clear();
		this.listaPaths.clear();
	}
	
	private void lerPastaDados() {
		limparDados();
		try {
			this.listaPaths.addAll(Files.list(PATH_PASTA_ENTRADA)
					.map(pathArquivo -> pathArquivo.toString())
					.filter(pathArquivo -> pathArquivo.endsWith(".dat"))
					.collect(Collectors.toList()));
			Collections.sort(listaPaths);
			if(!listaPaths.isEmpty()) {
				for (String pathArquivo: this.listaPaths) {
					System.out.println(pathArquivo);
					try(BufferedReader arquivo = new BufferedReader(new FileReader(pathArquivo))){
						List<String> conteudoArquivo = arquivo.lines()
								.collect(Collectors.toList());
						for(String linha: conteudoArquivo) {
							if(linha.startsWith("001")) {
								analiseDados.analiseVendedores(linha);
							} else if(linha.startsWith("002")) {
								analiseDados.analiseClientes(linha);
							} else if(linha.startsWith("003")) {
								analiseDados.analiseVendas(linha);
							} else {
								System.out.println("Erro ao ler linha do arquivo!");
							}
						}
					}
				}
				gravarArquivo(analiseDados.quantidadeClientes()
					+ "/"
					+ analiseDados.quantidadeVendedores()
					+ "/"
					+ analiseDados.buscarMelhorVenda()
					+ "/"
					+ analiseDados.buscarPiorVendedor());
			}else
				gravarArquivo("");
		} catch (IOException e) {
			throw new LeituraDadosException("Erro ao ler arquivos existentes na pasta de entrada de dados!" + e);
		}
		System.out.println("Aguardando novos arquivos em " + PATH_PASTA_ENTRADA + " com extensão '.dat'...");
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
}
