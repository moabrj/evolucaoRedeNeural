package ann;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import geral.Config;

public class RedeNeural {

	private Neuronio camadaEntrada[];
	private Neuronio camadaEscondida[];
	private Neuronio camadaSaida[];
	private double saidaEscondida[];
	
	public RedeNeural(int n_entradas, int n_neuronios, int n_saidas) {
		int tipo_funcao_ativacao = 1;
		if(Config.WTA) {
			tipo_funcao_ativacao = 2;
		}
		//criação da camada de entrada
		this.camadaEntrada = new Neuronio[n_entradas];
		for(int i=0; i<n_entradas; i++)
			this.camadaEntrada[i] = new Neuronio(Config.RECORRENCIA_ENTRADA);
		//criacao da camada intermediaria
		this.camadaEscondida = new Neuronio[n_neuronios];
		for(int i=0; i<n_neuronios; i++)
			this.camadaEscondida[i] = new Neuronio(n_entradas, tipo_funcao_ativacao,
					Config.RECORRENCIA_OUTROS);
		//criacao da camada intermediaria
		this.camadaSaida = new Neuronio[n_saidas];
		for(int i=0; i<n_saidas; i++)
			this.camadaSaida[i] = new Neuronio(n_neuronios, 1, Config.RECORRENCIA_OUTROS);
	}
	
	public double[] calculaSaida(double[] entradas) {
		//obtem saidas da camada de entrada
		double saidaDaEntrada[] = new double[entradas.length];
		for(int i=0; i<camadaEntrada.length; i++) {
			saidaDaEntrada[i] = camadaEntrada[i].ativado(entradas[i]);
		}
		//obtem saidas da camada escondida
		saidaEscondida= new double[this.camadaEscondida.length];
		for(int i=0; i<camadaEscondida.length; i++) {
			saidaEscondida[i] = camadaEscondida[i].ativado(saidaDaEntrada);
		}
		
		if(Config.WTA) {
			double maior = saidaEscondida[0];
			int index = 0;
			for(int i=1; i<saidaEscondida.length; i++) {
				if(saidaEscondida[i] > maior) {
					maior = saidaEscondida[i];
					index = i;
				}
			}
			for(int i=0; i<saidaEscondida.length; i++)
			{
				if(i!=index)
					saidaEscondida[i] = 0;
				else
					saidaEscondida[i] = 1;
			}
		}
		
		//obtem saidas da camada de saida
		double saidaFinal[] = new double[camadaSaida.length];
		for(int i=0; i<camadaSaida.length; i++) {
			saidaFinal[i] = camadaSaida[i].ativado(saidaEscondida);
		}
		
		return saidaFinal;
	}
	
	public Neuronio[] getNeuroniosEscondidos() throws Exception {
		return copyCamada(this.camadaEscondida);
	}
	
	public Neuronio[] getNeuroniosSaida() throws Exception {
		return copyCamada(this.camadaSaida);
	}
	
	public Neuronio[] getNeuroniosEntrada() throws Exception {
		return copyCamada(this.camadaEntrada);
	}
	
	private Neuronio[] copyCamada(Neuronio[] camada) throws Exception {
		Neuronio escondidaCopia[] = new Neuronio[camada.length];
		for(int i=0; i<camada.length; i++) {
			escondidaCopia[i] = camada[i].copia();
		}
		return escondidaCopia;
	}
	
	public double[] getSaidaEscondida() {
		return saidaEscondida;
	}
	
	public void setCamadaSaida(Neuronio[] camadaSaida) {
		this.camadaSaida = camadaSaida;
	}
	
	public void setCamadaEscondida(Neuronio[] camadaEscondida) {
		this.camadaEscondida = camadaEscondida;
	}
	
	public void setCamadaEntrada(Neuronio[] camadaEntrada) {
		this.camadaEntrada = camadaEntrada;
	}
	
	public String toString() {
		String strCamadaEntrada = "Camada de Entrada (TAUs): ";
		for(Neuronio n : this.camadaEntrada) {
			strCamadaEntrada+=" "+n.getTau();
		}
		String strCamadaEscondida = "Camada Escondida [peso]: \n";
		String strCamadaEscondidaTaus = "Camada Escondida [TAUs]: ";
		for(Neuronio n : this.camadaEscondida) {
			double[] pesos = null;
			try {
				pesos = n.getPesos();
			} catch (Exception e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			} 
			for(int i =0; i<n.getNumeroPesos(); i++) {
				strCamadaEscondida+=" ["+pesos[i]+"]";
			}
			strCamadaEscondida+="\n";
			strCamadaEscondidaTaus+=" ["+n.getTau()+"]";
		}
		String strCamadaSaida = "Camada Saida [peso]: \n";
		String strCamadaSaidaTaus = "Camada Saida [TAUs]: ";
		for(Neuronio n : this.camadaSaida) {
			double[] pesos = null;
			try {
				pesos = n.getPesos();
			} catch (Exception e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			} 
			for(int i =0; i<n.getNumeroPesos(); i++) {
				strCamadaSaida+=" ["+pesos[i]+"]";
			}
			strCamadaSaida+="\n";
			strCamadaSaidaTaus+=" ["+n.getTau()+"]";
		}
		return "\n"+strCamadaEntrada+"\n"+strCamadaEscondida+strCamadaEscondidaTaus
				+"\n"+strCamadaSaida+strCamadaSaidaTaus;
	}
	
	
}
