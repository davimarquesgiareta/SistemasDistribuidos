/*
 * Feito por Davi Marques Giareta e Luiz Gustavo Chinelato Setten
 */
package sistemasdistribuidos;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;

public class ServerSlave1 {
    public static void main(String[] args) throws IOException { 
        ServerSocket socketSlave1 = new ServerSocket(54323);
      
        System.out.println("A porta 54323 foi aberta!");
        System.out.println("Servidor esperando receber mensagens de clientes...");
        while (true) {
            Socket slave1 = socketSlave1.accept();

            System.out.println("Cliente " + slave1.getInetAddress().getHostAddress() + " conectado");
            
            ThreadSlave threadslave = new ThreadSlave(slave1, "C:\\Users\\luizg\\Desktop\\backup1");
            threadslave.start();
        }
    }
}
