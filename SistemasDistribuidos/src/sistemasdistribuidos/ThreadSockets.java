package sistemasdistribuidos;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.net.Socket;
import java.nio.file.Path;




public class ThreadSockets extends Thread {
    private Socket socket;
    public ThreadSockets(Socket s) {
        this.socket = s;
    }

    public void run() {
        System.out.println(Thread.currentThread().getName());//Imprimir o nome da Thread
        try {
            DataInputStream entrada2;
            DataOutputStream saida;
            
            DataOutputStream saida2;
            try ( //1 - Definir stream de entrada de dados no servidor
            //*************** O QUE TA VINDO DO CLIENTE ************
                    DataInputStream entrada = new DataInputStream(socket.getInputStream())) {
                String mensagem = entrada.readUTF();//Recebendo mensagem em Minúsculo do Cliente
                
                entrada2 = new DataInputStream(socket.getInputStream());
                String mensagem2 = entrada2.readUTF();//Recebendo mensagem em Minúsculo do Cliente
                // String novaMensagem2 = mensagem2.toUpperCase(); //Convertendo em Maiúsculo
                //*****************************************************
                
                if ("criar".equals(mensagem2)){
                    copiarArquivo(mensagem);
                     }  
                
                
                if ("modificar".equals(mensagem2)){
                    copiarArquivo(mensagem);
                     } 
             
                
                if ("deletar".equals(mensagem2)){
                    deletarArquivo(mensagem);
                     }   
                
               


//2 - Definir stream de saída de dados do servidor
                //************** VOLTANDO PARA O CLIENTE ************************
                saida = new DataOutputStream(socket.getOutputStream());
                saida.writeUTF(mensagem); //Enviando mensagem em Maiúsculo para Cliente
                saida2 = new DataOutputStream(socket.getOutputStream());
                saida2.writeUTF(mensagem2); //Enviando mensagem em Maiúsculo para Cliente
                //***************************************************************
                
                
                
                //3 - Fechar streams de entrada e saída de dados
            } //Recebendo mensagem em Minúsculo do Cliente
            //String novaMensagem = mensagem.toUpperCase(); //Convertendo em Maiúsculo
            entrada2.close();
            saida.close();
            saida2.close();
            
            //4 - Fechar socket de comunicação
            socket.close();
        } catch (IOException ioe) {
            System.out.println("Erro: " + ioe.toString());
        }
        
        
        
    }
    
    
      public static void copiarArquivo(String mensagem) {
              String[] caminhosplit = new String[6];
              String caminho = mensagem.toString();
             caminhosplit = caminho.split("pastatestes");
                
             
          
        try {
            System.out.println("o famoso child é "+ mensagem);
            String inFileName = caminho;
            
            
            String baseCaminhoBackup1 = "C:\\Users\\luizg\\Desktop\\master";
            
            String caminhoCompleto1 = baseCaminhoBackup1.concat(caminhosplit[1]);
          
            
            System.out.println("Caminho 1" + caminhoCompleto1);
         
            String outFileName = caminhoCompleto1;
          

            FileInputStream in = new FileInputStream(inFileName);
            FileOutputStream out = new FileOutputStream(outFileName);
           
            
            
            byte[] buf = new byte[1024];
            int len;

            while ((len = in.read(buf)) > 0) {
                out.write(buf, 0, len);
               
            }

            out.close();
          
            in.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
      
      
      public static void deletarArquivo (String mensagem){
        System.out.println("ENTROU NA DELETANCIA");
        System.out.println(" E O CHILD? : "+mensagem);
         String[] caminhosplit = new String[6];
         String caminho = mensagem.toString();
         caminhosplit = caminho.split("pastatestes");
                
             System.out.println("Caminho Split");
             for (int i = 0; i < caminhosplit.length; i++) {
                 System.out.println("->  "+caminhosplit[i]);
        }
             
            String baseCaminhoBackup1 = "C:\\Users\\luizg\\Desktop\\master";
         
            String caminhoCompleto1 = baseCaminhoBackup1.concat(caminhosplit[1]);
         
            
            System.out.println("1 - " + caminhoCompleto1);
        
        
        File f1 = new File(caminhoCompleto1);  
      
        f1.delete();
        
    }
  
    
    
    
    
}
