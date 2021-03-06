package ag;

import java.util.Arrays;

import ann.Neuronio;
import ann.RedeNeural;
import geral.Config;

public class Individuo implements Comparable<Individuo>{
	
	private RedeNeural redeNeural;
	private double fitness = -999;
	
	public Individuo() {
		this.redeNeural = new RedeNeural(Config.N_ENTRADAS, 
				Config.N_NEURONIOS_ESCONDIDOS, Config.N_NEURONIOS_ASSOCIATIVA,
				Config.N_SAIDAS);
	}
	
	public double[] atualizaRede(double[] entradas, int input_count) throws Exception {
		if(fitness == 108)
			fitness = 108;
		double[] e_copy = entradas;
		if(entradas.length > 7)
			e_copy = Arrays.copyOfRange(entradas, 0, 7); //o limiar é exclusivo, logo, os indices são de 0 a 6
		return redeNeural.calculaSaida(e_copy, input_count);
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
	
	public Neuronio[] getCamadaEntradaRecorrente() throws Exception {
		return this.redeNeural.getNeuroniosEntradaRecorrente();
	}
	
	public Neuronio[] getCamadaAssociativa() throws Exception {
		return this.redeNeural.getNeuroniosAssociativa();
	}
	
	public void setCamadaSaida(Neuronio[] camadaSaida) {
		this.redeNeural.setCamadaSaida(camadaSaida);
	}
	
	public void setCamadaEscondida(Neuronio[] camadaEscondida) {
		this.redeNeural.setCamadaEscondida(camadaEscondida);
	}
	
	public void setCamadaAssociativa(Neuronio[] camadaAssociativa) {
		this.redeNeural.setCamadaAssociativa(camadaAssociativa);
	}
	
	public void setCamadaEntrada(Neuronio[] camadaEntrada) {
		this.redeNeural.setCamadaEntrada(camadaEntrada);
	}
	
	public void setCamadaEntradaRecorrente(Neuronio[] camadaEntrada) {
		this.redeNeural.setCamadaEntradaRecorrente(camadaEntrada);
	}
	
	public void setFitness(double fitness) {
		this.fitness = fitness;
	}
	
	public double getFitness() {
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
		ind.setCamadaEntradaRecorrente(this.getCamadaEntradaRecorrente());
		ind.setCamadaEscondida(this.getCamadaEscondida());
		ind.setCamadaSaida(this.getCamadaSaida());
		ind.setCamadaAssociativa(this.getCamadaAssociativa());
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
	
	public double[] ativacaoCamadaAssociativa()
	{
		return this.redeNeural.getSaidaCamadaAssociativa();
	}
	
	public boolean ativouCamadaAssociativa() {
		return this.redeNeural.ativouCamadaAssociativa();
	}
	
	public void reiniciaRecorrencia()
	{
		this.redeNeural.zerarValoresRecorrencia();
	}

	public boolean ativouCamadaEscondida() {
		return this.redeNeural.ativouCamadaEscondida();
	}

}
