package interfaces;
import core.domain.entities.*;
import core.domain.entities.enums.Fase;
import jakarta.jws.WebMethod;
import jakarta.jws.WebService;

@WebService
public interface InterfacePartida {
    @WebMethod Partida criarPartida(Jogador[] jogadores, Fase fase, int posicaoFase);
}
