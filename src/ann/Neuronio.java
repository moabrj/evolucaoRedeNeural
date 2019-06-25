package ann;

import java.util.Random;

import geral.Auxiliar;
import geral.Config;

public class Neuronio {
	
	private double pesos[];
	private double tau = 0;
	private double somaAnt = 0;
	private boolean recorrencia = false;
	private boolean tipo_entrada = false; //se true é ativo será neuronio de entrada
	private int funcaoAtivacao = 1;
	private int tipoCamada = 1; //camda intermediaria por default
	
	
	private Neuronio() {
		Random r = Config.random;
		if(Config.RECORRENCIA_ENTRADA_FIXA)
			this.tau = Config.VALOR_TAU_FIXO;
		else
			this.tau = Auxiliar.truncate(r.nextDouble());
	}
	
	/**
	 * O número de pesos deve ser igual ao número de entradas
	 * O tipoFuncaoAtivacao pode ser 1-hiperbolica, 2-sigmoide,3-degrau,-1-WTA(Repassa a saída)
	 * O tipoCamada pode ser 0-Entrada, 1-Intermadiária, 2-Associativa, 3-Saída
	 * @param n_pesos
	 * @param tipoFuncaoAtivacao
	 * @param recorrencia
	 * @param tipoCamada
	 */
	public Neuronio(int n_pesos, int tipoFuncaoAtivacao, boolean recorrencia, int tipoCamada) {
		pesos = new double[n_pesos+1]; //+1 para o BIAS
		this.funcaoAtivacao = tipoFuncaoAtivacao;
		this.recorrencia = recorrencia;
		this.tipoCamada = tipoCamada;
		Random r = Config.random;
		if(Config.RECORRENCIA_ENTRADA_FIXA)
			this.tau = Config.VALOR_TAU_FIXO;
		else
			this.tau = Auxiliar.truncate(r.nextDouble());
		
		for(int i=0;i<n_pesos+1;i++)
		{
			if(tipoCamada != 2) //não for associativa
				pesos[i] = Auxiliar.truncate(Config.LIMITE_MIN + (r.nextDouble() * (Config.LIMITE_MAX - Config.LIMITE_MIN)));
			else
				pesos[i] = 0;
		}
		
		//pesos ligados na camada escondida são zerados
		if(tipoCamada == 1) { //camada escondida
			//pesos[n_pesos-1] é BIAS
			pesos[n_pesos-1] = 0; //ultimo
			pesos[n_pesos-2] = 0; //penultimo
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
		
		if(Config.RECORRENCIA_ENTRADA_FIXA)
			this.tau = Config.VALOR_TAU_FIXO;
		else
			this.tau = Auxiliar.truncate(r.nextDouble());
	}
	
	public double[] getPesos() throws Exception {
		if(tipo_entrada)
			throw new Exception("Não é possível obter pesos de neurônios do tipo input!");
		return pesos;
	}
	
	public double ativado(double entradas[]) throws Exception {
		double soma = 0;
		//somatorio
		if(!tipo_entrada) { //se não for do tipo entrada
			for(int i=0; i<entradas.length;i++)
				soma+=(entradas[i]*pesos[i]);
			if(Config.USAR_BIAS)
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
		//if(recorrencia)
		//	somaAnt = soma;
		return soma;
	}
	
	private double funcaoAtivacao(double entrada) throws Exception {
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
		} else if ( funcaoAtivacao == 3 ) { //degrau
			if(entrada >= 1)
				return 1;
			else
				return 0;
		}else //para WTA
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
	
	public void setTipoCamada(int tipo) {
		this.tipoCamada = tipo;
	}

}
