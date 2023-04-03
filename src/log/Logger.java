package log;


import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;

public class Logger {
	
	File arquivo;
	BufferedWriter bufferedWriter;
	FileWriter fileWriter;
	
	
	public Logger() {
		
	}
	
	
	public void criarArquivoDeLog(String diretorio) {
		try {
		
		this.arquivo = new File(diretorio + "/log.txt");
		
		if(!arquivo.exists()) {
			arquivo.createNewFile();
		}
		
		fileWriter = new FileWriter(diretorio + "/log.txt",true);
		
		bufferedWriter = new BufferedWriter(fileWriter);
		

		
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	
	
	public void adicionarRegistro(String data, String hora, String professor, String disciplina, String msg) {
		try {
			
				bufferedWriter.write("Data: " + data + "  -  Hora: " + hora + "  -  Professor: " + professor + "  -  Disciplina: " + disciplina + "  -  Msg: " + msg);
				bufferedWriter.newLine();
				bufferedWriter.flush();
				
		
		} catch (IOException e) {
			e.printStackTrace();
		}

	}
	
	
	public void fecharArquivoDeLog() {
		try {	
			bufferedWriter.close();
		
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

}
