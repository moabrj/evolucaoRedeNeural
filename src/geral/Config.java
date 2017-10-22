package geral;

import java.util.Random;

public class Config {
	
	public static int N_ENTRADAS = 7;
	public static int N_NEURONIOS_ESCONDIDOS = 5;
	public static int N_SAIDAS = 1;
	public static int SEMENTE = 5;
	
	public static int N_IND_POP = 50;
	public static int N_MAX_GERACOES = 500;
	public static double TAXA_MUTACAO = 0.2;
	public static double LIMITE_MIN = -10;
	public static double LIMITE_MAX = 10;
	//public static String NOME_ARQUIVO = "C:\\Users\\Moab\\Documents\\Projetos\\RedeNeural\\dados\\cenario_reduzido.csv";
	public static String NOME_ARQUIVO = "C:\\Users\\Moab\\Documents\\Projetos\\RedeNeural\\dados\\todos.csv";
	public static final Random random = new Random(SEMENTE);
	
	public static boolean RECORRENCIA_ENTRADA = true;
	public static boolean RECORRENCIA_OUTROS = true;
}
