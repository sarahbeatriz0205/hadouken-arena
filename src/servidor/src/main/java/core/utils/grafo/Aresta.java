package core.utils.grafo;

public class Aresta {
    private boolean ehDirecionada;
    private Object valor;
    private Vertice verticeOrigem;
    private Vertice verticeDestino;

    public Aresta(Object valor, Vertice verticeOrigem, Vertice verticeDestino, boolean ehDirecionada) {
        this.valor = valor; 
        this.verticeOrigem = verticeOrigem;
        this.verticeDestino = verticeDestino;
        this.ehDirecionada = ehDirecionada;
    }

    public Vertice getVerticeOrigem() {
        return this.verticeOrigem;
    }

    public Vertice getVerticeDestino() {
        return this.verticeDestino;
    }

    public Object getValor() {
        return this.valor;
    }

    public boolean getFlagDirecionada() {
        return this.ehDirecionada;
    }

    public void substituir(int valor) {
        this.valor = valor;
    }
}
