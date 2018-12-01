import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;

/**
 * ServidoSocket tem a função de receber todas as solicitações de conexão e passar os clientes para a classe GerenciadorDeClientes.
 * 
 * @author pedro
 *
 */
public class ServidorSocket {

	public static void main(String[] args) {
		
		ServerSocket servidor = null;
		
		try {
			
			System.out.println("Abrindo servidor");
			servidor = new ServerSocket(9999);
			System.out.println("Servidor aberto");
			
			//Lopping para pegar as solicitações de conexão.
			while(true) {
				Socket cliente = servidor.accept(); 
				new GerenciadorDeClientes(cliente);
			}
			
		} catch (IOException e) {
			
			try {
				//Só para garantir o fechamento do servidor.
				if(servidor != null)
					servidor.close();
				
			} catch (IOException e1) {}
			
			System.err.println("A porta está ocupada ou o servidor foi fechado");
		}
	}

}
