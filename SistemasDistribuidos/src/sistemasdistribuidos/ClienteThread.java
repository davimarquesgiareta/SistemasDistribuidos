package sistemasdistribuidos;

import java.io.*;
import java.net.Socket;
import static java.nio.file.StandardWatchEventKinds.*;
import java.io.IOException;
import java.nio.file.FileSystems;
import java.nio.file.FileVisitResult;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.SimpleFileVisitor;
import java.nio.file.WatchEvent;
import java.nio.file.WatchKey;
import java.nio.file.WatchService;
import java.nio.file.attribute.BasicFileAttributes;
import java.util.HashMap;
import java.util.Map;
import java.util.logging.Level;
import java.util.logging.Logger;

public class ClienteThread extends Thread {
    private final WatchService watcher;
    private final Map<WatchKey, Path> keys;
    private final Path directory;
    private final String ip1;
    private final int port1;
    private final String ip2; 
    private final int port2;

    ClienteThread(Path dir, String ip1, int port1,String ip2, int port2) throws IOException {
        this.watcher = FileSystems.getDefault().newWatchService();
        this.keys = new HashMap<WatchKey, Path>();
        this.directory = dir;
        this.ip1 = ip1;
        this.port1 = port1;
        this.ip2 = ip2;
        this.port2 = port2;
        walkAndRegisterDirectories(dir);
    }
 
    private void registerDirectory(Path dir) throws IOException {
        WatchKey key = dir.register(watcher, ENTRY_CREATE, ENTRY_DELETE, ENTRY_MODIFY);
        keys.put(key, dir);
    }
 
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
        while (true) {
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

                System.out.format("%s %s\n", event.kind().name(), child);
                System.out.println("Chegou aqui");

                if (kind == ENTRY_CREATE) {
                    try {
                        sendDirectory(child, "criar", ip1, port1, this.directory.toString());
                        sendDirectory(child, "criar", ip2, port2, this.directory.toString());

                        if (Files.isDirectory(child)) {
                            walkAndRegisterDirectories(child);
                            
                        }
                    } catch (IOException x) {
                        // do something useful
                    }
                }
                
                if (kind == ENTRY_DELETE){
                  sendDirectory(child,"deletar", ip1, port1, this.directory.toString());
                  sendDirectory(child,"deletar", ip2, port2, this.directory.toString());

                }
                
                if (kind == ENTRY_MODIFY){
                sendDirectory(child,"modificar", ip1, port1, this.directory.toString());
                sendDirectory(child,"modificar", ip2, port2, this.directory.toString());

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
    
    @Override
    public void run()  {
        System.out.println("TAMO NO CLIENT THREAD");        

//        Path dir = Paths.get("C:\\Users\\luizg\\Desktop\\master");
        try {
            new ClienteThread(this.directory, this.ip1, this.port1, this.ip2, this.port2).processEvents();
            System.out.println("encerrou o thread");
            
            //--------- PARTE DO SOCKET ----------//
            //1 - Abrir conexÃ£o
        } catch (IOException ex) {
            Logger.getLogger(ClienteThread.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
    
    
    
    public static void sendDirectory(Path child , String operation, String ip, int port, String mainDirectory) throws IOException{
                        System.out.println("o child é : "+child);
                        System.out.println("o estado é: "+operation);
                        Socket socket = new Socket(ip, port);
                        
                        String diretorio = child.toString();
//                        System.out.println(child.getParent().toString());
        

                        DataOutputStream saida = new DataOutputStream(socket.getOutputStream());
                        saida.writeUTF(diretorio);
                        
                        DataOutputStream saida2 = new DataOutputStream(socket.getOutputStream());
                        saida2.writeUTF(operation);
                        
                        DataOutputStream saida3 = new DataOutputStream(socket.getOutputStream());
                        saida3.writeUTF(mainDirectory);

                        DataInputStream entrada = new DataInputStream(socket.getInputStream());
                        String novaMensagem = entrada.readUTF();
                        System.out.println(novaMensagem);
                        
                        DataInputStream entrada2 = new DataInputStream(socket.getInputStream());
                        String novaMensagem2 = entrada2.readUTF();
                        System.out.println(novaMensagem2);
                        
                        //4 - Fechar streams de entrada e saÃ­da de dados
                        entrada.close();
                        entrada2.close();
                        saida.close();
                        saida2.close();
                        
                        //5 - Fechar o socket
                        socket.close();
    }
}