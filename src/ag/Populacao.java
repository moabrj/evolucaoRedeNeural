package ag;

import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Collections;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.LinkedList;

import geral.Config;

public class Populacao {
	
	private LinkedList<Individuo> individuos;
	private HistoricoEvolutivo historico;
	
	private int n1 = 0;
	private int n2 = 0;
	private int n3 = 0;
	private int n4 = 0;
	private int n5 = 0;
	
	public Populacao(HistoricoEvolutivo historico) {
		this.historico = historico;
		this.individuos = new LinkedList<>();
		for(int i=0; i<Config.N_IND_POP; i++)
			individuos.add(new Individuo());
	}
	
	public void calcularFitness1Saida(double[][] entradas, int q_linhas, int epoca) throws Exception {
		double entradaAnt = entradas[0][7];
		int qntInd = 0;
		Geracao g = new Geracao();
		n1=0;
		n2=0;
		n3=0;
		n4=0;
		n5=0;
		for(Individuo ind : individuos) {
			//para documentacao
			this.historico.neuroniosAtivos.append("\nIndividuo "+qntInd+"\n");
			ind.reiniciaRecorrencia();
			int fitness = 0;
			for(int i=0; i<q_linhas; i++) {
				if(entradaAnt != entradas[i][7]) {
					ind.reiniciaRecorrencia();
					entradaAnt = entradas[i][7];
				}
				
				double[] saida = ind.atualizaRede(entradas[i]);
				@SuppressWarnings("unused")
				double[] saidaEscondida = ind.ativacaoCamadaInter();
				
				//centro
				if (entradas[i][7] == 2 && saida[0] == 0)
                    fitness += 1;
				else if (entradas[i][7] == 2 && saida[0] != 0)
                    fitness -= 1;
				else if (entradas[i][7] == 3 && saida[0] == 1)
                    fitness += 1;
				else if (entradas[i][7] == 3 && saida[0] != 1)
                    fitness -= 1;
				
				//para registro
				if(Config.ATIVACAO_GERAL)
					this.gerarAtivacaoPopulacao(saida, saidaEscondida, i, qntInd, epoca, g);
				
			}
			ind.setFitness(fitness);
			qntInd++;
		}
	}
	
	/**
	 * Este método é usado para o cálculo de fitness
	 * quando usado tamanho e movimento ao mesmo tempo
	 * @param entradas
	 * @param q_linhas
	 * @throws Exception 
	 */
	public void calcularFitness3Saida(double[][] entradas, int q_linhas, int epoca) throws Exception {
		double entradaAnt = entradas[0][7];
		int qntInd = 0;
		Geracao g = new Geracao();
		n1=0;
		n2=0;
		n3=0;
		n4=0;
		n5=0;
		for(Individuo ind : individuos) {
			ind.reiniciaRecorrencia();
			this.historico.neuroniosAtivos.append("\nIndividuo "+qntInd+"\n");
			int fitness = 0;
			double[] saida;
			for(int i=0; i<q_linhas; i++) {
				if(entradaAnt != entradas[i][7]) {
					ind.reiniciaRecorrencia();
					entradaAnt = entradas[i][7];
				}
				
				saida = ind.atualizaRede(entradas[i]);
				@SuppressWarnings("unused")
				double[] saidaEscondida = ind.ativacaoCamadaInter();
				
				//centro
				if (entradas[i][7] == 0 && saida[0] == 0 && saida[1] == 0 && saida[2] == 0)
                    fitness += 1;
				else if (entradas[i][7] == 0 && saida[0] != 0 && saida[1] != 0 && saida[2] != 0)
                    fitness -= 1;
				//movimento
				else if (entradas[i][7] == 1 && saida[0] == 0 && saida[1] == 0 && saida[2] == 1)
                    fitness += 1;
				else if (entradas[i][7] == 1 && saida[0] != 0 && saida[1] != 0 && saida[2] != 1)
                    fitness -= 1;
				//movimento
				else if (entradas[i][7] == 2 && saida[0] == 0 && saida[1] == 1 && saida[2] == 0)
                    fitness += 1;
				else if (entradas[i][7] == 2 && saida[0] != 0 && saida[1] != 1 && saida[2] != 0)
                    fitness -= 1;
				//grande
				else if (entradas[i][7] == 3 && saida[0] == 0 && saida[1] == 1 && saida[2] == 1)
                    fitness += 1;
				else if (entradas[i][7] == 3 && saida[0] != 0 && saida[1] != 1 && saida[2] != 1)
                    fitness -= 1;
				//pequeno
				else if (entradas[i][7] == 4 && saida[0] == 1 && saida[1] == 0 && saida[2] == 0)
                    fitness += 1;
				else if (entradas[i][7] == 4 && saida[0] != 1 && saida[1] != 0 && saida[2] != 0)
                    fitness -= 1;
				
				//para registro
				if(Config.ATIVACAO_GERAL)
					this.gerarAtivacaoPopulacao(saida, saidaEscondida, i, qntInd, epoca, g);
			}
			ind.setFitness(fitness);
			qntInd++;
		}
	}
	
