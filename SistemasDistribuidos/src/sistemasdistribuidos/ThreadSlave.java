/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package sockets.thread;


import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.net.Socket;
import java.nio.file.Path;

public class ThreadSlave extends Thread {
    private Socket socket;
    public ThreadSlave(Socket s) {
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
                //String novaMensagem = mensagem.toUpperCase(); //Convertendo em Maiúsculo
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
            
            
            String baseCaminhoBackup1 = "C:\\Users\\davim\\Desktop\\backup1";
            //String baseCaminhoBackup2 = "C:\\Users\\davim\\Desktop\\backup2";
            String caminhoCompleto1 = baseCaminhoBackup1.concat(caminhosplit[1]);
           // String caminhoCompleto2 = baseCaminhoBackup2.concat(caminhosplit[1]);
            
            System.out.println("Caminho 1" + caminhoCompleto1);
           // System.out.println("Caminho2 " +caminhoCompleto2);
            
            String outFileName = caminhoCompleto1;
           // String outFileName2 = caminhoCompleto2;

            FileInputStream in = new FileInputStream(inFileName);
            FileOutputStream out = new FileOutputStream(outFileName);
            //FileOutputStream out2 = new FileOutputStream(outFileName2);
            
            
            byte[] buf = new byte[1024];
            int len;

            while ((len = in.read(buf)) > 0) {
                out.write(buf, 0, len);
                //out2.write(buf, 0, len);
            }

            out.close();
           // out2.close();
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
             
            String baseCaminhoBackup1 = "C:\\Users\\davim\\Desktop\\backup1";
          //  String baseCaminhoBackup2 = "C:\\Users\\davim\\Desktop\\backup2";
            String caminhoCompleto1 = baseCaminhoBackup1.concat(caminhosplit[1]);
           // String caminhoCompleto2 = baseCaminhoBackup2.concat(caminhosplit[1]);
            
            System.out.println("1 - " + caminhoCompleto1);
           //  System.out.println("2 - " + caminhoCompleto2);
        
        File f1 = new File(caminhoCompleto1);  
       // File f2 = new File(caminhoCompleto2);  
        f1.delete();
        //f2.delete();
    }
  
    
    
    
    
}
