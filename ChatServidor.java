import java.net.ServerSocket;
import java.net.Socket;
import java.util.List;
import java.util.ArrayList;
import java.util.Scanner;
import java.io.PrintWriter;
import java.io.InputStream;
import java.io.OutputStream;


public class ChatServidor {

	private ServerSocket socket;
	private List<Cliente> listaDeClientes = new ArrayList<Cliente>();
	
	private class Cliente {
		String ip;
		PrintWriter writer;
		
		public Cliente(String ip, OutputStream stream) {
			this.ip = ip;
			this.writer = new PrintWriter(stream);
		}
		
		public String getIp() {
			return ip;
		}
		
		public void envia(String msg) {
			writer.println(msg);
			writer.flush();
		}
	}
	
	private class TrataCliente implements Runnable {
		
		private Scanner leitor;
		
		public TrataCliente(InputStream stream) {
			leitor = new Scanner(stream);
		}
		
		public void run() {
			while (leitor.hasNextLine()){
				String s = leitor.nextLine();
				for (Cliente cliente : listaDeClientes) {
					cliente.envia(cliente.getIp() + " disse: " + s);
				}
			}
		}
	}

	public ChatServidor(int porta) {
		try {
			socket = new ServerSocket(porta);
			
			System.out.println("Iniciando o servidor");
			
			while (true) {
				Socket novoSocket = socket.accept();
				
				System.out.println("Novo Cliente");
				
				String hostAddress = novoSocket.getInetAddress().getHostAddress();				
				listaDeClientes.add(new Cliente(hostAddress, novoSocket.getOutputStream()));
				
				new Thread(new TrataCliente(novoSocket.getInputStream())).start();
			}			
		} catch (Exception ex) {
			ex.printStackTrace();
		}
	}
	
	public static void main(String[] args) {
		new ChatServidor(9999);
	}

}