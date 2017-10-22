package ag;

import java.io.FileOutputStream;
import java.io.FileWriter;
import java.io.IOException;
import java.io.OutputStream;
import java.io.PrintWriter;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.LinkedList;
import java.util.Random;

import org.jfree.chart.ChartFactory;
import org.jfree.chart.ChartUtilities;
import org.jfree.chart.JFreeChart;
import org.jfree.chart.axis.CategoryAxis;
import org.jfree.chart.plot.PlotOrientation;
import org.jfree.data.category.DefaultCategoryDataset;
import org.jfree.data.time.TimeSeries;
import org.jfree.data.xy.CategoryTableXYDataset;
import org.jfree.data.xy.XYDataset;
import org.jfree.data.xy.XYSeries;

import ann.Neuronio;
import geral.Config;
import geral.LeitorCSV;

public class AlgoritmoEvolutivo {
	
	private LeitorCSV leitor;
	private Populacao populacao;
	private FileWriter arq = null;
	private PrintWriter gravarArq = null;
	private JFreeChart grafico;
	private String nomeArquivo;
	
	public AlgoritmoEvolutivo() throws IOException {
		this.leitor = new LeitorCSV();
		this.leitor.obterArquivo();
		//this.leitor.getEntradas();
		this.populacao = new Populacao();
		Date dataHoraAtual = new Date();
		String data = new SimpleDateFormat("dd-MM-yyyy").format(dataHoraAtual);
		String hora = new SimpleDateFormat("HH-mm-ss").format(dataHoraAtual);
		nomeArquivo = "resultados\\"+data+hora+"-N-"+Config.N_NEURONIOS_ESCONDIDOS;
		arq = new FileWriter(nomeArquivo+".txt");
		gravarArq = new PrintWriter(arq);
		registraCabecalho();
	}
	
	public void evoluir() throws Exception {
		int cont = 1;
		CategoryTableXYDataset data = new CategoryTableXYDataset();
		while(cont <= Config.N_MAX_GERACOES) {
			this.gravarArq.println("Geração "+cont+"\n\n");
			//calcula o fitness de todos os individuos da população
			if(Config.N_SAIDAS > 1) //para 3 saidas
				this.populacao.calcularFitness(leitor.getEntradas(), leitor.q_linhas);
			else
				this.populacao.calcularFitness1Saida(leitor.getEntradas(), leitor.q_linhas);
			//salva os dados
			this.populacao.registrarLogs(gravarArq);
			//adiciona o melhor na lista de melhores da geração
			data.add(cont, this.populacao.getMelhorFitness(), "Melhor");
			data.add(cont, this.populacao.getMediaFitnessPop(), "Média");
			//seleciona os 10 melhores
			this.populacao.selecionaMelhores();
			//verifica andamento
			System.out.println("Geração "+cont+". Melhor individuo: "+this.populacao.getIndividuos().getFirst().getFitness());
			//preenche o restante da população com mutações dos melhores
			this.mutacao();
			cont++;
		}
		this.arq.close();
		grafico = ChartFactory.createXYLineChart("Evolução", "Geração", 
			    "Fitness", data, PlotOrientation.VERTICAL, true, false, false);
		
		OutputStream arquivo = new FileOutputStream(nomeArquivo+".png");
		ChartUtilities.writeChartAsPNG(arquivo, grafico, 800, 600);
		arquivo.close();
	}
	
	private void mutacao() throws Exception {
		int q_ind = this.populacao.getTamanhoPopulacao();
		LinkedList<Individuo> novos_individuos = new LinkedList<>();
		LinkedList<Individuo> copia_pop = this.populacao.getIndividuos();
		Random r = Config.random;
		while(q_ind < Config.N_IND_POP) {
			double n = r.nextDouble();
			Individuo ind;
			if(n < 0.2) { //20% de chance para o primeiro individuo
				ind = copia_pop.getFirst().copia();
			} else if (n >= 0.2 && n < 0.7) { //50% de chance para individuos da 1º a 4º posicao
				ind = copia_pop.get(r.nextInt(5)+1).copia(); //de 1 a 4
			} else { //30% para o restante
				ind = copia_pop.get(r.nextInt(5)+5).copia(); //de 5 a 9
			}
			
			//nesta parte são alterados todos os pesos se o neurônio for selecionado
			//também é escolhida uma camada a ser alterada
			n = r.nextDouble();
			Neuronio[] neuronios;
			if(n < 0.2) { //altera tau da camada de entrada
				neuronios = ind.getCamadaEntrada();
				for(Neuronio neuronio : neuronios) {
					if(r.nextDouble() < Config.TAXA_MUTACAO) {
						double x = r.nextDouble();
						neuronio.setTau(x);
					}
				}
				ind.setCamadaEntrada(neuronios);
			} else if (n >= 0.2 && n < 0.7) { //altera neuronios da camada escondida
				neuronios = ind.getCamadaEscondida();
				for(Neuronio neuronio : neuronios) {
					//altera pesos/bias do neuronio
					if(r.nextDouble() < Config.TAXA_MUTACAO) {
						double[] pesos = new double[neuronio.getNumeroPesos()];
						for(int i=0; i<neuronio.getNumeroPesos(); i++) {
							pesos[i] = Config.LIMITE_MIN + (r.nextDouble() * (Config.LIMITE_MAX - Config.LIMITE_MIN));
						}
						neuronio.setPesos(pesos);
						
						//o tau pode ser mutado ou não
						if(r.nextDouble() < Config.TAXA_MUTACAO) {
							double x = r.nextDouble();
							neuronio.setTau(x);
						}
					}
				}
				ind.setCamadaEscondida(neuronios);
			} else { //altera neuronios da camada de saida
				neuronios = ind.getCamadaSaida();
				for(Neuronio neuronio : neuronios) {
					//altera pesos/bias do neuronio
					if(r.nextDouble() < Config.TAXA_MUTACAO) {
						double[] pesos = new double[neuronio.getNumeroPesos()];
						for(int i=0; i<neuronio.getNumeroPesos(); i++) {
							pesos[i] = Config.LIMITE_MIN + (r.nextDouble() * (Config.LIMITE_MAX - Config.LIMITE_MIN));
						}
						neuronio.setPesos(pesos);
						
						//o tau pode ser mutado ou não
						if(r.nextDouble() < Config.TAXA_MUTACAO) {
							double x = r.nextDouble();
							neuronio.setTau(x);
						}
					}
				}
				ind.setCamadaSaida(neuronios);
			}
			novos_individuos.add(ind);
			q_ind++;
		}
		this.populacao.getIndividuos().addAll(novos_individuos);
	}
	
	private void registraCabecalho()
	{
		gravarArq.println("Informações gerais: ");
		gravarArq.println("Topologia da rede: "+Config.N_ENTRADAS+" entradas, "+
				Config.N_NEURONIOS_ESCONDIDOS+" neurônios escondidos, "+Config.N_SAIDAS+" saidas.");
		gravarArq.println("Número de individuos da população: "+Config.N_IND_POP);
		gravarArq.println("Número máximo de iterações/gerações: "+Config.N_MAX_GERACOES);
		gravarArq.println("Taxa de mutação: "+Config.TAXA_MUTACAO);
		gravarArq.println("Arquivo usado: "+Config.NOME_ARQUIVO);
		gravarArq.println("Recorrência de entrada = "+Config.RECORRENCIA_ENTRADA
				+", Recorrência em outras camadas = "+Config.RECORRENCIA_OUTROS);
		gravarArq.println("Semente do random: "+Config.SEMENTE);
		gravarArq.println();
	}

}
