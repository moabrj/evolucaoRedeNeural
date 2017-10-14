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
	
	public Populacao() {
		this.individuos = new LinkedList<>();
		for(int i=0; i<Config.N_IND_POP; i++)
			individuos.add(new Individuo());
	}
	
	
	public void calcularFitness(double[][] entradas, int q_linhas) {
		for(Individuo ind : individuos) {
			int fitness = 0;
			for(int i=0; i<q_linhas; i++) {
				double[] saida = ind.atualizaRede(entradas[i]);
				
				//centro
				if (entradas[i][7] == 0 && saida[1] == 1 && saida[0] == 0 && saida[2] == 0)
                    fitness += 1;
				else if (entradas[i][7] == 0 && saida[1] != 1)
                    fitness -= 1;
				else if (entradas[i][7] == 1 && saida[2] == 1 && saida[1] == 0 && saida[0] == 0)
                    fitness += 1;
				else if (entradas[i][7] == 1 && saida[2] != 1)
                    fitness -= 1;
				else if (entradas[i][7] == -1 && saida[0] == 1 && saida[1] == 0 && saida[2] == 0)
                    fitness += 1;
				else if (entradas[i][7] == -1 && saida[0] != -1)
                    fitness -= 1;
			}
			ind.setFitness(fitness);
		}
	}
	
	public void calcularFitness1Saida(double[][] entradas, int q_linhas) {
		for(Individuo ind : individuos) {
			int fitness = 0;
			for(int i=0; i<q_linhas; i++) {
				double[] saida = ind.atualizaRede(entradas[i]);
				
				//centro
				if (entradas[i][7] == 0 && saida[0] == 0)
                    fitness += 1;
				else if (entradas[i][7] == 0 && saida[0] != 0)
                    fitness -= 1;
				else if (entradas[i][7] == 1 && saida[0] == 1)
                    fitness += 1;
				else if (entradas[i][7] == 1 && saida[0] != 1)
                    fitness -= 1;
				else if (entradas[i][7] == -1 && saida[0] == -1)
                    fitness += 1;
				else if (entradas[i][7] == -1 && saida[0] != -1)
                    fitness -= 1;
			}
			ind.setFitness(fitness);
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

}