	public void ordenar() {
		Collections.sort(individuos);
	}
	
	public void selecionaMelhores() {
		this.ordenar();
		LinkedList<Individuo> aux = new LinkedList<>(this.individuos.subList(0, 10));
		this.individuos = aux;
	}
	
	public int getTamanhoPopulacao()
	{
		return this.individuos.size();
	}
	
	public LinkedList<Individuo> getIndividuos() {
		return individuos;
	}
	
	public void registrarLogs(PrintWriter gravarArq) throws IOException {
		this.ordenar();
		for(Individuo i : individuos) {
			gravarArq.println(i.toString());
		}
	}
	
	public double getMelhorFitness()
	{
		this.ordenar();
		return this.individuos.getFirst().getFitness();
	}
	
	public double getMediaFitnessPop()
	{
		double media = 0;
		for(Individuo i : individuos) {
			media += i.getFitness();
		}
		return media/this.individuos.size();
	}


	public StringBuilder getAtivacaoMelhor(double[][] ds, int q_linhas, int epoca) throws Exception {
		StringBuilder neuronioAtivo = new StringBuilder();
		Individuo ind = this.individuos.getFirst();
		int qntInd = 0;
		double n1 = 0;
		double n2 = 0;
		double n3 = 0;
		double n4 = 0;
		double n5 = 0;
		//for(Individuo ind : individuos) {
			ind.reiniciaRecorrencia();
			neuronioAtivo.append("\nIndividuo "+qntInd+"\n");
			double entradaAnt = ds[0][7];
			for(int i=0; i<q_linhas; i++) {
				if(entradaAnt != ds[i][7]) {
					ind.reiniciaRecorrencia();
					entradaAnt = ds[i][7];
				}
				double[] saida = ind.atualizaRede(ds[i]);
				double[] saidaEscondida = ind.ativacaoCamadaInter();
				for(int j=0;j<saidaEscondida.length;j++)
					neuronioAtivo.append(String.valueOf(saidaEscondida[j])+" ");
				for(int j=0;j<saida.length;j++)
					neuronioAtivo.append(String.valueOf(saida[j])+" ");
				neuronioAtivo.append("\n");
				int j = posMaximo(saidaEscondida);
				//montagem grafico
				if(i < 43) {// && i < 129) {
					if(j == 0)
						n1++;
					else if(j==1)
						n2++;
					else if(j==2)
						n3++;
					else if(j==3)
						n4++;
					else if(j==4)
						n5++;
				}
			}
			qntInd++;
		//}
		double fitnessMax = 43;
		Geracao g = new Geracao();
		g.addRegistroAtivacao((n1/fitnessMax)*100, (n2/fitnessMax)*100, (n3/fitnessMax)*100, (n4/fitnessMax)*100, (n5/fitnessMax)*100, epoca, this.historico);
		return neuronioAtivo;
	}

