package ag;

import java.util.HashMap;

import geral.Config;

public class Geracao {
	
	private HashMap<Integer,Integer> registroAtivacaoNeuronios;
	private double melhorFitness = 0;
	private double fitnessMedio = 0;
	
	public Geracao() {
		registroAtivacaoNeuronios = new HashMap<Integer, Integer>();
	}
	
	public void addRegistroAtivacao(double n1, double n2, double n3, double n4, double n5, int geracao, HistoricoEvolutivo h) {
		double[] n = {n1, n2, n3, n4, n5};
		for(int i = 1; i <= Config.N_NEURONIOS_ESCONDIDOS; i++) {
			h.inserirDadoGrafico(geracao, (int) n[i-1], "Neurônio "+i);
		}
	}
	
	public void addRegistroAtivacaoGrafico(int geracao, double n1, double n2, double n3, double n4, double n5, HistoricoEvolutivo h) {
		double[] n = {n1, n2, n3, n4, n5};
		for(int i = 1; i <= Config.N_NEURONIOS_ESCONDIDOS; i++) {
			h.inserirDadoGrafico(geracao, (int) n[i-1], "Neurônio "+i);
		}
	}
	
	public HashMap<Integer, Integer> resultadosAtivacao(){
		return this.registroAtivacaoNeuronios;
	}
}