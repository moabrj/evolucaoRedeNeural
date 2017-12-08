package geral;

import java.util.Random;

public class Config {
	
	public static int N_ENTRADAS = 7;
	public static int N_NEURONIOS_ESCONDIDOS = 3;
	public static int N_SAIDAS = 1;
	//public static int N_SAIDAS = 3;
	public static int SEMENTE = 5;
	public static int FUNCAO_ATIVACAO_SAIDA = 1; //1-tangente hiperbolico, 2-sigmoide
	
	public static int N_IND_POP = 50;
	public static int N_MAX_GERACOES = 500;
	public static double TAXA_MUTACAO = 0.2;
	public static double LIMITE_MIN = -10;
	public static double LIMITE_MAX = 10;
	public static String NOME_ARQUIVO = "C:\\Users\\Moab\\Documents\\Projetos\\RedeNeural\\dados\\movimento.csv";
	//public static String NOME_ARQUIVO = "C:\\Users\\Moab\\Documents\\Projetos\\RedeNeural\\dados\\tamanho.csv";
	//public static String NOME_ARQUIVO = "C:\\Users\\Moab\\Documents\\Projetos\\RedeNeural\\dados\\tamanho_movimento.csv";
	public static final Random random = new Random(SEMENTE);
	public static final boolean WTA = true;
	
	public static boolean RECORRENCIA_ENTRADA = true;
	public static boolean RECORRENCIA_OUTROS = false;
}
