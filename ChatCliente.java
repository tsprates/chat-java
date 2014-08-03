import java.net.Socket;
import java.net.InetSocketAddress;
import java.util.Scanner;
import java.io.InputStream;
import java.io.OutputStream;
import java.io.PrintWriter;


public class ChatCliente {
	
	private Socket socket;

	public ChatCliente(String ip, int porta) {
		Scanner teclado = new Scanner(System.in);
		
		try {
			socket = new Socket(ip, porta);
						
			System.out.println("Conectando ao servidor");
			
			new Thread(new TrataServidor(
						socket.getInputStream())).start();
			
			PrintWriter escritor = new PrintWriter(socket.getOutputStream());
			while (teclado.hasNextLine()) {
				escritor.println(teclado.nextLine());
				escritor.flush();
			}
		} catch (Exception ex) {
			ex.printStackTrace();
		}
	}
	
	private class TrataServidor implements Runnable {
		
		private Scanner leitor;
		
		public TrataServidor(InputStream stream) {
			leitor = new Scanner(stream);
		}
		
		public void run() {
			while (leitor.hasNextLine()) {
				System.err.println(leitor.nextLine());
			}
		}
	}
	
	public static void main(String[] args) {
		new ChatCliente("127.0.0.1", 9999);
	}

}