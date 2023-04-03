import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Scanner;
import java.util.concurrent.TimeUnit;

import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;
import org.openqa.selenium.Alert;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.chrome.ChromeOptions;
import org.openqa.selenium.firefox.FirefoxBinary;
import org.openqa.selenium.firefox.FirefoxDriver;
import org.openqa.selenium.firefox.FirefoxOptions;
import org.openqa.selenium.htmlunit.HtmlUnitDriver;

import log.Logger;

public class ModuloLancamentoFrequenciaEscolar {	
		WebDriver navegador;
		String enderecoPagina;
		String HTMLPagina;
		String user;
		String password;
		WebElement elementoPagina;
		Scanner input = new Scanner( System.in );	
		JSONObject jsonObject;
		JSONParser parser = new JSONParser();
		String Disciplina;
		String Codigo;
		String Professor;
		String Turma;
		String DataAula;
		String HorarioInicio;
		String HorarioFim;
		String QTDAula;
		String Conteudo;
		int Bimestre;
		String Pergunta;
		ArrayList <String> Alunos;
		ArrayList <String> Faltas;
		Logger logador = new Logger();
		
		
		
		// COMEÇO - ESCOLHE O NAVEGADOR ---------------------------------------
		public void iniciarChrome() {
			try {
				ChromeOptions options = new ChromeOptions();
				options.addArguments("--no-sandbox");
				options.addArguments("--disable-dev-shm-usage");
				System.out.println("Iniciando Google Chrome");
				navegador = new ChromeDriver();
				System.out.println("Google Chrome iniciado!");
			} catch (Exception e) {
				System.out.println(e.getMessage());
			}
		}
		
		public void iniciarChromeHeadless() {
			
			ChromeOptions options = new ChromeOptions();
			options.addArguments("--headless");
			options.addArguments("--ignore-certificate-errors");
			options.addArguments("--test-type");
			navegador = new ChromeDriver(options);
		}
		
		public void iniciarFirefox() {
			navegador = new FirefoxDriver(); 	
		}
		
		public void configNavegadorFirefoxHeadless() {
			try {
				System.out.println("Iniciando FirefoxBinary");
			    FirefoxBinary firefoxBinary = new FirefoxBinary();
			    System.out.println("FirefoxBinary iniciado!");
			    firefoxBinary.addCommandLineOptions("--headless");
			    FirefoxOptions firefoxOptions = new FirefoxOptions();
			    firefoxOptions.setBinary(firefoxBinary);
			    System.out.println("Iniciando FirefoxDriver");
			    navegador = new FirefoxDriver(firefoxOptions);
			    System.out.println("FirefoxDriver iniciado!");
			} catch (Exception e) {
				System.out.println(e.getMessage());
				//e.printStackTrace();
			}
		}
		
		public void iniciarHtmlUnitDriver() {
			navegador = new HtmlUnitDriver(); 
		}
		
		// FIM - ESCOLHE O NAVEGADOR -------------------------------------------
		
