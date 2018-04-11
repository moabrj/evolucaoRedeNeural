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

import geral.Auxiliar;
import geral.Config;

public class Populacao {
	
	private LinkedList<Individuo> individuos;
	private HistoricoEvolutivo[] historico;
	
	public Populacao(HistoricoEvolutivo[] historico) {
		this.historico = historico;
		this.individuos = new LinkedList<>();
		for(int i=0; i<Config.N_IND_POP; i++)
			individuos.add(new Individuo());
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
		StringBuilder ativacaoNeural_str = new StringBuilder();
		ativacaoNeural_str.append("Geração "+epoca+":");
		Individuo ind = this.individuos.getFirst();
		
		int[] peqInter = {0,0,0,0,0};
		int[] graInter = {0,0,0,0,0};
		int[] dirInter = {0,0,0,0,0};
		int[] esqInter = {0,0,0,0,0};
		int[] peqAss = {0,0,0,0,0};
		int[] graAss = {0,0,0,0,0};
		int[] dirAss = {0,0,0,0,0};
		int[] esqAss = {0,0,0,0,0};
		
		ind.reiniciaRecorrencia();
		ativacaoNeural_str.append("\nMelhor Individuo:\n");
		double entradaAnt = ds[0][7];
		for(int i=0; i<q_linhas; i++) {
			if(entradaAnt != ds[i][7]) {
				ind.reiniciaRecorrencia();
				entradaAnt = ds[i][7];
			}
			double[] saida = ind.atualizaRede(ds[i]);
			double[] saidaEscondida = ind.ativacaoCamadaInter();
			double[] saidaAssociativa = ind.ativacaoCamadaAssociativa();
			boolean ativouCamadaAssociativa = ind.ativouCamadaAssociativa();
			boolean ativouCamadaEscandida = ind.ativouCamadaEscondida();
			for(int j=0;j<saidaEscondida.length;j++)
				ativacaoNeural_str.append(String.valueOf((int)saidaEscondida[j])+" ");
			ativacaoNeural_str.append("| ");
			for(int j=0;j<saidaAssociativa.length;j++)
				ativacaoNeural_str.append(String.valueOf((int)saidaAssociativa[j])+" ");
			//tipo de ativacao - camada associativa ou caminho normal
			ativacaoNeural_str.append("| "+ativouCamadaAssociativa+" ");
			ativacaoNeural_str.append("|=> ");
			for(int j=0;j<saida.length;j++)
				ativacaoNeural_str.append(String.valueOf((int)saida[j])+" ");
			ativacaoNeural_str.append("\n");
			
			int j = -1;
			if(ativouCamadaEscandida) {
				j = posMaximo(saidaEscondida);
				//montagem grafico
				if(i < 43) { //pequeno
					peqInter[j]++;
				} else if(i>42 && i<86) { //grande
					graInter[j]++;
				} else if(i>85 && i<129) { //direita
					dirInter[j]++;
				} else { //esquerda
					esqInter[j]++;
				}
			}
			//montagem grafico
			if(ativouCamadaAssociativa) {
				j = posMaximo(saidaAssociativa);
				if(i < 43) { //pequeno
					peqAss[j]++;
				} else if(i>42 && i<86) { //grande
					graAss[j]++;
				} else if(i>85 && i<129) { //direita
					dirAss[j]++;
				} else { //esquerda
					esqAss[j]++;
				}
			}
			
		}
		
		int [][] vetoresAtivacao = {peqInter, graInter, dirInter, esqInter, peqAss, graAss, dirAss, esqAss};
		int fitnessMax = 43;
		for(int i=0;i<Config.NUMERO_CASOS;i++) {
			Geracao g = new Geracao();
			g.addRegistroAtivacao(vetoresAtivacao[i], fitnessMax, epoca, this.historico[i], 0);
		}
		
		return ativacaoNeural_str;
	}

	
	public void gerarAtivacaoPopulacao(double[] saidaFinal, double[] saidaEscondida, int linha, int qntInd, int epoca, Geracao g) throws Exception {
		/*
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
		 */
	}
	
	
	/**
	 * Retorna qual neurônio foi ativado para uma dada entrada
	 * @param v
	 * @return
	 * @throws Exception
	 */
	private int posMaximo(double[] v) throws Exception {
		for(int i=0;i<v.length;i++)
		{
			if(v[i] > 0) {
				return i;
			}
		}
		throw new Exception("WTA não funcionou, verifique o método posMaximo em População!!");
	}
	
