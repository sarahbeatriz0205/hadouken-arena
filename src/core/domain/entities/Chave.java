package core.domain.entities;
import java.util.ArrayList;
import core.utils.grafo.Grafo;

public class Chave {
    private Grafo chaveamento;

    public Chave() {

    }

    public Grafo criarChave(ArrayList<Jogador> jogadores, int qtdJogadores) throws Exception{
        Grafo chaveamento = new Grafo();
        if (jogadores.size() < qtdJogadores) {
            throw new Exception(); // se a qtd for menor que os jogadores inscritos, chave nao será gerada
        }
        // resto da lógica...
        return chaveamento;
    }
}