		public void acessarQAcademico_Professor(String pathPasta, String arquivoJsonFinal) {	
			
			enderecoPagina = "https://academico.ifmt.edu.br";
			navegador.get(enderecoPagina);		
				
			elementoPagina = navegador.findElement(By.partialLinkText("PROFESSOR"));
			elementoPagina.click();		
			
			elementoPagina = navegador.findElement(By.name("LOGIN"));		
			elementoPagina.sendKeys("99999");		
			elementoPagina = navegador.findElement(By.name("SENHA"));		
			elementoPagina.sendKeys("123456");				
			elementoPagina = navegador.findElement(By.name("Submit"));	
			elementoPagina.click();    
			
			elementoPagina = navegador.findElement(By.partialLinkText("Meus Diários"));	
			elementoPagina.click();   
			
			try {
				
			//jsonObject = (JSONObject) parser.parse(new FileReader("/home/ifmt/eclipse-workspace/selenium/teste.json"));
			jsonObject = (JSONObject) parser.parse(new FileReader(pathPasta + "/" + arquivoJsonFinal));
			DataAula = (String) jsonObject.get("data da aula");
			//Codigo = (String) jsonObject.get("Codigo");
			Codigo = "158611";
			HorarioInicio = (String) jsonObject.get("Horario de inicio");
			HorarioFim = (String) jsonObject.get("Horario de fim");
			QTDAula = (String) jsonObject.get("quantidade de aulas");
			Conteudo = (String) jsonObject.get("Conteudo");
			Alunos = (ArrayList <String>) jsonObject.get("lista de alunos");
			Faltas = (ArrayList <String>) jsonObject.get("Faltas");
			Bimestre = Integer.parseInt((String) jsonObject.get("Bimestre"));
			Pergunta = (String) jsonObject.get("Pergunta");
				
			//Trata as exceptions que podem ser lançadas no decorrer do processo		
			} catch (FileNotFoundException e) {
				e.printStackTrace();
			} catch (IOException e) {
				e.printStackTrace();
			} catch (ParseException e) {
				e.printStackTrace(); }
			
			
			// SE NOTA BIMESTRAL -----------------------------
			
			if (Pergunta.equals("Bimestral")){
			
					if (Bimestre == 1) {			
							// 1º BIMESTRE
								elementoPagina = navegador.findElement(By.xpath("//a[@href='https://academico.ifmt.edu.br/qacademico/index.asp?t=3066&MODO=FALTAS&COD_PAUTA=" + Codigo + "&ETAPA=1&N_ETAPA=1B']"));
								elementoPagina.click(); 						
							
					}	if (Bimestre == 2) {
							// 2º BIMESTRE
								elementoPagina = navegador.findElement(By.xpath("//a[@href='https://academico.ifmt.edu.br/qacademico/index.asp?t=3066&MODO=FALTAS&COD_PAUTA=" + Codigo + "&ETAPA=4&N_ETAPA=2B']"));
								elementoPagina.click(); 
							
					}	if (Bimestre == 3) {
							// 3º BIMESTRE		
								elementoPagina = navegador.findElement(By.xpath("//a[@href='https://academico.ifmt.edu.br/qacademico/index.asp?t=3066&MODO=FALTAS&COD_PAUTA=" + Codigo + "&ETAPA=7&N_ETAPA=3B']"));
								elementoPagina.click(); 
							
					}	if (Bimestre == 4) {
							// 4º BIMESTRE		
								elementoPagina = navegador.findElement(By.xpath("//a[@href='https://academico.ifmt.edu.br/qacademico/index.asp?t=3066&MODO=FALTAS&COD_PAUTA=" + Codigo + "&ETAPA=10&N_ETAPA=4B']"));
								elementoPagina.click(); 
					}
			} 
			
			
			// SE NOTA SEMESTRAL --------------------------------
			
			if (Pergunta.equals("Semestral")) {
				
				elementoPagina = navegador.findElement(By.xpath("//a[@href='https://academico.ifmt.edu.br/qacademico/index.asp?t=3066&MODO=FALTAS&COD_PAUTA=" + Codigo + "&ETAPA=1&N_ETAPA=NS']"));
				elementoPagina.click(); 
				
			} 
			
			elementoPagina = navegador.findElement(By.name("DT_AULA_MINISTRADA"));	 
			elementoPagina.sendKeys(DataAula);		
		
			elementoPagina = navegador.findElement(By.name("HORARIO_INI"));	
			elementoPagina.sendKeys(HorarioInicio);		
			
			elementoPagina = navegador.findElement(By.name("HORARIO_FIN"));	
			elementoPagina.sendKeys(HorarioFim);		
			
			elementoPagina = navegador.findElement(By.name("N_AULAS"));	
			elementoPagina.sendKeys(QTDAula);
		
			elementoPagina = navegador.findElement(By.name("CONTEUDO"));	 
			elementoPagina.sendKeys(Conteudo);
			
			//for(int i=0; i<Alunos.size(); i++) { 			  //lista os alunos que estao localizados no vetor
			//	System.out.println(Alunos.get(i));
					
			int indice = 7;
			for(int i=0; i<Faltas.size(); i++) {									
				
			elementoPagina = navegador.findElement(By.xpath("//*[@id=\"div_secao_esquerda\"]/tbody/tr["+indice+"]/td/input[@type=\"text\"]"));
				
					//elementoPagina = navegador.findElement(By.xpath("//*[@id=\"div_secao_esquerda\"]/tbody/tr["+indice+"]/td[1]/input"));
					
					//elementoPagina = navegador.findElement(By.xpath("//*[@id=\"div_secao_esquerda\"]/tbody/tr[7]/td[1]/input"));
					
					//*[@id="tabela_completa"]//*[@id="div_secao_esquerda"]/tbody/tr[7]/td[1]/input
					
					//*[@id="tabela_completa"]//*[@id="inv1"]//*[@id="div_secao_esquerda"]/tbody/tr[7]/td[1]/input
					
					                    
					//	String htmlElemento = elementoPagina.getAttribute("outerHTML");    RECUPERA O HTML DO INDICE--------
					//	System.out.println("htmlElemento: " + htmlElemento);   
				
			elementoPagina.sendKeys(Faltas.get(i));			
			indice = indice + 1;			
	}	
			elementoPagina = navegador.findElement(By.xpath("//html//tr[6]/td[1]/input[1][@type=\"submit\"]"));															
			elementoPagina.click();
			
			
			//-----------------------------------------------------------
			Alert confirmacao = navegador.switchTo().alert(); //se aparecer uma caixa de alerta de conflito
			confirmacao.accept();
			//-----------------------------------------------------------				
			
			
					//*[@id="div_secao_esquerda"]/tbody/tr[7]/td/input[@type="text"]  ----- VAI FUNCIONAR
				
					// XPath - Aluno            : //a[@tabindex='-1'][contains(text(),'Aluno Ficticio 1')]
					// XPath - Matricula Alunos : //a[@tabindex='-1'][contains(text(),'20181REDES0010')]				
					// XPath - Codigo  			: //strong[contains(text(),'154996')]
					// XPath - NS   			: //*[text() = 'NS']
					// XPath - Turma 			: //td[contains(text(),'20181.REDES.1N')]	
					
					 /*
					XPath do NOME DA DISCIPLINA
			
					/html/body/table/tbody/tr[2]/td/table/tbody/tr[2]/td[2]/table[2]/tbody/tr/td[2]/p[1]/table/tbody/tr[2]/td[2]/strong
			
					/html/body/table/tbody/tr[2]/td/table/tbody/tr[2]/td[2]/table[2]/tbody/tr/td[2]/p[1]/table/tbody/tr[3]/td[2]/strong
			
					OBS: Só muda o indice do ultimo " tr "

			-----------------------------------------------------------------------------------------------------------		*/
	}
			
