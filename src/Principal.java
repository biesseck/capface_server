import java.io.BufferedReader;
import java.io.DataInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.ServerSocket;
import java.net.Socket;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.zip.ZipEntry;
import java.util.zip.ZipInputStream;

public class Principal {

	ServerSocket serverSocket;
	Socket socketConexao;
	int porta = 3322;
	
	String dirUploads = "/home/ifmt/CapfaceUploads";
	String dirRegistroAula;
	String pathDirRegistroAula;
	String userName;
	String zipFileName;
	String pathZipFile;
	long zipFileSize;
	
	public Principal() {
		criarDiretorioDeUpload(dirUploads);
		inicializarServidor();
		
		while(true) {
			System.out.println("CAPFACE SERVER INICIADO");
			System.out.println(" -> Aguardando conexao...");
			socketConexao = aguardarConexao();
			System.out.println(" -> Usuario conectado: " + socketConexao.getInetAddress().toString());
			
			try {
				//System.out.println(" -> Aguardando arquivo do cliente...");
				getFile(socketConexao);
				//System.out.println(" -> Arquivo recebido!");
				
				System.out.println(" -> Descompactando arquivo ZIP para '" + pathDirRegistroAula + "'");
				descompactarArquivoZIP(pathZipFile, pathDirRegistroAula);			
				//System.out.println(" -> Arquivo ZIP descompactado");
				
				System.out.println("\n -> Iniciando Modulo de Reconhecimento Facial...");
				inicializarModuloDeReconhecimentoFacial();				
				System.out.println(" -> Terminado!");
				
				System.out.println("\n -> Iniciando Modulo de Lancamento de Registro de Aula...");
				inicializarModuloDeLancamentoDeFrequenciaEscolar();				
				System.out.println(" -> Terminado!");
				
			} catch (IOException e) {
				e.printStackTrace();
			}
			
			System.out.println("");
		
		}
	}
	
	
	public void inicializarModuloDeReconhecimentoFacial() throws IOException {
		//String pythonCommand = "python3 /home/bernardo/PycharmProjects/TestesPython/showImage.py /home/bernardo/eclipse-workspace_teste/CapFaceServer/testfile.jpg";
		String pythonCommand = "python3 /home/ifmt/pycharm-workspace/face_recognition_capface_module/recognizeFaces.py " + pathDirRegistroAula + " inicial.json";
		Process p = Runtime.getRuntime().exec(pythonCommand);
		BufferedReader stdInput = new BufferedReader(new InputStreamReader(p.getErrorStream()));
		
		String s;
		while ((s = stdInput.readLine()) != null) {
            System.out.println(s);
        }
	}
	
	
	public void inicializarModuloDeLancamentoDeFrequenciaEscolar() {
		new ModuloLancamentoFrequenciaEscolar(pathDirRegistroAula, "final.json");
	}
	
	
	public void receberDadosDeUpload(DataInputStream dis) throws IOException {
		DateFormat dateFormat = new SimpleDateFormat("MM/yyyy HH:mm:ss");
		Date date = new Date();
		String dataHoraAtual = dateFormat.format(date); //16/11/2016 12:08:43
		dataHoraAtual = dataHoraAtual.replace("/", "-");
		dataHoraAtual = dataHoraAtual.replace(" ", "_");
		dataHoraAtual = dataHoraAtual.replace(":", "-");
		
		userName = dis.readUTF();
		//System.out.println(" -->> userName: " + userName);
		
		zipFileName = dis.readUTF();
		//System.out.println(" -->> zipFileName: " + zipFileName);
		
		zipFileSize = Long.parseLong(dis.readUTF());
		//System.out.println(" -->> zipFileSize: " + zipFileSize);
		
		dirRegistroAula = userName + "_" + dataHoraAtual;
		pathDirRegistroAula = dirUploads + "/" + dirRegistroAula;
		pathZipFile = dirUploads + "/" + zipFileName;
	}
	
	
	public void getFile(Socket clientSock) throws IOException {
		DataInputStream dis = new DataInputStream(clientSock.getInputStream());		
		receberDadosDeUpload(dis);
		FileOutputStream fos = new FileOutputStream(pathZipFile);
		byte[] buffer = new byte[4096];
		
		int filesize = (int) zipFileSize; // Send file size in separate msg
		int read = 0;
		int totalRead = 0;
		int remaining = filesize;
		System.out.println(" -> Recebendo registro de aula: " + zipFileName);
		while((read = dis.read(buffer, 0, Math.min(buffer.length, remaining))) > 0) {
			totalRead += read;
			remaining -= read;
			fos.write(buffer, 0, read);
			fos.flush();
		}
		
		fos.close();
		dis.close();
	}
	
	
	/*
	public void descompactarArquivoZIP(String zipFilePath, String destDir) throws IOException {
		String unzipCommand = "unzip " + zipFilePath + " -d " + destDir;
		Process p = Runtime.getRuntime().exec(unzipCommand);
	}
	*/
	
	
	public void descompactarArquivoZIP(String zipFilePath, String destDir) {
		File dir = new File(destDir);
        // create output directory if it doesn't exist
        if(!dir.exists()) dir.mkdirs();
        FileInputStream fis;
        //buffer for read and write data to file
        byte[] buffer = new byte[1024];
        try {
            fis = new FileInputStream(zipFilePath);
            ZipInputStream zis = new ZipInputStream(fis);
            ZipEntry ze = zis.getNextEntry();
            while(ze != null){
                String fileName = ze.getName();
                File newFile = new File(destDir + File.separator + fileName);
                //System.out.println("Unzipping to " + newFile.getAbsolutePath());
                //create directories for sub directories in zip
                new File(newFile.getParent()).mkdirs();
                FileOutputStream fos = new FileOutputStream(newFile);
                int len;
                while ((len = zis.read(buffer)) > 0) {
                	fos.write(buffer, 0, len);
                }
                fos.close();
                //close this ZipEntry
                zis.closeEntry();
                ze = zis.getNextEntry();
            }
            //close last ZipEntry
            zis.closeEntry();
            zis.close();
            fis.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
        
    }
    
	
	
	
	public Socket aguardarConexao() {
		Socket cliente = null;
		try {
			cliente = serverSocket.accept();
		} catch (IOException e) {
			e.printStackTrace();
		}
		return cliente;
	}
	
	
	public void inicializarServidor() {
		try {
			serverSocket = new ServerSocket(porta);
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	
	public void criarDiretorioDeUpload(String path) {
		File f = new File(path);
		if (!f.exists()) {
			f.mkdirs();
		}
	}
	
	
	public static void main(String[] args) {
		new Principal();
	}

}
