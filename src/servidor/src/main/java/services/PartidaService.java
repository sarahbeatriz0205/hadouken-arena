package services;
import core.domain.entities.Jogador;
import core.domain.entities.Partida;
import core.domain.entities.enums.Fase;
import interfaces.InterfacePartida;
import jakarta.jws.WebService;

@WebService(endpointInterface = "interfaces.InterfacePartida")
public class PartidaService implements InterfacePartida {
    public Partida criarPartida(Jogador[] jogadores, Fase fase, int posicaoFase) {
        return new Partida(jogadores, fase, posicaoFase);
    }
}
