Projeto Final CoreEngineering

------------------------------

Projeto com finalidade de testar conhecimentos obtidos na trilha Core Engineering ilegra!

Como executar:
- Realize download do repositório;
- Abra a pasta temaFinalCore;
- Inicie o terminal do seu sistema operacional dentro da pasta do projeto;
- Execute o comando "sudo apt-get install gradle", caso não tenha o gradle instalado em sua máquina;
- Execute o comando "gradle build";
- Execute o comando "gradle run";

Funcionamento da aplicação:
- Ao iniciar a aplicação irá criar os diretórios de entrada em "HOMEPATH/data/in/" e saída de dados em "HOMEPATH/data/out/", caso não exista;
- A aplicação irá ler apenas arquivo com extensão ".dat";
- Ao detectar mudanção no diretório de entrada o mesmo irá gerar um novo relatório no diretório de saída em um arquivo com nome
	"{dados_saida}.done.dat", lendo todos os arquivo existentes no diretório de entrada;
- É possível verificar quando um nova alteração de entrada, deleção ou modificação no arquivo é realizada apartir do terminal;
- Dentro da pasta arquivos de testes, encontram-se arquivos utilizados para teste da aplicação;
- Veja que o arquivo segue um padrão para leitura dos dados e elaboração do relatório;
- A aplicação detecta o delimitador de dados o utiliza para realizar decodificação do arquivo;

- O relatório criado consiste em (Separados pelo delimitador "/"):
* Quantidades de clientes
* Quantidades de vendedores
* ID da venda mais cara
* Dados do pior vendedor

- Para parar a execução utilize "CTRL+C" e após execute "gradle --stop".

------------------------------

Por: Antonio Azambuja
