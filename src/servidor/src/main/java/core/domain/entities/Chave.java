package core.domain.entities;
import core.utils.grafo.Grafo;

public class Chave {
    private Grafo chaveamento;

    public Chave() {
        this.chaveamento = new Grafo();
    }

    public Grafo getChaveamento() {
        return this.chaveamento;
    }
}