	public void calcularFitness1Saida(double[][] entradas, int q_linhas, int epoca) throws Exception {
		double entradaAnt = entradas[0][7];
		int qntInd = 0;
		Geracao g = new Geracao();
		for(Individuo ind : individuos) {
			//para documentacao
			//this.historico.neuroniosAtivos.append("\nIndividuo "+qntInd+"\n");
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
				if (entradas[i][7] == 0 && saida[0] == 0)
                    fitness += 1;
				else if (entradas[i][7] == 0 && saida[0] != 0)
                    fitness -= 1;
				else if (entradas[i][7] == 1 && saida[0] == 1)
                    fitness += 1;
				else if (entradas[i][7] == 1 && saida[0] != 1)
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
	 * O individuo é classificado(fitness) de acordo com a resposta.
	 * Movimento para direita e objeto grande deve ser classificado como afastar
	 * Movimento para esquerda e objeto pequeno deve ser classificado como aproximar
	 * @param entradas
	 * @param q_linhas
	 * @param epoca
	 * @throws Exception
	 */
	public void calcularFitness2Saida(double[][] entradas, int q_linhas, int epoca) throws Exception {
		double entradaAnt = entradas[0][7];
		int qntInd = 0;
		Geracao g = new Geracao();
		for(Individuo ind : individuos) {
			ind.reiniciaRecorrencia();
			//this.historico.neuroniosAtivos.append("\nIndividuo "+qntInd+"\n");
			double fitness = 0;
			double[] saida;
			for(int i=0; i<q_linhas; i++) {
				if(entradaAnt != entradas[i][7]) {
					ind.reiniciaRecorrencia();
					entradaAnt = entradas[i][7];
				}
				
				saida = ind.atualizaRede(entradas[i]);
				@SuppressWarnings("unused")
				double[] saidaEscondida = ind.ativacaoCamadaInter();
				double fitnessAnt = fitness;
				//aproximar
				if (entradas[i][7] == 0 && saida[0] == 1 && saida[1] == 0)
                    fitness += 1;
				else if (entradas[i][7] == 0 && saida[0] != 1 && saida[1] != 0)
                    fitness -= 1;
				//afastar
				else if (entradas[i][7] == 1 && saida[0] == 0 && saida[1] == 1)
                    fitness += 1;
				else if (entradas[i][7] == 1 && saida[0] != 0 && saida[1] != 1)
                    fitness -= 1;
				
				//para testes
				//pune individuos que não utilizam o caminho com camada associativa
				//decrementando o fitness
				/*
				if(Auxiliar.USAR_CAMADA_ASSOCIATIVA && Auxiliar.TREINO_COM_CICLOS) {
					if(i < 86) { //entradas de movimento para esquerda e direita
						if(fitness > fitnessAnt) {
							if(!ind.ativouCamadaAssociativa()) //ativou caminho normal
								fitness-=1;
						}
					}
				}
				*/
				
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
		for(Individuo ind : individuos) {
			ind.reiniciaRecorrencia();
			//this.historico.neuroniosAtivos.append("\nIndividuo "+qntInd+"\n");
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
		for(Individuo ind : individuos) {
			ind.reiniciaRecorrencia();
			//this.historico.neuroniosAtivos.append("\nIndividuo "+qntInd+"\n");
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
