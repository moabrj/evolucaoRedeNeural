package ag;

import java.util.ArrayList;
import java.util.HashMap;

import org.jfree.data.xy.CategoryTableXYDataset;

public class HistoricoEvolutivo {
	
	private ArrayList<Geracao> geracoes;
	public StringBuilder neuroniosAtivos;
	private CategoryTableXYDataset data;
	
	public HistoricoEvolutivo() {
		this.geracoes = new ArrayList<Geracao>();
		neuroniosAtivos = new StringBuilder();
		data = new CategoryTableXYDataset();
	}
	
	public void addGeracao(Geracao g) {
		geracoes.add(g);
	}
	
	public CategoryTableXYDataset gerarDataset() {
		CategoryTableXYDataset data = new CategoryTableXYDataset();
		int cont = 0;
		for(Geracao g : geracoes) {
			HashMap<Integer, Integer> map = g.resultadosAtivacao();
			data.add(cont, map.get(1), "Neurônio 1");
			data.add(cont, map.get(2), "Neurônio 2");
			//data.add(cont, map.get(3), "Neurônio 3");
			//data.add(cont, map.get(4), "Neurônio 4");
			//data.add(cont, map.get(5), "Neurônio 5");
			cont++;
		}
		
		return data;
	}

	public void inserirDadoGrafico(int x, int y, String label) {
		data.add(x, y, label);
	}
	
	public StringBuilder getListagemAtivacaoNeural() {
		// TODO Auto-generated method stub
		return neuroniosAtivos;
	}

	public void limparHistoricoAtivacaoNeural() {
		this.neuroniosAtivos = new StringBuilder();
	}

	public CategoryTableXYDataset getGraficoDataset() {
		// TODO Auto-generated method stub
		return this.data;
	}
}


