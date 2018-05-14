package geral;

import java.util.Random;

public class Config {
	
	public static int N_ENTRADAS = 7;
	public static int N_NEURONIOS_ASSOCIATIVA = 2;
	public static int N_NEURONIOS_ESCONDIDOS = 2;
	public static int N_SAIDAS = 2;
	//public static int N_SAIDAS = 3;
	public static int SEMENTE = 10;
	public static int FUNCAO_ATIVACAO_OUTROS = -1; //1-tangente hiperbolico, 2-sigmoide, 3-degrau, -1-repassa sem modificar
	public static int FUNCAO_ATIVACAO_SAIDA = 3; //1-tangente hiperbolico, 2-sigmoide, 3-degrau
	
	public static int N_IND_POP = 50;
	public static int N_MAX_GERACOES = 1000;
	public static double TAXA_MUTACAO = 0.3;
	public static double LIMITE_MIN = -2;
	public static double LIMITE_MAX = 2;
	public static int NUMERO_CASOS = 8; //4 tipos de entrada vezes (camada ass + camada inter)
	
	//objeto pequeno centralizado
	public static String NOME_ARQUIVO = "C:\\Users\\Moab\\Documents\\Projetos\\RedeNeural\\dados\\4\\treino.csv";
	public static String TREINO_CICLO_1 = "C:\\Users\\Moab\\Documents\\Projetos\\RedeNeural\\dados\\4\\tamanho_vazado.csv";
	public static String TREINO_CICLO_2 = "C:\\Users\\Moab\\Documents\\Projetos\\RedeNeural\\dados\\4\\treino.csv";
	//objeto pequeno vazado com ruido
	//public static String NOME_ARQUIVO = "C:\\Users\\Moab\\Documents\\Projetos\\RedeNeural\\dados\\3\\treino_ciclo_2.csv";
	//public static String TREINO_CICLO_1 = "C:\\Users\\Moab\\Documents\\Projetos\\RedeNeural\\dados\\3\\tamanho_2.csv";
	//public static String TREINO_CICLO_2 = "C:\\Users\\Moab\\Documents\\Projetos\\RedeNeural\\dados\\3\\treino_ciclo_2.csv";
	//objeto pequeno e grande vazado sem ruido
	//public static String NOME_ARQUIVO = "C:\\Users\\Moab\\Documents\\Projetos\\RedeNeural\\dados\\2\\treino.csv";
	//public static String TREINO_CICLO_1 = "C:\\Users\\Moab\\Documents\\Projetos\\RedeNeural\\dados\\2\\tamanho_vazado.csv";
	//public static String TREINO_CICLO_2 = "C:\\Users\\Moab\\Documents\\Projetos\\RedeNeural\\dados\\2\\treino.csv";
	
	public static final Random random = new Random(SEMENTE);
	public static final double MAX_FITNESS = 516;
	
	public static boolean RECORRENCIA_ENTRADA = true;
	public static boolean RECORRENCIA_ENTRADA_FIXA = false;
	public static boolean RECORRENCIA_OUTROS = false;
	public static boolean USAR_BIAS = false;
	public static final boolean WTA = true;
	public static boolean ATIVACAO_GERAL = false;
	public static double VALOR_TAU_FIXO = 0.2;
	public static double LIMITE_MIN_TAU = 0;
	public static double LIMITE_MAX_TAU = 1;
	public static boolean USAR_DUPLA_ENTRADA = true;
}
