package ag;

import java.awt.BasicStroke;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.Graphics2D;
import java.awt.Rectangle;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.FileWriter;
import java.io.IOException;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.io.PrintWriter;
import java.io.Writer;
import java.text.Format;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.Date;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.Properties;
import java.util.Random;

import org.apache.batik.dom.svg.SVGDOMImplementation;
import org.apache.batik.svggen.SVGGraphics2D;
import org.freehep.graphics2d.VectorGraphics;
import org.freehep.graphicsio.pdf.PDFGraphics2D;
import org.freehep.graphicsio.ps.PSGraphics2D;
import org.jfree.chart.ChartFactory;
import org.jfree.chart.ChartUtilities;
import org.jfree.chart.JFreeChart;
import org.jfree.chart.LegendItem;
import org.jfree.chart.LegendItemCollection;
import org.jfree.chart.axis.ValueAxis;
import org.jfree.chart.plot.PlotOrientation;
import org.jfree.chart.plot.SeriesRenderingOrder;
import org.jfree.chart.plot.XYPlot;
import org.jfree.chart.renderer.xy.XYItemRenderer;
import org.jfree.data.xy.CategoryTableXYDataset;
import org.w3c.dom.DOMImplementation;
import org.w3c.dom.Document;
import org.freehep.graphicsio.PageConstants;

import ann.Neuronio;
import geral.Auxiliar;
import geral.Config;
import geral.LeitorCSV;

public class AlgoritmoEvolutivo {
	
	private LeitorCSV leitor;
	private Populacao populacao;
	private FileWriter arq = null;
	private PrintWriter gravarArq = null;
	private FileWriter arqAtivacao = null;
	private PrintWriter gravarArqAtivacao = null;
	private JFreeChart grafico;
	private String nomeArquivo;
	private HistoricoEvolutivo[] historico; 
	
	public AlgoritmoEvolutivo() throws IOException {
		this.historico = this.criaListaHistoricoEvolutivo();
		this.leitor = new LeitorCSV();
		//this.leitor.getEntradas();
		this.populacao = new Populacao(this.historico);
		Date dataHoraAtual = new Date();
		String data = new SimpleDateFormat("dd-MM-yyyy").format(dataHoraAtual);
		String hora = new SimpleDateFormat("HH-mm-ss").format(dataHoraAtual);
		nomeArquivo = "resultados\\"+data+hora+"-N-"+Config.N_NEURONIOS_ESCONDIDOS;
		
		arqAtivacao = new FileWriter(nomeArquivo+"Ativacao"+".txt");
		gravarArqAtivacao = new PrintWriter(arqAtivacao);
		
		arq = new FileWriter(nomeArquivo+".txt");
		gravarArq = new PrintWriter(arq);
		
		registraCabecalho();
	}

