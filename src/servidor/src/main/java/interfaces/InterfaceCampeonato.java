package interfaces;
import core.domain.entities.Campeonato;
import jakarta.jws.WebMethod;
import jakarta.jws.WebService;

@WebService
public interface InterfaceCampeonato {
    @WebMethod Campeonato criarCampeonato(String nome, int qtdJogadores);
    @WebMethod String inscreverJogador(String nomeJogador, int campeonatoId);
    @WebMethod String gerarChave(int campeonatoId);
    @WebMethod String listarCampeonatos();
    @WebMethod String detalharCampeonato(int campeonatoId);
    @WebMethod int getCampeonatoPorNome(String nome);
    @WebMethod String[] listarJogadoresPorCampeonato(int campeonatoId);
}
