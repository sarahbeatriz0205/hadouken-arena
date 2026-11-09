package services;
import interfaces.InterfaceCampeonato;
import core.domain.entities.*;
import jakarta.jws.WebService;

@WebService(endpointInterface = "interfaces.InterfaceCampeonato")
public class CampeonatoService implements InterfaceCampeonato {
    public Campeonato criarCampeonato(String nome, int qtdJogadores) {
        return new Campeonato(nome, qtdJogadores);
    }

    public Chave inserirChaveamento(Campeonato campeonato) {
        Chave chave = new Chave();
        campeonato.setChaveamento(chave);
        return chave;
    }

    public Jogador inscreverJogador(Jogador jogador, Campeonato campeonato) {
        campeonato.setJogadores(jogador);
        return jogador;
    }
}
