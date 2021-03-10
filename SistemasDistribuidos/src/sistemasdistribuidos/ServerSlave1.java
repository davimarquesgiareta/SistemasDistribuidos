/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package sockets.thread;



import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;

public class ServerSlave1 {
    public static void main(String[] args) throws IOException {
        //1 - Definir o serverSocket (abrir porta de conexão)
     
        ServerSocket socketSlave1 = new ServerSocket(54323);
      
        System.out.println("A porta 54322 foi aberta!");
        System.out.println("Servidor esperando receber mensagens de clientes...");
        while (true) {
            //2 - Aguardar solicitações de conexão de clientes 
           
            Socket slave1 = socketSlave1.accept();
          
            
            //Mostrar endereço IP do cliente conectado
            System.out.println("Cliente " + slave1.getInetAddress().getHostAddress() + " conectado");
            
            //3 - Definir uma thread para cada cliente conectado
           
            ThreadSlave threadslave = new ThreadSlave(slave1);
          
            
            
            threadslave.start();
            
        }
    }
}
