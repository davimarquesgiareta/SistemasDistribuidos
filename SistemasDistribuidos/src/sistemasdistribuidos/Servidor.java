package sistemasdistribuidos;

import java.io.DataOutputStream;
import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.nio.file.Path;
import java.nio.file.Paths;

public class Servidor {
    public static void main(String[] args) throws IOException {
        //1 - Definir o serverSocket (abrir porta de conexão)
        ServerSocket servidorSocket = new ServerSocket(54322);

        System.out.println("A porta 54322 foi aberta!");
        System.out.println("Servidor esperando receber mensagens de clientes...");

        while (true) {
            //2 - Aguardar solicitações de conexão de clientes 
            Socket socket = servidorSocket.accept();
            ThreadSockets thread = new ThreadSockets(socket);
            thread.start();
            
            Path diretoriox = Paths.get("C:\\Users\\luizg\\Desktop\\master");
            ClienteThread clientmaster = new ClienteThread(diretoriox);
            clientmaster.start(); 
       }
           
    }
}
