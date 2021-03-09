package sistemasdistribuidos;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;

public class Servidor {
    public static void main(String[] args) throws IOException {
        //1 - Definir o serverSocket (abrir porta de conexÃ£o)
        ServerSocket serverSocket = new ServerSocket(54323);
        System.out.println("A porta 54321 foi aberta!");
        System.out.println("Servidor esperando receber mensagem de cliente...");
        //2 - Aguardar solicitaÃ§Ã£o de conexÃ£o de cliente
        Socket socket = serverSocket.accept();
        //Mostrar endereÃ§o IP do cliente conectado
        System.out.println("Cliente " + socket.getInetAddress().getHostAddress() + " conectado");

        //3 - Definir stream de entrada de dados no servidor
        DataInputStream entrada = new DataInputStream(socket.getInputStream());
        String mensagem = entrada.readUTF();//receber mensagem em minÃºsculo do cliente
        String novaMensagem = mensagem.toUpperCase(); //converter mensagem em maiÃºsculo

        //4 - Definir stream de saÃ­da de dados do servidor
        DataOutputStream saida = new DataOutputStream(socket.getOutputStream());
        saida.writeUTF(novaMensagem); //Enviar mensagem em maiÃºsculo para cliente

        //5 - Fechar streams de entrada e saÃ­da de dados
        entrada.close();
        saida.close();

        //6 - Fechar sockets de comunicaÃ§Ã£o e conexÃ£o
        socket.close();
        serverSocket.close();
    }
}
