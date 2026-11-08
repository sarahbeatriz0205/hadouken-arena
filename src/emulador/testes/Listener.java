package src.emulador.testes;
import java.io.*;
import java.net.*;

public class Listener {
    public static void main(String[] args) throws Exception {
        // Define explicitamente o IP local em formato IPv4
        InetAddress ip = InetAddress.getByName("127.0.0.1");
        
        // Parâmetros: Porta (9000), Fila de conexões (50), Endereço IP (127.0.0.1)
        try (ServerSocket server = new ServerSocket(9000, 50, ip)) {
            System.out.println("Escutando no IP " + ip.getHostAddress() + " na porta 9000...");
            
            while (true) {
                try (Socket s = server.accept();
                     BufferedReader in = new BufferedReader(new InputStreamReader(s.getInputStream()))) {
                    
                    System.out.println("Conectou: " + s.getRemoteSocketAddress());
                    
                    String linha;
                    // Lê a mensagem inteira até encontrar o \n enviado pelo Lua
                    while ((linha = in.readLine()) != null) {
                        System.out.println(linha);
                    }
                    
                    System.out.println("Desconectou");
                }
            }
        }
    }
}