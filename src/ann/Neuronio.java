package ann;

import java.util.Random;

import geral.Config;

public class Neuronio {
	
	private double pesos[];
	private double tau;
	private double somaAnt = 0;
	private boolean recorrencia = false;
	private boolean tipo_entrada = false; //se true é ativo será neuronio de entrada
	private int funcaoAtivacao = 1;
	
	
	private Neuronio() {
		Random r = Config.random;
		this.tau = r.nextDouble();
	}
	
	/**
	 * O número de pesos deve ser igual ao número de entradas
	 * @param n_pesos
	 * @param tau
	 */
	public Neuronio(int n_pesos, int tipoFuncaoAtivacao, boolean recorrencia) {
		pesos = new double[n_pesos+1]; //+1 para o BIAS
		this.funcaoAtivacao = tipoFuncaoAtivacao;
		this.recorrencia = recorrencia;
		Random r = Config.random;
		this.tau = r.nextDouble();
		for(int i=0;i<n_pesos+1;i++)
		{
			pesos[i] = Config.LIMITE_MIN + (r.nextDouble() * (Config.LIMITE_MAX - Config.LIMITE_MIN));
		}
	}
	
	/**
	 * O uso desse construtor implica em neurônios do tipo entrada. Estes
	 * neurônios apenas repassam a informação, podendo haver recorrência ou
	 * não.
	 * @param recorrencia
	 * @param tau
	 */
	public Neuronio(boolean recorrencia) {
		this.recorrencia = recorrencia;
		this.tipo_entrada = true;
		Random r = Config.random;
		this.tau = r.nextDouble();
	}
	
	public double[] getPesos() throws Exception {
		if(tipo_entrada)
			throw new Exception("Não é possível obter pesos de neurônios do tipo input!");
		return pesos;
	}
	
	public double ativado(double entradas[]) {
		double soma = 0;
		//somatorio
		if(!tipo_entrada) { //se não for do tipo entrada
			for(int i=0; i<entradas.length;i++)
				soma+=(entradas[i]*pesos[i]);
			soma+=pesos[pesos.length-1] + (somaAnt*tau); //BIAS + (somaAnt * tau)
			if(recorrencia)
				somaAnt = soma;
			
			//calcula ativacao
			return funcaoAtivacao(soma);
		} else {
			soma = entradas[0] + (somaAnt * tau);
			if(recorrencia)
				somaAnt = soma;
			return soma;
		}
	}
	
	public double ativado(double entrada) {
		double soma = 0;
		//somatorio
		soma = entrada + (somaAnt * tau);
		if(recorrencia)
			somaAnt = soma;
		return soma;
	}
	
	private double funcaoAtivacao(double entrada) {
		double resposta;
		if(funcaoAtivacao == 1) { //tangente hiperbolico
			resposta = Math.tanh(entrada);
			if(resposta >= 0.7)
				return 1;
			else if(resposta < 0.7 && resposta > -0.7)
				return 0;
			else
				return -1;
		} else if (funcaoAtivacao == 2) {
			resposta = 2/ (1 + Math.exp(-entrada));
			if(resposta >= 0.6)
				return 1;
			else
				return 0;
		} else if ( funcaoAtivacao == 3 ) {
			if(entrada > 10)
				entrada = 10;
			else if (entrada < -10)
				entrada = -10;
			resposta = (entrada + 10)/20;
			if(resposta >= 0.5)
				return 1;
			else
				return 0;
		}
		else //para WTA
			return entrada;
	}
	
	public double getTau() {
		return this.tau;
	}
	
	public void setPesos(double[] pesos) throws Exception {
		if(tipo_entrada)
			throw new Exception("Não é possível setar pesos de neurônios do tipo input!");
		this.pesos = pesos;
	}
	
	public void setTau(double t) {
		this.tau = t;
	}
	public boolean isRecorrencia() {
		return recorrencia;
	}

	public void setRecorrencia(boolean r) {
		this.recorrencia = r;		
	}
	
	public int getNumeroPesos() {
		return this.pesos.length;
	}
	
	public void setTipo_entrada(boolean tipo_entrada) {
		this.tipo_entrada = tipo_entrada;
	}
	
	public void setFuncaoAtivacao(int funcaoAtivacao) {
		this.funcaoAtivacao = funcaoAtivacao;
	}

	public Neuronio copia() throws Exception {
		Neuronio n = new Neuronio();
		if(!tipo_entrada)
			n.setPesos(pesos.clone());
		n.setTau(this.getTau());
		n.setRecorrencia(recorrencia);
		n.setTipo_entrada(tipo_entrada);
		n.setFuncaoAtivacao(funcaoAtivacao);
		return n;
	}
	
	public void zeraValorRecorrente()
	{
		this.somaAnt = 0;
	}

}