		public void imprimeArray(String[] Alunos) {
		    for (int i = 0; i < Alunos.length; i++) {
		        System.out.println(Alunos[i]);
	    }
	}
				
		public ModuloLancamentoFrequenciaEscolar (String pathPasta, String arquivoJsonFinal) {
			
			try {
				logador.criarArquivoDeLog(".");
				System.out.println("Arquivo de log criado!");
				
				
				// Obter a data e a hora do sistema
				// Obter o nome do professor do arquivo JSON
				// Obter o nome da disciplina do arquivo JSON
				
				
				logador.adicionarRegistro("18/06/2018", "11:15", "Bernardo", "Seguranca em Redes", "Executou o programa");
				System.out.println("Novo registro adicionado");
				
				
				System.setProperty("webdriver.chrome.driver", "/home/ifmt/eclipse-workspace/CapFaceServer/exec/chromedriver");
				System.setProperty("webdriver.gecko.driver", "/home/ifmt/eclipse-workspace/CapFaceServer/execm/geckodriver");
				
				
				iniciarChrome(); //FUNIONA
				//iniciarHtmlUnitDriver(); //NAO FUNCIONA
				//iniciarFirefox(); //FUNCIONANDO
				//configNavegadorFirefoxHeadless(); //FUNCIONA
				//iniciarChromeHeadless(); //NAO FUNCIONA
					
				navegador.manage().timeouts().implicitlyWait(10, TimeUnit.SECONDS);	
				
				acessarQAcademico_Professor(pathPasta, arquivoJsonFinal);
				
				
				logador.adicionarRegistro("18/06/2018", "11:15", "Bernardo", "Seguranca em Redes", "Lancamento efetuado com sucesso");
				//System.out.println("Novo registro adicionado");
				
				logador.fecharArquivoDeLog();
				//System.out.println("Arquivo de log fechado!");
				
				
				
				//HTMLPagina = navegador.getPageSource();
				//System.out.println("\n\nCODIGO HTML DA PAGINA");
				//System.out.println(HTMLPagina);
			
			} catch (Exception e) {
				System.out.println(e.getMessage());
				e.printStackTrace();
			}
			
		}	
		
		
}

