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
import java.util.Date;
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
import org.jfree.chart.axis.ValueAxis;
import org.jfree.chart.plot.PlotOrientation;
import org.jfree.chart.plot.XYPlot;
import org.jfree.data.xy.CategoryTableXYDataset;
import org.w3c.dom.DOMImplementation;
import org.w3c.dom.Document;
import org.freehep.graphicsio.PageConstants;

import ann.Neuronio;
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
	private HistoricoEvolutivo historico; 
	
	public AlgoritmoEvolutivo() throws IOException {
		this.historico = new HistoricoEvolutivo();
		this.leitor = new LeitorCSV();
		this.leitor.obterArquivo();
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
		while(cont <= Config.N_MAX_GERACOES) {
			this.gravarArq.println("Geração "+cont+"\n\n");
			//calcula o fitness de todos os individuos da população
			if(cont==222)
				cont=222;
			if(Config.N_SAIDAS == 3) //para 3 saidas
				this.populacao.calcularFitness3Saida(leitor.getEntradas(), leitor.q_linhas);
			else
				this.populacao.calcularFitness1Saida(leitor.getEntradas(), leitor.q_linhas, cont);
			//salva os dados
			this.populacao.registrarLogs(gravarArq);
			//adiciona o melhor na lista de melhores da geração
			data.add(cont, this.populacao.getMelhorFitness(), "Melhor");
			data.add(cont, this.populacao.getMediaFitnessPop(), "Média");
			//seleciona os 10 melhores
			this.populacao.selecionaMelhores();
			//salva dados de ativacao do melhor individuo
			StringBuilder str_ativacao = this.populacao.getAtivacaoMelhor(leitor.getEntradas(), leitor.q_linhas, cont);
			//StringBuilder str_ativacao = this.historico.getListagemAtivacaoNeural();
			//this.historico.limparHistoricoAtivacaoNeural();
			this.gravarArqAtivacao.println("\n\nGeração "+cont+":");
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
		grafico = ChartFactory.createXYLineChart("Evolução", "Geração", 
			    "Fitness", data, PlotOrientation.VERTICAL, true, false, false);
		this.salvaGrafico("evolucao");
		
		//grafico de ativacao
		CategoryTableXYDataset dataAtivacao = this.historico.getGraficoDataset();
		grafico = ChartFactory.createXYLineChart("Ativação Neural", "Geração", 
			    "Percentual", dataAtivacao, PlotOrientation.VERTICAL, true, false, false);
		this.salvaGrafico("ativacao");
		
	}
	
	private void mutacao() throws Exception {
		int q_ind = this.populacao.getTamanhoPopulacao();
		LinkedList<Individuo> novos_individuos = new LinkedList<>();
		LinkedList<Individuo> copia_pop = this.populacao.getIndividuos();
		Random rand = Config.random;
	
		while(q_ind < Config.N_IND_POP) {
			Individuo ind = copia_pop.get(rand.nextInt(10)).copia();
			//if(n < 0.2) { //20% de chance para o primeiro individuo
			//	ind = copia_pop.getFirst().copia();
			//} else if (n >= 0.2 && n < 0.7) { //50% de chance para individuos da 1º a 4º posicao
			//	ind = copia_pop.get(r.nextInt(5)+1).copia(); //de 1 a 4
			//} else { //30% para o restante
			//	ind = copia_pop.get(r.nextInt(5)+5).copia(); //de 5 a 9
			//}
			
			//nesta parte são alterados todos os pesos se o neurônio for selecionado
			//também é escolhida uma camada a ser alterada
			Neuronio[] neuronios = ind.getCamadaEntrada();
			for(Neuronio neuronio : neuronios) {
				if(rand.nextDouble() < Config.TAXA_MUTACAO) {
					double x = rand.nextDouble();
					neuronio.setTau(x);
				}
			}
			
			ind.setCamadaEntrada(neuronios);
			neuronios = ind.getCamadaEscondida();
			for(Neuronio neuronio : neuronios) {
				//altera pesos/bias do neuronio
				if(rand.nextDouble() < Config.TAXA_MUTACAO) {
					double[] pesos = new double[neuronio.getNumeroPesos()];
					for(int i=0; i<neuronio.getNumeroPesos(); i++) {
						pesos[i] = Config.LIMITE_MIN + (rand.nextDouble() * (Config.LIMITE_MAX - Config.LIMITE_MIN));
					}
					neuronio.setPesos(pesos);
					
					//o tau pode ser mutado ou não
					if(rand.nextDouble() < Config.TAXA_MUTACAO) {
						double x = rand.nextDouble();
						neuronio.setTau(x);
					}
				}
			}
			ind.setCamadaEscondida(neuronios);
			
			neuronios = ind.getCamadaSaida();
			for(Neuronio neuronio : neuronios) {
				//altera pesos/bias do neuronio
				if(rand.nextDouble() < Config.TAXA_MUTACAO) {
					double[] pesos = new double[neuronio.getNumeroPesos()];
					for(int i=0; i<neuronio.getNumeroPesos(); i++) {
						pesos[i] = Config.LIMITE_MIN + (rand.nextDouble() * (Config.LIMITE_MAX - Config.LIMITE_MIN));
						/*
						double adicao = -10 + (rand.nextDouble() * (-20)); //aleatorio entre -2 e 2
						if((pesos[i]+adicao)<Config.LIMITE_MAX && (pesos[i]+adicao)>Config.LIMITE_MIN)
							pesos[i]+=adicao;
						else if((pesos[i]+adicao)>Config.LIMITE_MAX)
							pesos[i] = Config.LIMITE_MAX;
						else
							pesos[i] = Config.LIMITE_MIN;
						*/
					}
					neuronio.setPesos(pesos);
					
					//o tau pode ser mutado ou não
					if(rand.nextDouble() < Config.TAXA_MUTACAO) {
						double x = rand.nextDouble();
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
				g.setFont(font);
				g.startExport();
		        chart.draw( g,new Rectangle(x,y));
		        g.endExport();
			} catch (FileNotFoundException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
    }
	 
	private void salvaGrafico(String str) throws IOException
	{
		//configura cor, largura de linha
		XYPlot chart = grafico.getXYPlot();
		chart.setBackgroundPaint(null);
		int seriesCount = chart.getSeriesCount();
		for (int i = 0; i < seriesCount; i++) {
			chart.getRenderer().setSeriesStroke(i, new BasicStroke(2));
		}
		
		ValueAxis axisX = chart.getRangeAxis();
		ValueAxis axisY = chart.getDomainAxis();
		Font font = new Font("Dialog", Font.PLAIN, 16);
		axisX.setTickLabelFont(font);
		axisY.setTickLabelFont(font);
		
		//OutputStream arquivo = new FileOutputStream(nomeArquivo+str+".png");
		//ChartUtilities.writeChartAsPNG(arquivo, grafico, 800, 600);
		//this.export(new File(nomeArquivo+"SVG.svg"), grafico, 800, 600, 1); //salva em svg
		//this.export(new File(nomeArquivo+"EPS.eps"), grafico, 800, 600, 2, font); //salva em eps
		this.export(new File(nomeArquivo+str+"PDF.pdf"), grafico, 800, 600, 3, font); //salva em pdf
		//arquivo.close();
		
	}

}
