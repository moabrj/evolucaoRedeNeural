package geral;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;

public class LeitorCSV {
	
	private double[][] entradas = null;
	private ArrayList<Vetor> dados;
	public int q_linhas = 0;
	private String csvFile = Config.NOME_ARQUIVO;
	
	public void obterArquivo() {
		dados = new ArrayList<>();
        String line = "";
        String cvsSplitBy = ",";

        try (BufferedReader br = new BufferedReader(new FileReader(csvFile))) {
        	
        	String lineAux = line;
            while ((line = br.readLine()) != null) {
                // use comma as separator
            	lineAux = line;
                String[] lido = line.split(cvsSplitBy);
                dados.add(new Vetor(lido));
            }
            
            int i = 0;
            q_linhas = dados.size();
            int colunas = dados.get(0).dados.length;
            entradas = new double[q_linhas][colunas];
            for(Vetor e : dados) {
            	entradas[i] = e.getDados();
            	i++;
            }

        } catch (IOException e) {
            e.printStackTrace();
        }
	}
	
	public double[][] getEntradas() throws Exception
	{
		if(entradas != null) {
			return entradas;
		} else
			throw new Exception("O vetor com entradas não foi "
					+ "carregado ou ocorreu um erro. Método getEntradas em LeitorCSV");
		
	}
	
	class Vetor {
		double[] dados;
		String[] dadosRaw;
		public Vetor(String[] dados) {
			this.dadosRaw = dados;
			transformaDados();
		}
		
		private void transformaDados() {
			dados = new double[dadosRaw.length];
			for(int i=0; i<dadosRaw.length; i++)
			{
				dados[i] = Double.parseDouble(dadosRaw[i]);
			}
		}
		
		public double[] getDados()
		{
			return this.dados;
		}
	}
	
	/**
	 * O nome deve ser dado com o caminho absoluto.
	 * @param nome
	 */
	public void setArquivo(String nome) {
		this.csvFile = nome;
	}

}

