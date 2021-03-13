/*
 * Feito por Davi Marques Giareta e Luiz Gustavo Chinelato Setten
 */
package sistemasdistribuidos;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;

public class ServerSlave2 {
    public static void main(String[] args) throws IOException {
        ServerSocket servidorSocket = new ServerSocket(54324);
        
        System.out.println("A porta 54322 foi aberta!");
        System.out.println("Servidor esperando receber mensagens de clientes...");
        while (true) {
            Socket socket = servidorSocket.accept();
            System.out.println("Cliente " + socket.getInetAddress().getHostAddress() + " conectado");

            ThreadSlave2 thread = new ThreadSlave2(socket, "C:\\Users\\luizg\\Desktop\\backup2");
            thread.start();
        }
    }
}