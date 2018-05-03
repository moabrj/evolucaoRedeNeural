package geral;

public class Auxiliar {

	public static boolean USAR_CAMADA_ASSOCIATIVA = false;
	public static boolean TREINO_COM_CICLOS = true;
	public static boolean CICLO_1 = false;
	public static boolean CICLO_2 = false;
	
	/**
	 * Trunca para duas casas decimais
	 */
	public static double truncate(double value) {
        return Math.round(value * 100) / 100d;
    }

	public static double truncateFour(double value) {
		return Math.round(value * 10000) / 10000d;
	}
	
}
