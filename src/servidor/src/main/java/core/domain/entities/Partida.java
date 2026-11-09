package core.domain.entities;
import core.domain.entities.enums.EstadoPartida;
import core.domain.entities.enums.Fase;

public class Partida {
    private Jogador[] jogadores;
    private EstadoPartida estado;
    private Fase fase;
    private Jogador vencedor;
    private int posicaoFase; 
    private int roundsGanhosJogador1;
    private int roundsGanhosJogador2;

    public Partida(Jogador[] jogadores, Fase fase, int posicaoFase) {
        this.jogadores = jogadores;
        this.estado = EstadoPartida.NAO_INICIADA;
        this.fase = fase;
        this.posicaoFase = posicaoFase;
    }

    public void inserirVencedor(Jogador jogador) {
        this.vencedor = jogador;
    }

    public void adicionarRodadaGanha() {
        if (this.vencedor == this.jogadores[0]) {
            this.roundsGanhosJogador1++;
        }
        else {
            this.roundsGanhosJogador2++;
        }
    }
}
