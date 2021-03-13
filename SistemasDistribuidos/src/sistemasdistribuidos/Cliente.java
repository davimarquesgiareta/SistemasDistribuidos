/*
 * Feito por Davi Marques Giareta e Luiz Gustavo Chinelato Setten
 */

package sistemasdistribuidos;

import java.io.*;
import java.net.Socket;
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
    private final Path directory;
    private final String ip;
    private final int port;

    Cliente(Path dir, String ip, int port) throws IOException {
        this.watcher = FileSystems.getDefault().newWatchService();
        this.keys = new HashMap<WatchKey, Path>();
        this.directory = dir;
        this.ip = ip;
        this.port = port;

        walkAndRegisterDirectories(dir);
    }
 
    private void registerDirectory(Path dir) throws IOException 
    {
        WatchKey key = dir.register(watcher, ENTRY_CREATE, ENTRY_DELETE, ENTRY_MODIFY);
        keys.put(key, dir);
    }

    private void walkAndRegisterDirectories(final Path start) throws IOException {
        Files.walkFileTree(start, new SimpleFileVisitor<Path>() {
            @Override
            public FileVisitResult preVisitDirectory(Path dir, BasicFileAttributes attrs) throws IOException {
                registerDirectory(dir);
                return FileVisitResult.CONTINUE;
            }
        });
    }
    
    void processEvents() throws IOException {
        while (true) {
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
 
                @SuppressWarnings("unchecked")
                Path name = ((WatchEvent<Path>)event).context();
                Path child = dir.resolve(name);
 
                
                //event.kind().name() Ã© a aÃ§Ã£o - CRIAR, DELETE, ATUALIZAR
                //child Ã© o diretÃ³rio/arquivo 
                System.out.format("%s %s\n", event.kind().name(), child);
                
                if (kind == ENTRY_CREATE) {
                    try {
                        mandarDiretorio(child, "criar", this.directory.toString(), this.ip, this.port);
                        if (Files.isDirectory(child)) {
                            walkAndRegisterDirectories(child);
                        }
                    } catch (IOException x) {
                        // Só para suprimir o erro
                    }
                }
                
                if (kind == ENTRY_DELETE){
                  mandarDiretorio(child,"deletar", this.directory.toString(), this.ip, this.port);
                }
                
                if (kind == ENTRY_MODIFY){
                mandarDiretorio(child,"modificar", this.directory.toString(), this.ip, this.port);
                }
            }
                
            boolean valid = key.reset();
            if (!valid) {
                keys.remove(key);

                if (keys.isEmpty()) {
                    break;
                }
            }
        }
    }
     
    public static void main(String[] args) throws IOException {       
        Path dir = Paths.get("C:\\Users\\luizg\\Desktop\\pastatestes");
        new Cliente(dir, "127.0.0.1", 54322).processEvents();
    }

    public static void mandarDiretorio(Path child , String estado, String mainDirectory, String ip, int port) throws IOException{
                        System.out.println("o child é : "+child);
                        System.out.println("o estado é: "+estado);
                        Socket socket = new Socket(ip, port);
                        
                        String diretorio = child.toString();
        
                        //2 - Definir stream de saÃ­da de dados do cliente
                        
                        //************* SAIDAAAASSS ************************
                        DataOutputStream saida = new DataOutputStream(socket.getOutputStream());
                        saida.writeUTF(diretorio); //Enviar  mensagem em minÃºsculo para o servidor
                        
                        DataOutputStream saida2 = new DataOutputStream(socket.getOutputStream());
                        saida2.writeUTF(estado); //Enviar  mensagem em minÃºsculo para o servidor
                        
                        DataOutputStream saida3 = new DataOutputStream(socket.getOutputStream());
                        saida3.writeUTF(mainDirectory);
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

