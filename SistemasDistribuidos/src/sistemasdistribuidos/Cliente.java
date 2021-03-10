package sistemasdistribuidos;

/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */


import java.io.*;
import java.net.Socket;
import java.nio.file.Path;
import java.nio.file.Paths;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import static java.nio.file.StandardWatchEventKinds.*;
 
import java.io.IOException;
import java.nio.file.FileSystems;
import java.nio.file.FileVisitResult;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.SimpleFileVisitor;
import java.nio.file.WatchEvent;
import java.nio.file.WatchKey;
import java.nio.file.WatchService;
import java.nio.file.attribute.BasicFileAttributes;
import java.util.HashMap;
import java.util.Map;

public class Cliente {
    private final WatchService watcher;
    private final Map<WatchKey, Path> keys;

    
 
    /**
     * Creates a WatchService and registers the given directory
     */
    Cliente(Path dir) throws IOException {
        this.watcher = FileSystems.getDefault().newWatchService();
        this.keys = new HashMap<WatchKey, Path>();
 
        walkAndRegisterDirectories(dir);
    }
 
    /**
     * Register the given directory with the WatchService; This function will be called by FileVisitor
     */
    private void registerDirectory(Path dir) throws IOException 
    {
        WatchKey key = dir.register(watcher, ENTRY_CREATE, ENTRY_DELETE, ENTRY_MODIFY);
        keys.put(key, dir);
    }
 
    /**
     * Register the given directory, and all its sub-directories, with the WatchService.
     */
    private void walkAndRegisterDirectories(final Path start) throws IOException {
        // register directory and sub-directories
        Files.walkFileTree(start, new SimpleFileVisitor<Path>() {
            @Override
            public FileVisitResult preVisitDirectory(Path dir, BasicFileAttributes attrs) throws IOException {
                registerDirectory(dir);
                return FileVisitResult.CONTINUE;
            }
        });
    }
    
    void processEvents() throws IOException {
        for (;;) {
 
            // wait for key to be signalled
            WatchKey key;
            try {
                key = watcher.take();
            } catch (InterruptedException x) {
                return;
            }
 
            Path dir = keys.get(key);
            if (dir == null) {
                System.err.println("WatchKey not recognized!!");
                continue;
            }
 
            for (WatchEvent<?> event : key.pollEvents()) {
                @SuppressWarnings("rawtypes")
                WatchEvent.Kind kind = event.kind();
 
                // Context for directory entry event is the file name of entry
                @SuppressWarnings("unchecked")
                Path name = ((WatchEvent<Path>)event).context();
                Path child = dir.resolve(name);
 
                // print out event
                //event.kind().name() Ã© a aÃ§Ã£o - CRIAR, DELETE, ATUALIZAR
                //child Ã© o diretÃ³rio/arquivo 
                System.out.format("%s %s\n", event.kind().name(), child);
                
                // if directory is created, and watching recursively, then register it and its sub-directories
                if (kind == ENTRY_CREATE) {
                    try {
                        mandarDiretorio(child, "criar");
                        if (Files.isDirectory(child)) {
                            walkAndRegisterDirectories(child);
                            
                        }
                    } catch (IOException x) {
                        // do something useful
                    }
                }
                
                if (kind == ENTRY_DELETE){
                  mandarDiretorio(child,"deletar");
                }
                
                if (kind == ENTRY_MODIFY){
                mandarDiretorio(child,"modificar");
                }
            }
                
 
            // reset key and remove from set if directory no longer accessible
            boolean valid = key.reset();
            if (!valid) {
                keys.remove(key);
                
                // all directories are inaccessible
                if (keys.isEmpty()) {
                   
                    break;
                }
            }
        }
    }
    
    
    public static void main(String[] args) throws IOException {
        
        Path dir = Paths.get("C:\\Users\\luizg\\Desktop\\pastatestes");
        new Cliente(dir).processEvents();


        //--------- PARTE DO SOCKET ----------//
       //1 - Abrir conexÃ£o
        
        
    }
    
    
    
    public static void mandarDiretorio(Path child , String estado) throws IOException{
                        System.out.println("o child é : "+child);
                        System.out.println("o estado é: "+estado);
                        Socket socket = new Socket("127.0.0.1", 54322);
                        
                        String diretorio = child.toString();
        
                        //2 - Definir stream de saÃ­da de dados do cliente
                        
                        //************* SAIDAAAASSS ************************
                        DataOutputStream saida = new DataOutputStream(socket.getOutputStream());
                        saida.writeUTF(diretorio); //Enviar  mensagem em minÃºsculo para o servidor
                        
                        DataOutputStream saida2 = new DataOutputStream(socket.getOutputStream());
                        saida2.writeUTF(estado); //Enviar  mensagem em minÃºsculo para o servidor
                        // *************************************************

                        //3 - Definir stream de entrada de dados no cliente
                        //*********** O QUE TA VINDO DE VOLTA DO SERVIDOR ********************
                        DataInputStream entrada = new DataInputStream(socket.getInputStream());
                        String novaMensagem = entrada.readUTF();//Receber mensagem em maiÃºsculo do servidor
                        System.out.println(novaMensagem); //Mostrar mensagem em maiÃºsculo no cliente
                        
                        DataInputStream entrada2 = new DataInputStream(socket.getInputStream());
                        String novaMensagem2 = entrada2.readUTF();//Receber mensagem em maiÃºsculo do servidor
                        System.out.println(novaMensagem2); //Mostrar mensagem em maiÃºsculo no cliente
                        //***********************************************************************
                        
                        
                        //4 - Fechar streams de entrada e saÃ­da de dados
                        entrada.close();
                        entrada2.close();
                        saida.close();
                        saida2.close();
                        
                        //5 - Fechar o socket
                        socket.close();
                         //--------- FIM PARTE DO SOCKET ----------//
    }
}

