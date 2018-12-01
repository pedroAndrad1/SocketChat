import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;
import java.net.UnknownHostException;
import java.util.Scanner;

public class Cliente {
	public static void main(String[] args) {
		Scanner sc = new Scanner(System.in);
		
		
		System.out.println("Bem-vindo!");
		String IP = "";
		String porta = "";
		System.out.println("Informe o enderço IP do servidor");
		IP = sc.nextLine();
		System.out.println("Informe a porta do servidor");
		porta = sc.nextLine();
		
		try {
			
			final Socket cliente = new Socket(IP, Integer.parseInt(porta));
			
			//Abri essa Thread para o cliente poder receber mensagens e enviar mensagens ao mesmo tempo.
			new Thread(){
				
				//Lendo mensagens vindas do servidor
				public void run() {
					
					try {
						
						BufferedReader leitor = new BufferedReader(new InputStreamReader(cliente.getInputStream()));
						
						while(true) {
							
							String mensagem = leitor.readLine();
							System.out.println(mensagem);
						
						}
					} catch (IOException e) {
						e.printStackTrace();
					}
				
				}
			
			}.start();
			
			//Escreve mensagens para o servidor.
			PrintWriter escritor = new PrintWriter(cliente.getOutputStream(), true);
			
			//Escreve uma mensagem para o servidor.
			while(true) {
				
				String mensagemTerminal = sc.nextLine();
				
				escritor.println(mensagemTerminal);//Manda a mensagem para o servidor.
				
				if(mensagemTerminal.equalsIgnoreCase("/sair")) {
					System.exit(0); //Fecha a conexão com o servidor. 
				}
			
			}
			
			
		} catch (UnknownHostException e) {
			System.err.println("Endereço informado inválido");
		} catch(IOException e1) {
			System.err.println("O servidor pode estar fora do ar :(");
			
		}
		
	}
	

}
