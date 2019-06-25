package geral;

import java.util.Random;

public class Config {
	
	public static int N_ENTRADAS = 7;
	public static int N_NEURONIOS_ASSOCIATIVA = 2;
	public static int N_NEURONIOS_ESCONDIDOS = 2;
	public static int N_SAIDAS = 2;
	//public static int N_SAIDAS = 3;
	public static int SEMENTE = 0;//(new Random()).nextInt(1000);
	public static int FUNCAO_ATIVACAO_OUTROS = -1; //1-tangente hiperbolico, 2-sigmoide, 3-degrau, -1-repassa sem modificar
	public static int FUNCAO_ATIVACAO_SAIDA = 3; //1-tangente hiperbolico, 2-sigmoide, 3-degrau
	
	public static int N_IND_POP = 50;
	public static int N_MAX_GERACOES = 500;
	public static double TAXA_MUTACAO = 0.2;
	public static double LIMITE_MIN = -2;
	public static double LIMITE_MAX = 2;
	public static int NUMERO_CASOS = 8; //4 tipos de entrada vezes (camada ass + camada inter)
	
	
	//-----------------------------------------------------------------------------------------------------------------------
	public static int TIPO_ARQUIVO = 3; //(ver referencia abaixo)
	
	//treino com objeto ruido - tipo 2 (objeto 1 como ruido e objeto 2 e 3 como alvos)
	/*
	public static String NOME_ARQUIVO = "D:\\Documentos\\Projetos\\evolucaoRedeNeural\\dados\\5\\com_ruido.csv";
	public static String TREINO_CICLO_1 = "D:\\Documentos\\Projetos\\evolucaoRedeNeural\\dados\\5\\tamanho_ruido.csv";
	public static String TREINO_CICLO_2 = "D:\\Documentos\\Projetos\\evolucaoRedeNeural\\dados\\5\\com_ruido.csv";
	*/
	//treino sem objeto ruido - tipo 1 (2 objetos sem ruido, com objetos 4 e 5)
	/*
	public static String NOME_ARQUIVO = "D:\\Documentos\\Projetos\\evolucaoRedeNeural\\dados\\5\\sem_ruido.csv";
	public static String TREINO_CICLO_1 = "D:\\Documentos\\Projetos\\evolucaoRedeNeural\\dados\\5\\tamanho_tri.csv";
	public static String TREINO_CICLO_2 = "D:\\Documentos\\Projetos\\evolucaoRedeNeural\\dados\\5\\sem_ruido.csv";
	*/
	
	//treino com 5 objetos - tipo 3 (objetos 2 e 3 como alvos e objetos 1, 4 e 5 como ruído)
	///*
	public static String NOME_ARQUIVO = "D:\\Documentos\\Projetos\\evolucaoRedeNeural\\dados\\6\\5_objetos_e_movimento.csv";
	public static String TREINO_CICLO_1 = "D:\\Documentos\\Projetos\\evolucaoRedeNeural\\dados\\6\\5_objetos.csv";
	public static String TREINO_CICLO_2 = "D:\\Documentos\\Projetos\\evolucaoRedeNeural\\dados\\6\\5_objetos_e_movimento.csv";
	//*/
	
	//treino sem objeto ruido - tipo 4 (2 objetos sem ruido, com objetos 2 e 3)
	/*
	public static String NOME_ARQUIVO = "D:\\Documentos\\Projetos\\evolucaoRedeNeural\\dados\\6\\obj2e3eMov.csv";
	public static String TREINO_CICLO_1 = "D:\\Documentos\\Projetos\\evolucaoRedeNeural\\dados\\6\\obj2e3.csv";
	public static String TREINO_CICLO_2 = "D:\\Documentos\\Projetos\\evolucaoRedeNeural\\dados\\6\\obj2e3eMov.csv";
	*/
	
	//treino com 4 objetos - tipo 5 (objetos 2 e 3 como alvos e objetos 1, 4 e 5 como ruído)
	/*
	public static String NOME_ARQUIVO = "D:\\Documentos\\Projetos\\evolucaoRedeNeural\\dados\\6\\4_objetos_e_movimento.csv";
	public static String TREINO_CICLO_1 = "D:\\Documentos\\Projetos\\evolucaoRedeNeural\\dados\\6\\4_objetos.csv";
	public static String TREINO_CICLO_2 = "D:\\Documentos\\Projetos\\evolucaoRedeNeural\\dados\\6\\4_objetos_e_movimento.csv";
	*/
	
	//treino com 3 objetos - tipo 6 (objetos 3 e 4 como alvos e objeto 2 como ruído)
	/*
	public static String NOME_ARQUIVO = "D:\\Documentos\\Projetos\\evolucaoRedeNeural\\dados\\6\\3_objetos_e_movimento.csv";
	public static String TREINO_CICLO_1 = "D:\\Documentos\\Projetos\\evolucaoRedeNeural\\dados\\6\\3_objetos.csv";
	public static String TREINO_CICLO_2 = "D:\\Documentos\\Projetos\\evolucaoRedeNeural\\dados\\6\\3_objetos_e_movimento.csv";
	*/
	//--------------------------------------------------------------------------------------------------------------------------
	
	public static final Random random = new Random(SEMENTE);
	public static final double MAX_FITNESS = 172;
	
	public static boolean RECORRENCIA_ENTRADA = true;
	public static boolean RECORRENCIA_ENTRADA_FIXA = true;
	public static boolean RECORRENCIA_OUTROS = false;
	public static boolean USAR_BIAS = false;
	public static boolean ATIVA_BIAS = true;
	public static final boolean WTA = true;
	public static boolean ATIVACAO_GERAL = false;
	public static double VALOR_TAU_FIXO = 0.2;
	public static double LIMITE_MIN_TAU = 0;
	public static double LIMITE_MAX_TAU = 1;
	public static boolean DUPLA_ENTRADA = true; //false - entrada simples true - entrada dupla
}
