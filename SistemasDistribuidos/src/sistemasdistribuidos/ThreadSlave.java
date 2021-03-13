/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package sistemasdistribuidos;


import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.net.Socket;

public class ThreadSlave extends Thread {
    private Socket socket;
    protected String path;
    public ThreadSlave(Socket s, String path) {
        this.socket = s;
        this.path = path;
    }
    

    public void run() {
        System.out.println(Thread.currentThread().getName());//Imprimir o nome da Thread
        try {
            DataInputStream entrada2;            
            DataInputStream entrada3;

            DataOutputStream saida;
            
            DataOutputStream saida2;
            try (
                DataInputStream entrada = new DataInputStream(socket.getInputStream())) {
                String mensagem = entrada.readUTF();

                //Tipo de operação
                entrada2 = new DataInputStream(socket.getInputStream());
                String mensagem2 = entrada2.readUTF();
                
                //Caminho base do cliente
                entrada3 = new DataInputStream(socket.getInputStream());
                String mensagem3 = entrada3.readUTF();

                if ("criar".equals(mensagem2)){
                    copiarArquivo(mensagem, mensagem3, this.path);
                     }  
                
                if ("modificar".equals(mensagem2)){
                    copiarArquivo(mensagem, mensagem3, this.path);
                     } 
             
                
                if ("deletar".equals(mensagem2)){
                    deletarArquivo(mensagem, mensagem3, this.path);
                     }   

                saida = new DataOutputStream(socket.getOutputStream());
                saida.writeUTF(mensagem);
                saida2 = new DataOutputStream(socket.getOutputStream());
                saida2.writeUTF(mensagem2);
            }

            entrada2.close();
            saida.close();
            saida2.close();

            socket.close();
        } catch (IOException ioe) {
            System.out.println("Erro: " + ioe.toString());
        }
 
    }
    
    
      public static void copiarArquivo(String mensagem, String mainPath, String localPath) {
        String caminhosplit = mensagem.toString().replace(mainPath, "");

        try {
            System.out.println("o famoso child é "+ mensagem);
            String inFileName = mensagem.toString();

            String baseCaminhoBackup1 = localPath;

            String caminhoCompleto1 = baseCaminhoBackup1.concat(caminhosplit);

            String outFileName = caminhoCompleto1;

            try (FileInputStream in = new FileInputStream(inFileName); FileOutputStream out = new FileOutputStream(outFileName)) {
                
                byte[] buf = new byte[1024];
                int len;
                
                while ((len = in.read(buf)) > 0) {
                    out.write(buf, 0, len);
                }
                
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
      
      
      public static void deletarArquivo (String mensagem, String mainPath, String localPath){
        String caminhosplit2 = mensagem.toString().replace(mainPath, "");
        String baseCaminhoBackup1 = localPath;
        String caminhoCompleto1 = baseCaminhoBackup1.concat(caminhosplit2);
        
        File f1 = new File(caminhoCompleto1);  
        f1.delete();
    }
    
}
