
package sistemasdistribuidos;


import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;

public class ServerSlave2 {
    public static void main(String[] args) throws IOException {
        //1 - Definir o serverSocket (abrir porta de conexão)
        ServerSocket servidorSocket = new ServerSocket(54324);
        System.out.println("A porta 54322 foi aberta!");
        System.out.println("Servidor esperando receber mensagens de clientes...");
        while (true) {
            //2 - Aguardar solicitações de conexão de clientes 
            Socket socket = servidorSocket.accept();
            //Mostrar endereço IP do cliente conectado
            System.out.println("Cliente " + socket.getInetAddress().getHostAddress() + " conectado");
            
            //3 - Definir uma thread para cada cliente conectado
            ThreadSlave2 thread = new ThreadSlave2(socket);
            thread.start();
        }
    }
}