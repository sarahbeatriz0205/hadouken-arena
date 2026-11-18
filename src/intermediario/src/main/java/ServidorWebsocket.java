import java.io.StringReader;
import java.util.Scanner;
import jakarta.websocket.*;
import jakarta.websocket.server.ServerEndpoint;
import jakarta.json.Json;
import jakarta.json.JsonObject;
import jakarta.json.JsonReader;
import java.util.logging.Level;
import java.util.logging.Logger;
import org.glassfish.tyrus.server.Server;
import core.DadosPartida;

@ServerEndpoint("/transmissao")
public class ServidorWebsocket {
        private static final Manipulador m = new Manipulador();

        @OnOpen 
        public void abrir(Session sessao) { 
            m.adicionarCliente(sessao); 
        }

        @OnClose 
        public void fechar(Session sessao) {
            m.removerCliente(sessao);
        }

        @OnError 
        public void erro(Throwable t){
            Logger.getLogger(ServidorWebsocket.class.getName()).log(Level.SEVERE, null, t);
        }

        @OnMessage 
        public void mensagem(String msgJson, Session sessao) {
            try (JsonReader jr = Json.createReader(new StringReader(msgJson))) {
            JsonObject jo = jr.readObject();
            String acao = jo.getString("acao", "");
            if ("souListener".equals(acao)) {
                m.removerCliente(sessao);
            }
            else if ("atualizarDados".equals(acao)) {
                DadosPartida dadosAtual = new DadosPartida(jo.getInt("vidaJogador1"), jo.getInt("vidaJogador2"), jo.getInt("timer"));
                m.enviarDadosPartida(dadosAtual); 
                System.out.println("P1: " + dadosAtual.getVidaJogador1() + " | P2: " + dadosAtual.getVidaJogador2());
            }
        }
    }

    public static void main(String[] args) throws Exception{
        Server server = new Server("localhost", 8080, "/intermediario", null, ServidorWebsocket.class);
        server.start();
        System.out.println("Servidor WebSocket rodando em: ws://localhost:8025/intermediario/transmissao");
        System.out.println("Pressione ENTER para encerrar...");
        new Scanner(System.in).nextLine();
        server.stop();
    }
}
