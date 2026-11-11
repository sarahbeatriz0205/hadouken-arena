package services;
import db.Database;
import interfaces.InterfaceCampeonato;
import core.domain.entities.*;
import jakarta.jws.WebService;

@WebService(endpointInterface = "interfaces.InterfaceCampeonato")
public class CampeonatoService implements InterfaceCampeonato {
    public String criarCampeonato(String nome, int qtdJogadores) {
        Campeonato c = new Campeonato(nome, qtdJogadores);
        String sqlInserirCampeonato =
                """
                INSERT INTO campeonato (nome, qtdJogadores, dataCriacao, estado) 
                VALUES ('%s', %s, '%s', '%s');
                """.formatted(c.getNome(), c.getQtdJogadores(), c.getDataCriacao(), c.getEstado());
        Database.executarComando(sqlInserirCampeonato);
        return "Nome: " + c.getNome() + "\nQuantidade de jogadores: " + c.getQtdJogadores();
    }

    // errado
    public Chave inserirChaveamento(Campeonato campeonato) {
        Chave chave = new Chave();
        campeonato.setChaveamento(chave);
        return chave;
    }

    public String inscreverJogador(String nomeJogador, int campeonatoId) {
        String sqlBuscarCampeonato =
                """
                SELECT 1 FROM campeonato WHERE id = %d
                """.formatted(campeonatoId);
        String result = Database.executarQuery(sqlBuscarCampeonato);
        if (!result.isEmpty()) {
            Jogador jogador = new Jogador(nomeJogador);
            String sqlInserirJogador =
                    """
                    INSERT INTO jogador (nome, campeonato_id) VALUES (%s, %d);
                    """.formatted(jogador.getNome(), campeonatoId);
            Database.executarComando(sqlInserirJogador);
            return "Jogador: " + jogador.getNome();
        }
        else {
            return "Erro!";
        }
    }
}
