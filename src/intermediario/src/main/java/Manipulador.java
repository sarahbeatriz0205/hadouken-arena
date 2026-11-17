import java.io.IOException;
import core.DadosPartida;
import jakarta.json.JsonObject;
import jakarta.json.spi.JsonProvider;
import jakarta.websocket.Session;
import java.util.LinkedList;

public class Manipulador {
    private final LinkedList<Session> sessoes = new LinkedList<Session>();

    public void adicionarCliente(Session sessao) {
        sessoes.add(sessao);
        System.out.println("Entrou mais um! Id do cliente: " + sessao.getId());
    }

    public void removerCliente(Session sessao) {
        sessoes.remove(sessao);
        System.out.println("Cliente removido");
    }

    public void enviarDadosPartida(DadosPartida dados) {
        JsonProvider jp  = JsonProvider.provider();
        JsonObject jo = jp.createObjectBuilder()
            .add("acao", "dadosAtualizados")
            .add("vidaJogador1", dados.getVidaJogador1())
            .add("vidaJogador2", dados.getVidaJogador2())
            .add("timer", dados.getTimer())
            .build();

            for (int i=0; i < sessoes.size(); i++) {
                try {
                    sessoes.get(i).getBasicRemote().sendText(jo.toString());
                } catch (IOException ex) {
                    sessoes.remove(sessoes.get(i));
                    ex.printStackTrace();
            }        
        }
    }
}
