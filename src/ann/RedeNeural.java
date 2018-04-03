package ann;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import geral.Auxiliar;
import geral.Config;

public class RedeNeural {

	private Neuronio camadaEntrada[];
	private Neuronio camadaEscondida[];
	private Neuronio camadaAssociativa[];
	private Neuronio camadaSaida[];
	private double saidaEscondida[];
	private double saidaAssociativa[];
	private boolean associativaAtivou = false;
	
	/**
	 * 
	 * @param n_entradas (Numero de entrada)
	 * @param n_neuronios (Numero de neuronios da camada escondida)
	 * @param n_n_associativa (Numero de neuronios da camada associativa)
	 * @param n_saidas (Numero de neuronios de saida)
	 */
	public RedeNeural(int n_entradas, int n_neuronios, int n_n_associativa, int n_saidas) {
		int tipo_funcao_ativacao = 1;
		if(Config.WTA) {
			tipo_funcao_ativacao = -1;
		}
		//criação da camada de entrada
		this.camadaEntrada = new Neuronio[n_entradas];
		for(int i=0; i<n_entradas; i++) {
			this.camadaEntrada[i] = new Neuronio(Config.RECORRENCIA_ENTRADA);
			this.camadaEntrada[i].setTipoCamada(0);
		}
		//criacao da camada intermediaria
		this.camadaEscondida = new Neuronio[n_neuronios];
		for(int i=0; i<n_neuronios; i++)
			this.camadaEscondida[i] = new Neuronio(n_entradas+n_n_associativa, tipo_funcao_ativacao,
					Config.RECORRENCIA_OUTROS, 1);
		//criacao da camada intermediaria ASSOCIATIVA
		this.camadaAssociativa = new Neuronio[n_n_associativa];
		for(int i=0; i<n_n_associativa; i++)
			this.camadaAssociativa[i] = new Neuronio(n_entradas, tipo_funcao_ativacao,
					Config.RECORRENCIA_OUTROS, 2);
		//criacao da camada de saida
		this.camadaSaida = new Neuronio[n_saidas];
		for(int i=0; i<n_saidas; i++)
			this.camadaSaida[i] = new Neuronio(n_neuronios, Config.FUNCAO_ATIVACAO_SAIDA, Config.RECORRENCIA_OUTROS, 3);
	}
	
