import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;
import java.util.ArrayList;
import java.util.Scanner;

public class GerenciadorDeClientes extends Thread {
		
		private Socket cliente;
		private String nomeCliente;
		private static ArrayList<GerenciadorDeClientes> clientes = new ArrayList<GerenciadorDeClientes>();
		private PrintWriter escritor;
		
		public GerenciadorDeClientes(Socket cliente) {
			this.cliente = cliente;
			clientes.add(this);//Add o cliente a lista de clientes
			
			start();//Inicia a Thread e vai pro método run().
		}
		
		@Override
		public void run() {
				try {
					//Lê as mensagens vindas do cliente.
					BufferedReader leitor = new BufferedReader(new InputStreamReader(cliente.getInputStream()));
					//Envia mensagens para o cliente.
					 escritor = new PrintWriter(cliente.getOutputStream(),true);
					
					escritor.println("Qual o seu nome?");
				
					String msg = leitor.readLine();
					
					this.nomeCliente = msg.replaceAll(",","");
					
					escritor.println("Bem vindo ao servidor, " + this.nomeCliente);
					escritor.println("Comando para sair do servidor - /sair");
					escritor.println("Comando para listar os membros do chat - /membros");
					
					//Lopping para ler as mensagens vindas do cliente.
					while(true) {
						msg = leitor.readLine();
						
						//If para resolver um bug do cliente receber varíos nulls quando saísse do servidor e para checar se 
						//a mensagem não é um comendo. O que, no caso, não deve ser enviada para os clientes.
						if( (msg != null || msg.isEmpty() == false) && (msg.equalsIgnoreCase("/sair") == false) &&
						   (msg.equalsIgnoreCase("/membros") == false) ) {
							
							GerenciadorDeClientes atual = this;
							//Envia a mensagem para todos os clientes no servidor
							for(GerenciadorDeClientes c: clientes)
									c.escritor.println(atual.nomeCliente + ": " + msg);
			
						}else {
							//Fecha a conexão caso receba o comando /sair
							if(msg.equalsIgnoreCase("/sair")) {
								this.cliente.close();
							
							}else if(msg.equalsIgnoreCase("/membros")){
								//Lista todos os clientes do servidor
								StringBuilder listaMembros = new StringBuilder();
								listaMembros.append("Membros do chat: ")		;
								
								for(GerenciadorDeClientes c: clientes) {
									listaMembros.append(c.nomeCliente);
									listaMembros.append(",");
								}
								listaMembros.delete(listaMembros.length() - 1, listaMembros.length()); //tira a ultima "," da lista
								
								escritor.println(listaMembros.toString());
							}
						}
						
					}
					
				} catch (IOException e) {
					System.err.println("Um cliente fechou a conexão");
				}
			
		}
}
