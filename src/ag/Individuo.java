package ag;

import java.util.Arrays;

import ann.Neuronio;
import ann.RedeNeural;
import geral.Config;

public class Individuo implements Comparable<Individuo>{
	
	private RedeNeural redeNeural;
	private int fitness = -999;
	
	public Individuo() {
		this.redeNeural = new RedeNeural(Config.N_ENTRADAS, 
				Config.N_NEURONIOS_ESCONDIDOS, Config.N_SAIDAS);
	}
	
	public double[] atualizaRede(double[] entradas) {
		double[] e_copy = entradas;
		if(entradas.length > 7)
			e_copy = Arrays.copyOfRange(entradas, 0, 7); //o limiar é exclusivo, logo, os indices são de 0 a 6
		return redeNeural.calculaSaida(e_copy);
	}
	
	public Neuronio[] getCamadaEscondida() throws Exception {
		return this.redeNeural.getNeuroniosEscondidos();
	}
	
	public Neuronio[] getCamadaSaida() throws Exception {
		return this.redeNeural.getNeuroniosSaida();
	}
	
	public Neuronio[] getCamadaEntrada() throws Exception {
		return this.redeNeural.getNeuroniosEntrada();
	}
	
	public void setCamadaSaida(Neuronio[] camadaSaida) {
		this.redeNeural.setCamadaSaida(camadaSaida);
	}
	
	public void setCamadaEscondida(Neuronio[] camadaEscondida) {
		this.redeNeural.setCamadaEscondida(camadaEscondida);
	}
	
	public void setCamadaEntrada(Neuronio[] camadaEntrada) {
		this.redeNeural.setCamadaEntrada(camadaEntrada);
	}
	
	public void setFitness(int fitness) {
		this.fitness = fitness;
	}
	
	public int getFitness() {
		return fitness;
	}

	@Override
	public int compareTo(Individuo ind) {
		int compare = (int) ind.getFitness();
		   
        return (int) (compare - this.getFitness()); //ordem decrescente
	}
	
	public Individuo copia() throws Exception
	{
		Individuo ind = new Individuo();
		
		ind.setCamadaEntrada(this.getCamadaEntrada());
		ind.setCamadaEscondida(this.getCamadaEscondida());
		ind.setCamadaSaida(this.getCamadaSaida());
		//ind.setFitness(this.getFitness());
		return ind;
	}
	
	public String toString()
	{
		String str = "Fitness "+this.fitness+redeNeural.toString();
		return str;
		
	}
	
	public double[] ativacaoCamadaInter()
	{
		return this.redeNeural.getSaidaEscondida();
	}

}