	public double[] calculaSaida(double[] entradas) {
		//obtem saidas da camada de entrada
		double saidaDaEntrada[] = new double[entradas.length];
		for(int i=0; i<camadaEntrada.length; i++) {
			saidaDaEntrada[i] = camadaEntrada[i].ativado(entradas[i]);
		}
		
		//obtem saidas da camada associativa
		associativaAtivou = false;
		saidaAssociativa = new double[camadaAssociativa.length];
		if(Auxiliar.USAR_CAMADA_ASSOCIATIVA) {
			for(int i=0; i<camadaAssociativa.length; i++) {
				saidaAssociativa[i] = camadaAssociativa[i].ativado(saidaDaEntrada);
			}
			
			//calcula o WTA
			//Esse WTA é modificado, ele pode sair 0 em todos, mas nunca 1 em todos.
			//Se o neurônio mais ativo for maior que 0, então temos ativação da camada
			//caso contrário, não ocorre ativação da camada
			if(Config.WTA) {
				double maior = saidaAssociativa[0];
				int index = 0; //indice no vetor do maior valor
				for(int i=1; i<saidaAssociativa.length; i++) {
					if(saidaAssociativa[i] > maior) {
						maior = saidaAssociativa[i];
						saidaAssociativa[index] = 0;
						index = i;
					} else {
						saidaAssociativa[i] = 0;
					}
				}
				// só permite a ativação do WTA se o neurônio mais ativo
				// for maior que 0
				if(saidaAssociativa[index] > 0) {
					saidaAssociativa[index] = 1;
					associativaAtivou = true;
				} else {
					saidaAssociativa[index] = 0;
				}
				
				/*
				for(int i=0; i<saidaAssociativa.length; i++)
				{
					if(i!=index)
						saidaAssociativa[i] = 0;
					else
						saidaAssociativa[i] = 1;
				} */
			}
		} else {
			for(int i=0; i<camadaAssociativa.length; i++)
				saidaAssociativa[i] = 0;
		}
		
		
		
		//obtem saidas da camada escondida
		/*
		 * A entrada da camada escondida é composta pelas entradas da camada de entrada
		 * e entradas da camada associativa. A camada escondida so leva em conta os resultados
		 * da camada associativa se Auxiliar.USAR_CAMADA_ASSOCIATIVA estiver ativo (true).
		 */
		saidaEscondida= new double[this.camadaEscondida.length];
		double[] entradaCamadaEscondida = new double[saidaDaEntrada.length + saidaAssociativa.length];
		for(int i=0;i<entradaCamadaEscondida.length;i++)
			entradaCamadaEscondida[i] = 0;
		
		//prepara entrada da camada escondida
        if(Auxiliar.USAR_CAMADA_ASSOCIATIVA) { //entrada composta por: entrada + associativa
        	if(associativaAtivou) {
        		for(int i=0;i<saidaDaEntrada.length;i++)
        			saidaDaEntrada[i] = 0;
        		System.arraycopy(saidaDaEntrada, 0, entradaCamadaEscondida, 0, saidaDaEntrada.length);
        		System.arraycopy(saidaAssociativa, 0, entradaCamadaEscondida, saidaDaEntrada.length, saidaAssociativa.length);
        	} else {
        		System.arraycopy(saidaDaEntrada, 0, entradaCamadaEscondida, 0, saidaDaEntrada.length);
            	for(int i=0;i<saidaAssociativa.length;i++)
            		saidaAssociativa[i] = 0;
            	System.arraycopy(saidaAssociativa, 0, entradaCamadaEscondida, saidaDaEntrada.length, saidaAssociativa.length);
        	}
        } else {
        	System.arraycopy(saidaDaEntrada, 0, entradaCamadaEscondida, 0, saidaDaEntrada.length);
        	for(int i=0;i<saidaAssociativa.length;i++)
        		saidaAssociativa[i] = 0;
        	System.arraycopy(saidaAssociativa, 0, entradaCamadaEscondida, saidaDaEntrada.length, saidaAssociativa.length);
        }
        //fornece entradas para a camada intermediaria
        for(int i=0; i<camadaEscondida.length; i++) {
			saidaEscondida[i] = camadaEscondida[i].ativado(entradaCamadaEscondida); //entrada com resultados contabilizados
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
	
	public Neuronio[] getNeuroniosAssociativa() throws Exception {
		return copyCamada(this.camadaAssociativa);
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
	
	public double[] getSaidaCamadaAssociativa() {
		return this.saidaAssociativa;
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
	
	public void setCamadaAssociativa(Neuronio[] camadaAssociativa) {
		this.camadaAssociativa = camadaAssociativa;
	}
	
	public boolean ativouCamadaAssociativa() {
		return this.associativaAtivou;
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
			int limite = n.getNumeroPesos();
			if(!Config.USAR_BIAS)
				limite = limite - 1; //não imprime o BIAS
			for(int i =0; i<limite; i++) {
				strCamadaEscondida+=" ["+pesos[i]+"]";
			}
			strCamadaEscondida+="\n";
			strCamadaEscondidaTaus+=" ["+n.getTau()+"]";
		}
		
		String strCamadaAssociativa = "Camada Associativa [pesos]: \n";
		String strCamadaAssociativaTaus = "Camada Associativa [TAUs]: ";
		for(Neuronio n : this.camadaAssociativa) {
			double[] pesos = null;
			try {
				pesos = n.getPesos();
			} catch (Exception e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			} 
			int limite = n.getNumeroPesos();
			if(!Config.USAR_BIAS)
				limite = limite - 1; //não imprime o BIAS
			for(int i =0; i<limite; i++) {
				strCamadaAssociativa+=" ["+pesos[i]+"]";
			}
			strCamadaAssociativa+="\n";
			strCamadaAssociativaTaus+=" ["+n.getTau()+"]";
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
			int limite = n.getNumeroPesos();
			if(!Config.USAR_BIAS)
				limite = limite - 1; //não imprime o BIAS
			for(int i =0; i<limite; i++) {
				strCamadaSaida+=" ["+pesos[i]+"]";
			}
			strCamadaSaida+="\n";
			strCamadaSaidaTaus+=" ["+n.getTau()+"]";
		}
		
		if(Config.RECORRENCIA_OUTROS)
			return "\n"+strCamadaEntrada+"\n"+strCamadaEscondida+strCamadaEscondidaTaus
				+"\n"+strCamadaAssociativa+strCamadaAssociativaTaus
				+"\n"+strCamadaSaida+strCamadaSaidaTaus;
		else
			return "\n"+strCamadaEntrada+"\n"+strCamadaEscondida
					+"\n"+strCamadaAssociativa
					+"\n"+strCamadaSaida;
	}
	
	public void zerarValoresRecorrencia()
	{
		for(int i=0;i<camadaEntrada.length;i++) {
			camadaEntrada[i].zeraValorRecorrente();
		}
	}
}
