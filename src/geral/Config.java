package geral;

import java.util.Random;

public class Config {
	
	public static int N_ENTRADAS = 7;
	public static int N_NEURONIOS_ASSOCIATIVA = 2;
	public static int N_NEURONIOS_ESCONDIDOS = 2;
	public static int N_SAIDAS = 2;
	//public static int N_SAIDAS = 3;
	public static int SEMENTE = 10;
	public static int FUNCAO_ATIVACAO_SAIDA = 2; //1-tangente hiperbolico, 2-sigmoide
	
	public static int N_IND_POP = 50;
	public static int N_MAX_GERACOES = 500;
	public static double TAXA_MUTACAO = 0.3;
	public static double LIMITE_MIN = -10;
	public static double LIMITE_MAX = 10;
	//public static String NOME_ARQUIVO = "C:\\Users\\Moab\\Documents\\Projetos\\RedeNeural\\dados\\2\\movimento.csv";
	//public static String NOME_ARQUIVO = "C:\\Users\\Moab\\Documents\\Projetos\\RedeNeural\\dados\\2\\tamanho.csv";
	//public static String NOME_ARQUIVO = "C:\\Users\\Moab\\Documents\\Projetos\\RedeNeural\\dados\\2\\movimento_tamanho.csv";
	public static String NOME_ARQUIVO = "C:\\Users\\Moab\\Documents\\Projetos\\RedeNeural\\dados\\2\\aproximar_afastar.csv";
	
	public static String TREINO_CICLO_1 = "C:\\Users\\Moab\\Documents\\Projetos\\RedeNeural\\dados\\2\\tamanho.csv";
	public static String TREINO_CICLO_2 = "C:\\Users\\Moab\\Documents\\Projetos\\RedeNeural\\dados\\2\\aproximar_afastar.csv";
	
	public static final Random random = new Random(SEMENTE);
	public static final double MAX_FITNESS = 172;
	
	public static boolean RECORRENCIA_ENTRADA = true;
	public static boolean RECORRENCIA_OUTROS = false;
	public static boolean ATIVA_BIAS = true;
	public static final boolean WTA = true;
	public static boolean ATIVACAO_GERAL = false;
}
