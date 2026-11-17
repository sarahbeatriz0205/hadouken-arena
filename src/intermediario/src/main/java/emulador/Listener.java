package emulador;
import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.InetAddress;
import java.net.ServerSocket;
import java.net.Socket;
import java.net.URI;
import jakarta.json.Json;
import jakarta.websocket.ClientEndpoint;
import jakarta.websocket.OnClose;
import jakarta.websocket.OnOpen;
import jakarta.websocket.Session;
import org.glassfish.tyrus.client.ClientManager;

@ClientEndpoint 
public class Listener {
    private static Session sessaoWebsocket;

    @OnOpen 
    public void conectar(Session sessao) {
        sessaoWebsocket = sessao;
        System.out.println("Conectado ao Servidor WebSocket!");

        // identificacao pro server nao enviar os dados pro listener de volta
        String identificacao = Json.createObjectBuilder()
                .add("acao", "souListener")
                .build()
                .toString();
        sessao.getAsyncRemote().sendText(identificacao);
        System.out.println("Deu certo");
    }

    @OnClose 
    public void desconectar() {
        sessaoWebsocket = null;
    }

    private static void enviarParaWebSocket(String l) {
        try {
            String[] partes = l.split(";");
            int p1 = Integer.parseInt(partes[0].split("=")[1]);
            int p2 = Integer.parseInt(partes[1].split("=")[1]);
            int timer = Integer.parseInt(partes[2].split("=")[1]);

            String json = Json.createObjectBuilder()
                    .add("acao", "atualizarDados")
                    .add("vidaJogador1", p1)
                    .add("vidaJogador2", p2)
                    .add("timer", timer)
                    .build()
                    .toString();

            sessaoWebsocket.getAsyncRemote().sendText(json);
            } catch (Exception e) {
                System.err.println("Erro ao processar linha do Lua: " + l);
            }
    }

    public static void main(String[] args) throws Exception{
        String urlServidor = "ws://localhost:8080/intermediario/transmissao";
        ClientManager cliente = ClientManager.createClient();
        cliente.connectToServer(Listener.class, new URI(urlServidor));

        InetAddress ip = InetAddress.getByName("127.0.0.1");
        try (ServerSocket server = new ServerSocket(9000, 50, ip)) {
            System.out.println("Escutando BizHawk em " + ip.getHostAddress() + ":9000...");

            while (true) {
                try (Socket s = server.accept();
                     BufferedReader in = new BufferedReader(new InputStreamReader(s.getInputStream()))) {

                    System.out.println("BizHawk conectou: " + s.getRemoteSocketAddress());

                    String linha;
                    while ((linha = in.readLine()) != null) {
                        enviarParaWebSocket(linha);
                    }

                    System.out.println("BizHawk desconectou");
                }
            }
        }
    }
}
