package ag;

import java.util.HashMap;

public class Geracao {
	
	private HashMap<Integer,Integer> registroAtivacaoNeuronios;
	private double melhorFitness = 0;
	private double fitnessMedio = 0;
	
	public Geracao() {
		registroAtivacaoNeuronios = new HashMap<Integer, Integer>();
	}
	
	public void addRegistroAtivacao(double n1, double n2, double n3, double n4, double n5, int geracao, HistoricoEvolutivo h) {
		h.inserirDadoGrafico(geracao, (int) n1, "Neurônio 1");
		h.inserirDadoGrafico(geracao, (int) n2, "Neurônio 2");
		h.inserirDadoGrafico(geracao, (int) n3, "Neurônio 3");
		h.inserirDadoGrafico(geracao, (int) n4, "Neurônio 4");
		h.inserirDadoGrafico(geracao, (int) n5, "Neurônio 5");
	}
	
	public void addRegistroAtivacaoGrafico(int geracao, double n1, double n2, double n3, double n4, double n5, HistoricoEvolutivo h) {
		h.inserirDadoGrafico(geracao, (int) n1, "Neurônio 1");
		h.inserirDadoGrafico(geracao, (int) n2, "Neurônio 2");
		h.inserirDadoGrafico(geracao, (int) n3, "Neurônio 3");
		h.inserirDadoGrafico(geracao, (int) n4, "Neurônio 4");
		h.inserirDadoGrafico(geracao, (int) n5, "Neurônio 5");
	}
	
	public HashMap<Integer, Integer> resultadosAtivacao(){
		return this.registroAtivacaoNeuronios;
	}
}