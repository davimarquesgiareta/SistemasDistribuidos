/*
 * Feito por Davi Marques Giareta e Luiz Gustavo Chinelato Setten
 */

package sistemasdistribuidos;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.net.Socket;

public class ThreadSlave2 extends Thread {
    private final Socket socket;
    protected String path;
    public ThreadSlave2(Socket s, String path) {
        this.socket = s;
        this.path = path;
    }

    @Override
    public void run() {
        System.out.println(Thread.currentThread().getName());//Imprimir o nome da Thread
        try {
            DataInputStream entrada2;
            DataInputStream entrada3;
            
            DataOutputStream saida;
            
            DataOutputStream saida2;
            try (DataInputStream entrada = new DataInputStream(socket.getInputStream())) {
                String mensagem = entrada.readUTF();

                entrada2 = new DataInputStream(socket.getInputStream());
                String mensagem2 = entrada2.readUTF();

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
        String caminhosplit = mensagem.replace(mainPath, "");
                
        try {
            System.out.println("o famoso child é "+ mensagem);
            String inFileName = mensagem;
            String baseCaminhoBackup2 = localPath;
            String caminhoCompleto2 = baseCaminhoBackup2.concat(caminhosplit);

            String outFileName2 = caminhoCompleto2;

            try (FileInputStream in = new FileInputStream(inFileName); FileOutputStream out2 = new FileOutputStream(outFileName2)) {
                byte[] buf = new byte[1024];
                int len;
                
                while ((len = in.read(buf)) > 0) {
                    out2.write(buf, 0, len);
                }
                
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
      
      
      public static void deletarArquivo (String mensagem, String mainPath, String localPath){
        String caminhosplit = mensagem.replace(mainPath, "");

        String baseCaminhoBackup2 = localPath;

        String caminhoCompleto2 = baseCaminhoBackup2.concat(caminhosplit);

        File f2 = new File(caminhoCompleto2);  

        f2.delete();
    }
}
