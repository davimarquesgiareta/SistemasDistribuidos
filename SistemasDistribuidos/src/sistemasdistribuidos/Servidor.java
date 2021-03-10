package sockets.thread;

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
     //   ServerSocket socketSlave1 = new ServerSocket(54323);
      //  ServerSocket socketSlave2 = new ServerSocket(54324);
        System.out.println("A porta 54322 foi aberta!");
        System.out.println("Servidor esperando receber mensagens de clientes...");
        
      
        
        while (true) {
            //2 - Aguardar solicitações de conexão de clientes 
            Socket socket = servidorSocket.accept();
            ThreadSockets thread = new ThreadSockets(socket);
            thread.start();
            
              Path diretoriox = Paths.get("C:\\Users\\davim\\Desktop\\master");
            ClienteThread clientmaster = new ClienteThread(diretoriox);
            clientmaster.start(); 
            
             
            
            /*
            Socket slave1 = socketSlave1.accept();
            ThreadSlave threadslave = new ThreadSlave(slave1);
            threadslave.start();
            
            
            Socket slave2 = socketSlave2.accept();
            ThreadSlave2 threadslave2 = new ThreadSlave2(slave2);
             threadslave2.start();
            */
            
            //Mostrar endereço IP do cliente conectado
           // System.out.println("Cliente " + socket.getInetAddress().getHostAddress() + " conectado");
            
            //3 - Definir uma thread para cada cliente conectado
            
            
            
            
            
            
           
       }
           
    }
}
