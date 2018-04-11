package geral;
import java.io.FileWriter;
import java.io.PrintWriter;
import java.util.Random;

import ag.AlgoritmoEvolutivo;
import ann.Neuronio;
import ann.RedeNeural;
import coppelia.*;

public class Main {
	
	public static void main(String[] args) throws Exception {
		///*
		AlgoritmoEvolutivo ae = new AlgoritmoEvolutivo();
		ae.evoluir();
		
		/*
		LeitorCSV leitor = new LeitorCSV();
		leitor.setArquivo(Config.TREINO_CICLO_2);
		leitor.obterArquivo();
		
		Random r = Config.random;
		
		double[][] entradas = leitor.getEntradas();
		StringBuilder saida = new StringBuilder();
		for(int i =0;i<leitor.q_linhas;i++) {
			String str = null;
			for(int j=0; j<7; j++) {
				double value = (0.009 + (r.nextDouble() * (0.009 + 0.009)));
				str = String.valueOf(Auxiliar.truncateFour(entradas[i][j] + value))+",";
				saida.append(str);
			}
			saida.append(entradas[i][7]+"\n");
			System.out.println(i);
		}
		FileWriter arq = new FileWriter("treino_ciclo_ruido.csv");
		PrintWriter gravar = new PrintWriter(arq);
		
		gravar.print(saida.toString());
		
		arq.close();
		gravar.close();
		*/
		
		//*/
		
		//teste api v-rep
		/*
		System.out.println("Program started");
        remoteApi vrep = new remoteApi();
        vrep.simxFinish(-1); // just in case, close all opened connections
        int clientID = vrep.simxStart("127.0.0.1",19999,true,true,5000,5);
        if (clientID!=-1)
        {
            System.out.println("Connected to remote API server");   

            // Now try to retrieve data in a blocking fashion (i.e. a service call):
            IntWA objectHandles = new IntWA(1);
            int ret=vrep.simxGetObjects(clientID,vrep.sim_handle_all,objectHandles,vrep.simx_opmode_blocking);
            if (ret==vrep.simx_return_ok)
                System.out.format("Number of objects in the scene: %d\n",objectHandles.getArray().length);
            else
                System.out.format("Remote API function call returned with error code: %d\n",ret);
                
            try
            {
                Thread.sleep(2000);
            }
            catch(InterruptedException ex)
            {
                Thread.currentThread().interrupt();
            }
    
            // Now retrieve streaming data (i.e. in a non-blocking fashion):
            long startTime=System.currentTimeMillis();
            IntW mouseX = new IntW(0);
            vrep.simxGetIntegerParameter(clientID,vrep.sim_intparam_mouse_x,mouseX,vrep.simx_opmode_streaming); // Initialize streaming
            while (System.currentTimeMillis()-startTime < 5000)
            {
                ret=vrep.simxGetIntegerParameter(clientID,vrep.sim_intparam_mouse_x,mouseX,vrep.simx_opmode_buffer); // Try to retrieve the streamed data
                if (ret==vrep.simx_return_ok) // After initialization of streaming, it will take a few ms before the first value arrives, so check the return code
                    System.out.format("Mouse position x: %d\n",mouseX.getValue()); // Mouse position x is actualized when the cursor is over V-REP's window
            }
            
            // Now send some data to V-REP in a non-blocking fashion:
            vrep.simxAddStatusbarMessage(clientID,"Hello V-REP!",vrep.simx_opmode_oneshot);

            // Before closing the connection to V-REP, make sure that the last command sent out had time to arrive. You can guarantee this with (for example):
            IntW pingTime = new IntW(0);
            vrep.simxGetPingTime(clientID,pingTime);

            // Now close the connection to V-REP:   
            vrep.simxFinish(clientID);
        }
        else
            System.out.println("Failed connecting to remote API server");
        System.out.println("Program ended");
		*/
	}
	
	private static String decode(String s) {
		String result = "";
		int i = 0;
		while(s.charAt(i) != ']' && i<s.length()) {
			if(Character.isDigit(s.charAt(i))) {
				int n = Integer.parseInt(String.valueOf(s.charAt(i)));
				int j = 0;
				i++;
				String x = "";
				if(s.charAt(i) == '[') 
					x+=decode(s.substring(i+1));
				else
					x=String.valueOf(s.charAt(i));
				while(j<n) {
					x+=x;
					j++;
				}
			}
			else if(s.charAt(i) == '[')
				result+=decode(s.substring(i+1));
			else
				result+=String.valueOf(s.charAt(i));
			
			i++;
		}
		return result;
	}

}