	public void evoluir() throws Exception {
		int cont = 1;
		CategoryTableXYDataset data = new CategoryTableXYDataset();
		
		//Configura o primeiro ciclo de treinamento
		if(Auxiliar.TREINO_COM_CICLOS)
		{
			this.leitor.setArquivo(Config.TREINO_CICLO_1);
			Auxiliar.USAR_CAMADA_ASSOCIATIVA = false;
		}
		this.leitor.obterArquivo();
		
		while(cont <= Config.N_MAX_GERACOES) {
			this.gravarArq.println("Geração "+cont+"\n\n");
			
			if(Auxiliar.TREINO_COM_CICLOS)
			{
				if(cont == 50) { //treinar ciclo 1
					this.leitor.setArquivo(Config.TREINO_CICLO_2);
					Auxiliar.USAR_CAMADA_ASSOCIATIVA = true;
					Auxiliar.CICLO_2 = true;
					this.leitor.obterArquivo(); //realizar leitura do arquivo .csv
				} 
			}
			
			//calcula o fitness de todos os individuos da população
			if(Config.N_SAIDAS == 4) //para 4 saidas
				this.populacao.calcularFitness4Saida(leitor.getEntradas(), leitor.q_linhas, cont);
			else
				this.populacao.calcularFitness2Saida(leitor.getEntradas(), leitor.q_linhas, cont);
			//salva os dados
			this.populacao.registrarLogs(gravarArq);
			//adiciona o melhor na lista de melhores da geração
			data.add(cont, this.populacao.getMelhorFitness(), "Melhor");
			data.add(cont, this.populacao.getMediaFitnessPop(), "Média");
			//seleciona os 10 melhores
			this.populacao.selecionaMelhores();
			
			//salva dados de ativacao do melhor individuo
			StringBuilder str_ativacao = null;
			if(!Config.ATIVACAO_GERAL)
				str_ativacao = this.populacao.getAtivacaoMelhor(leitor.getEntradas(), leitor.q_linhas, cont);
			//else {
			//	str_ativacao = this.historico.getListagemAtivacaoNeural();
			//	this.historico.limparHistoricoAtivacaoNeural();
			//}
			
			//this.gravarArqAtivacao.println("\n\nGeração "+cont+":");
			this.gravarArqAtivacao.println(str_ativacao.toString());
			this.gravarArqAtivacao.flush();
			
			//verifica andamento
			System.out.println("Geração "+cont+". Melhor individuo: "+this.populacao.getIndividuos().getFirst().getFitness());
			
			//preenche o restante da população com mutações dos melhores
			this.mutacao();
			
			cont++;
		}
		this.arq.close();
		this.gravarArqAtivacao.close();
		
		//registra dados de fitness do processo evolutivo em arquivo
		FileWriter arqMelhor = new FileWriter(nomeArquivo+"-MELHOR.csv");
		PrintWriter gravarMelhor = new PrintWriter(arqMelhor);
		FileWriter arqMedia = new FileWriter(nomeArquivo+"-MEDIA.csv");
		PrintWriter gravarMedia = new PrintWriter(arqMedia);
		for(int i=0;i<Config.N_MAX_GERACOES;i++) {
			//melhor
			Number x = data.getX(0, i);
			Number y = data.getY(0, i);
			//media
			Number x1 = data.getX(1, i);
			Number y1 = data.getY(1, i);
			gravarMelhor.println(x+", "+y);
			gravarMedia.println(x1+", "+y1);
		}
		gravarMelhor.close();
		gravarMedia.close();
		
		//salva gráfico de processo evolutivo
		grafico = ChartFactory.createXYLineChart("Evolução", "Geração", 
			    "Fitness", data, PlotOrientation.VERTICAL, true, false, false);
		this.salvaGrafico("evolucao", 2);
		
		
		for(int k=0;k<Config.NUMERO_CASOS;k++) {
			this.historico[k].gerarDataset();
			CategoryTableXYDataset dataAtivacao = this.historico[k].getGraficoDataset();
			grafico = ChartFactory.createXYLineChart("Ativação Neural", "Geração", 
				    "Percentual", dataAtivacao, PlotOrientation.VERTICAL, true, false, false);
			for(int t=0;t<dataAtivacao.getSeriesCount();t++) {
				FileWriter arqNeuronio = new FileWriter(nomeArquivo+"-H_"+k+"-N_"+(t+1)+".csv");
				PrintWriter gravarNeuronio = new PrintWriter(arqNeuronio);
				
				//registra dados da evolução da ativação neural em arquivo 
				for(int i=0;i<Config.N_MAX_GERACOES;i++) {
					Number x = dataAtivacao.getX(t, i);
					Number y = dataAtivacao.getY(t, i);
					gravarNeuronio.println(x+", "+y);
				}
				gravarNeuronio.close();
			}
		}
		
		//salva grafico de ativacao neural
		/*
		CategoryTableXYDataset dataAtivacao = this.historico.getGraficoDataset();
		grafico = ChartFactory.createXYLineChart("Ativação Neural", "Geração", 
			    "Percentual", dataAtivacao, PlotOrientation.VERTICAL, true, false, false);
		this.salvaGrafico("ativacao", 1);
		*/
	}
	
