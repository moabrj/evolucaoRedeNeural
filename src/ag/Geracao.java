package ag;

import java.util.HashMap;

import geral.Config;

public class Geracao {
	
	private HashMap<Integer,Integer> registroAtivacaoNeuronios;
	private double melhorFitness = 0;
	private double fitnessMedio = 0;
	private String nome = null;
	
	public Geracao(String nome) {
		registroAtivacaoNeuronios = new HashMap<Integer, Integer>();
		this.nome = nome;
	}
	
	public Geracao() {
		registroAtivacaoNeuronios = new HashMap<Integer, Integer>();
		this.nome = "Não informado";
	}
	
	public void addRegistroAtivacao(double n1, double n2, double n3, double n4, double n5, int geracao, HistoricoEvolutivo h) {
		double[] n = {n1, n2, n3, n4, n5};
		for(int i = 1; i <= Config.N_NEURONIOS_ESCONDIDOS; i++) {
			h.inserirDadoGrafico(geracao, (int) n[i-1], "Neurônio "+i);
		}
	}
	
	public void addRegistroAtivacao(int n[], int maxFitness, int geracao, HistoricoEvolutivo h, int tipoCamada) {
		for(int k=0; k<n.length;k++) {
			double rr = (double) n[k];
			double r = (rr/maxFitness)*100.0;
			n[k] = (int) r;
		}
		if(tipoCamada == 0) //camada escondida
			for(int i = 1; i <= Config.N_NEURONIOS_ESCONDIDOS; i++) {
				h.inserirDadoGrafico(geracao, (int) n[i-1], "Neurônio "+i);
			}
		else if (tipoCamada == 1) //camada associativa
			for(int i = 1; i <= Config.N_NEURONIOS_ASSOCIATIVA; i++) {
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