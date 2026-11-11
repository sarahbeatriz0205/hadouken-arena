package interfaces;
import core.domain.entities.Campeonato;
import core.domain.entities.Chave;
import core.domain.entities.Jogador;
import jakarta.jws.WebMethod;
import jakarta.jws.WebService;

@WebService
public interface InterfaceCampeonato {
    @WebMethod String criarCampeonato(String nome, int qtdJogadores);
    @WebMethod String inscreverJogador(String nomeJogador, int campeonatoId);
    @WebMethod String gerarChave(int campeonatoId);
    @WebMethod String listarCampeonatos();
}
