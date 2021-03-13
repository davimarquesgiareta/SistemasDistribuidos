package sistemasdistribuidos;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.nio.file.Path;
import java.nio.file.Paths;

public class Servidor {
    public static void main(String[] args) throws IOException {
        //1 - Definir o serverSocket (abrir porta de conexão)
        String dir = "C:\\Users\\luizg\\Desktop\\master";
        ServerSocket servidorSocket = new ServerSocket(54322);

        System.out.println("A porta 54322 foi aberta!");
        System.out.println("Servidor esperando receber mensagens de clientes...");

        Path diretorio = Paths.get(dir);
        ClienteThread clientmaster = new ClienteThread(diretorio, "127.0.0.1", 54323, "127.0.0.1", 54324);
        clientmaster.start(); 
        while (true) {
            //2 - Aguardar solicitações de conexão de clientes 
            Socket socket = servidorSocket.accept();
            ThreadSockets thread = new ThreadSockets(socket, dir);
            thread.start();
       }
           
    }
}
