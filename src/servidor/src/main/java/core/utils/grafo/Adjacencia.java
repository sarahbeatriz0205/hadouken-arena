package core.utils.grafo;

public class Adjacencia {
    // par vértice-aresta
    private Vertice vertice;
    private Aresta aresta;

    public Adjacencia(Vertice vertice, Aresta aresta) {
        this.vertice = vertice;
        this.aresta = aresta;
    }

    public Vertice getVertice() {
        return this.vertice;
    }

    public Aresta getAresta() {
        return this.aresta;
    }
}