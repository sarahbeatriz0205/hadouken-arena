package core.domain.entities;
import core.domain.entities.enums.TipoFim;

public class Round {
    private int numeroRound;
    private int vidaJogador1;
    private int vidaJogador2;
    private TipoFim tipoFim;
    private Jogador vencedor;

    public Round() {
        this.numeroRound = 1;
        this.vidaJogador1 = 176;
        this.vidaJogador2 = 176;
    }

    public void inserirVencedor(Jogador jogador) {
        this.vencedor = jogador;
    }

    // o intermediario vai pegar esse dado e tentar transformar em string?
    // possibilidade
    public void adicionarTipoDeFim(String tipo) {
        if (tipo == "KO") {
            this.tipoFim = TipoFim.KO;
            return;
        }
        if (tipo == "Timeover") {
            this.tipoFim = TipoFim.TIMEOVER;
            return;
        }
    }

    // tem que ver se esses tipos batem com oq eu vou receber do script
    // e se consigo fazer a tradução
    // vai ficar sendo chamada constantemente
    public int modificarVidaJogador1(int vidaAtual) {
        this.vidaJogador1 = vidaAtual;
        return this.vidaJogador1;
    }

    public int modificarVidaJogador2(int vidaAtual) {
        this.vidaJogador2 = vidaAtual;
        return this.vidaJogador2;
    }
}