	public void gerarAtivacaoPopulacao(double[] saidaFinal, double[] saidaEscondida, int linha, int qntInd, int epoca, Geracao g) throws Exception {
		StringBuilder neuronioAtivo = this.historico.getListagemAtivacaoNeural();
		//Individuo ind = this.individuos.getFirst();
		
		for(int j=0;j<saidaEscondida.length;j++)
			neuronioAtivo.append(String.valueOf(saidaEscondida[j])+" ");
		for(int j=0;j<saidaFinal.length;j++)
			neuronioAtivo.append(String.valueOf(saidaFinal[j])+" ");
		neuronioAtivo.append("\n");
		int j = posMaximo(saidaEscondida);
		//montagem grafico
		if(linha>91) {
			if(j == 0)
				n1++;
			else if(j==1)
				n2++;
			else if(j==2)
				n3++;
			else if(j==3)
				n4++;
			else if(j==4)
				n5++;
		}
		
		double fitnessMax = 43*50;
		g.addRegistroAtivacaoGrafico(epoca, (n1/fitnessMax)*100, (n2/fitnessMax)*100, (n3/fitnessMax)*100, (n4/fitnessMax)*100, (n5/fitnessMax)*100, this.historico);
		//this.historico.addGeracao(g);
	}

	
	private int posMaximo(double[] v) throws Exception {
		for(int i=0;i<v.length;i++)
		{
			if(v[i] > 0) {
				return i;
			}
		}
		throw new Exception("WTA não funcionou, verifique o método posMaximo em População!!");
	}
	
	/**
	 * Este método é usado para o cálculo de fitness
	 * quando usado tamanho e movimento ao mesmo tempo
	 * @param entradas
	 * @param q_linhas
	 * @throws Exception 
	 */
	public void calcularFitness4Saida(double[][] entradas, int q_linhas, int epoca) throws Exception {
		double entradaAnt = entradas[0][7];
		int qntInd = 0;
		Geracao g = new Geracao();
		n1=0;
		n2=0;
		n3=0;
		n4=0;
		n5=0;
		for(Individuo ind : individuos) {
			ind.reiniciaRecorrencia();
			this.historico.neuroniosAtivos.append("\nIndividuo "+qntInd+"\n");
			int fitness = 0;
			double[] saida;
			for(int i=0; i<q_linhas; i++) {
				if(entradaAnt != entradas[i][7]) {
					ind.reiniciaRecorrencia();
					entradaAnt = entradas[i][7];
				}
				
				saida = ind.atualizaRede(entradas[i]);
				@SuppressWarnings("unused")
				double[] saidaEscondida = ind.ativacaoCamadaInter();
				
				//direita
				if (entradas[i][7] == 0 && saida[0] == 1 && saida[1] == 0 && saida[2] == 0 && saida[3] == 0)
                    fitness += 1;
				else if (entradas[i][7] == 0 && saida[0] != 1 && saida[1] != 0 && saida[2] != 0 && saida[3] != 0)
                    fitness -= 1;
				//esquerda
				else if (entradas[i][7] == 1 && saida[0] == 0 && saida[1] == 1 && saida[2] == 0 && saida[3] == 0)
                    fitness += 1;
				else if (entradas[i][7] == 1 && saida[0] != 0 && saida[1] != 1 && saida[2] != 0 && saida[3] != 0)
                    fitness -= 1;
				//pequeno
				else if (entradas[i][7] == 2 && saida[0] == 0 && saida[1] == 0 && saida[2] == 1 && saida[3] == 0)
                    fitness += 1;
				else if (entradas[i][7] == 2 && saida[0] != 0 && saida[1] != 0 && saida[2] != 1 && saida[3] != 0)
                    fitness -= 1;
				//grande
				else if (entradas[i][7] == 3 && saida[0] == 0 && saida[1] == 0 && saida[2] == 0 && saida[3] == 1)
                    fitness += 1;
				else if (entradas[i][7] == 3 && saida[0] != 0 && saida[1] != 0 && saida[2] != 0 && saida[3] != 1)
                    fitness -= 1;
				
				//para registro
				if(Config.ATIVACAO_GERAL)
					this.gerarAtivacaoPopulacao(saida, saidaEscondida, i, qntInd, epoca, g);
			}
			ind.setFitness(fitness);
			qntInd++;
		}
	}

}