	private void mutacao() throws Exception {
		int q_ind = this.populacao.getTamanhoPopulacao();
		LinkedList<Individuo> novos_individuos = new LinkedList<>();
		LinkedList<Individuo> copia_pop = this.populacao.getIndividuos();
		Random rand = Config.random;
	
		while(q_ind < Config.N_IND_POP) {
			Individuo ind = copia_pop.get(rand.nextInt(10)).copia();
			
			//nesta parte são alterados todos os pesos se o neurônio for selecionado
			//também é escolhida uma camada a ser alterada
			Neuronio[] neuronios = ind.getCamadaEntrada();
			for(Neuronio neuronio : neuronios) {
				if(rand.nextDouble() < Config.TAXA_MUTACAO) {
					double x = rand.nextDouble();
					x = Auxiliar.truncate(x);
					neuronio.setTau(x);
				}
			}
			ind.setCamadaEntrada(neuronios);
			
			//realiza mutação nos pesos dos neurônios da camada escondida
			neuronios = ind.getCamadaEscondida();
			for(Neuronio neuronio : neuronios) {
				//altera pesos/bias do neuronio
				if(rand.nextDouble() < Config.TAXA_MUTACAO) {
					double[] pesos = new double[neuronio.getNumeroPesos()];
					for(int i=0; i<neuronio.getNumeroPesos(); i++) {
						pesos[i] = Auxiliar.truncate(Config.LIMITE_MIN + (rand.nextDouble() * (Config.LIMITE_MAX - Config.LIMITE_MIN)));
					}
					neuronio.setPesos(pesos);
					
					//o tau pode ser mutado ou não
					if(rand.nextDouble() < Config.TAXA_MUTACAO) {
						double x = rand.nextDouble();
						x = Auxiliar.truncate(x);
						neuronio.setTau(x);
					}
				}
			}
			ind.setCamadaEscondida(neuronios);
			
			//realiza mutação nos pesos dos neurônios da camada associativa
			if(Auxiliar.USAR_CAMADA_ASSOCIATIVA) {
				neuronios = ind.getCamadaAssociativa();
				for(Neuronio neuronio : neuronios) {
					//altera pesos/bias do neuronio
					if(rand.nextDouble() < Config.TAXA_MUTACAO) {
						double[] pesos = new double[neuronio.getNumeroPesos()];
						for(int i=0; i<neuronio.getNumeroPesos(); i++) {
							pesos[i] = Auxiliar.truncate(Config.LIMITE_MIN + (rand.nextDouble() * (Config.LIMITE_MAX - Config.LIMITE_MIN)));
						}
						neuronio.setPesos(pesos);
						
						//o tau pode ser mutado ou não
						if(rand.nextDouble() < Config.TAXA_MUTACAO) {
							double x = rand.nextDouble();
							x = Auxiliar.truncate(x);
							neuronio.setTau(x);
						}
					}
				}
				ind.setCamadaAssociativa(neuronios);
			}
			
			//realiza mutação nos pesos dos neurônios de saída
			neuronios = ind.getCamadaSaida();
			for(Neuronio neuronio : neuronios) {
				//altera pesos/bias do neuronio
				if(rand.nextDouble() < Config.TAXA_MUTACAO) {
					double[] pesos = new double[neuronio.getNumeroPesos()];
					for(int i=0; i<neuronio.getNumeroPesos(); i++) {
						pesos[i] = Auxiliar.truncate(Config.LIMITE_MIN + (rand.nextDouble() * (Config.LIMITE_MAX - Config.LIMITE_MIN)));
					}
					neuronio.setPesos(pesos);
					//o tau pode ser mutado ou não
					if(rand.nextDouble() < Config.TAXA_MUTACAO) {
						double x = rand.nextDouble();
						x = Auxiliar.truncate(x);
						neuronio.setTau(x);
					}
				}
			}
			ind.setCamadaSaida(neuronios);
			
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
	
	public void export(File name, JFreeChart chart, int x, int y, int tipo, Font font) {
		if(tipo == 1) { //SVG
			// Get a DOMImplementation
	        DOMImplementation domImpl = SVGDOMImplementation.getDOMImplementation();
	        Document document = domImpl.createDocument(null, "svg", null);
	        SVGGraphics2D svgGenerator = new SVGGraphics2D(document);
	        chart.draw(svgGenerator,new Rectangle(x,y));
	        boolean useCSS = true; // we want to use CSS style attribute
	        Writer out;
			try {
				out = new OutputStreamWriter(new FileOutputStream(name), "UTF-8");
				svgGenerator.stream(out, useCSS);
				out.close();
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		} else if (tipo == 2) { //EPS
			try {
				VectorGraphics g = new PSGraphics2D(name, new Dimension(x, y));
				Properties p = new Properties();
				p.setProperty(PSGraphics2D.ORIENTATION, "Landscape");
				g.setProperties(p);
				g.setFont(font);
				g.startExport();
		        chart.draw( g,new Rectangle(x,y));
		        g.endExport();
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		} else if (tipo == 3) { //PDF
			PDFGraphics2D g;
			try {
				g = new PDFGraphics2D(name, new Dimension(x, y));
				Properties p = new Properties();
		        p.setProperty(PDFGraphics2D.class.getName()+"."+PageConstants.ORIENTATION, PageConstants.LANDSCAPE);
		        //p.setProperty(PDFGraphics2D.class.getName()+"."+PageConstants.FIT_TO_PAGE, "true");
		        p.setProperty(PDFGraphics2D.class.getName()+"."+PageConstants.PAGE_SIZE, PageConstants.INTERNATIONAL);
				//p.setProperty(PDFGraphics2D.class.getName()+"."+PageConstants.PAGE_MARGINS, PageConstants.SMALL);
		        //p.setProperty(PDFGraphics2D.PAGE_SIZE, "800x600");
		        //p.setProperty(PDFGraphics2D.PAGE_SIZE, "pdf");
				g.setProperties(p);
				//g.setFont(new Font("Dialog", Font.PLAIN, 16));
				g.startExport();
		        chart.draw( g,new Rectangle(x,y));
		        g.endExport();
			} catch (FileNotFoundException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
    }
	 
	private void salvaGrafico(String str, int tipo_range) throws IOException
	{
		//configura cor, largura de linha
		XYPlot chart = grafico.getXYPlot();
		chart.setBackgroundPaint(null);
		
		Font font = new Font("Dialog", Font.PLAIN, 38);
		
		//muda largura das linhas nas series
		int seriesCount = chart.getSeriesCount();
		for (int i = 0; i < seriesCount; i++) {
			chart.getRenderer().setSeriesStroke(i, new BasicStroke(2));
		}
		
		ArrayList<LegendItem> legendItems = new ArrayList<LegendItem>();
	    Iterator<LegendItem> itr = chart.getLegendItems().iterator();
	    while (itr.hasNext()) {
	        legendItems.add(itr.next());
	    }
	    /*
	    //Reverse the order
	    Collections.sort(legendItems, new Comparator<LegendItem>() {
	        public int compare(LegendItem lhs, LegendItem rhs) {
	            return rhs.getSeriesKey().compareTo(lhs.getSeriesKey());
	        }
	    });
	    */
	    LegendItemCollection newItems = new LegendItemCollection();
	    for (LegendItem item : legendItems) {
	    	item.setLabelFont(new Font("Dialog", Font.PLAIN, 18));
	    	item.setLineStroke(new BasicStroke(2));
	        newItems.add(item);
	    }
	    chart.setFixedLegendItems(newItems);
		
		
		//muda tamanho de numeros do intervalo em x e y
		ValueAxis axisX = chart.getRangeAxis();
		ValueAxis axisY = chart.getDomainAxis();
		axisX.setTickLabelFont(new Font("Dialog", Font.PLAIN, 18));
		axisY.setTickLabelFont(new Font("Dialog", Font.PLAIN, 18));
		axisX.setLabelFont(new Font("Dialog", Font.BOLD, 18));
		axisY.setLabelFont(new Font("Dialog", Font.BOLD, 18));
		if(tipo_range == 1) { //percentual
			axisY.setRange(0, Config.N_MAX_GERACOES);
			axisX.setRange(0, 100);
		}
		else
			axisX.setRange(-60, Config.MAX_FITNESS);
		
		OutputStream arquivo = new FileOutputStream(nomeArquivo+str+".png");
		ChartUtilities.writeChartAsPNG(arquivo, grafico, 800, 600);
		
		//this.export(new File(nomeArquivo+"SVG.svg"), grafico, 800, 600, 1, font); //salva em svg
		//this.export(new File(nomeArquivo+"EPS.eps"), grafico, 800, 600, 2, font); //salva em eps
		
		//this.export(new File(nomeArquivo+str+"PDF.pdf"), grafico, 800, 600, 3, font); //salva em pdf
		
		arquivo.close();
		
	}
	
	private HistoricoEvolutivo[] criaListaHistoricoEvolutivo() {
		HistoricoEvolutivo[] historicos = new HistoricoEvolutivo[Config.NUMERO_CASOS];
		for(int i=0; i<Config.NUMERO_CASOS; i++) {
			historicos[i] = new HistoricoEvolutivo();
		}
		return historicos;
	}

}
