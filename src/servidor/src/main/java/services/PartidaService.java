package services;
import core.domain.entities.Jogador;
import core.domain.entities.Partida;
import core.domain.entities.enums.Fase;

public class PartidaService {
    public Partida criarPartida(Jogador[] jogadores, Fase fase, int posicaoFase) {
        return new Partida(jogadores, fase, posicaoFase);
    }
}
