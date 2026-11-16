package services;
import core.domain.entities.Jogador;
import core.domain.entities.Partida;
import core.domain.entities.enums.Fase;
import db.Database;
import interfaces.InterfacePartida;
import jakarta.jws.WebService;

@WebService(endpointInterface = "interfaces.InterfacePartida")
public class PartidaService implements InterfacePartida {
    public Partida criarPartida(Jogador[] jogadores, Fase fase, int posicaoFase) {
        return new Partida(jogadores, fase, posicaoFase);
    }

    // RF04 - Início de partida
    public String iniciarPartidaLocal(int partidaId) {
        String partida = buscarPartidaPorId(partidaId);
        String[] particaoPartida = partida.split(" \\| ");
        String sqlAlterarEstadoPartida =
                """
                UPDATE partida SET estado='%s' WHERE id = %d;
                """.formatted("AGUARDANDO_EMULADOR", partidaId);
        Database.executarComando(sqlAlterarEstadoPartida);

        // gambiarra pra saber se o estado ta mudando no banco
        partida = buscarPartidaPorId(partidaId);
        String[] particaoPartidaAtualizada = partida.split(" \\| ");
        return particaoPartidaAtualizada[5].trim();
    }

    public String buscarPartidasPorCampeonato(int campeonato_id) {
        String sqlBuscarPartidas =
                """
                SELECT p.id || ' | ' || COALESCE(j1.nome, 'A definir') || ' | ' ||
                                                  COALESCE(j2.nome, 'A definir') || ' | ' ||
                                                  COALESCE(jv.nome, 'A definir') || ' | ' || p.fase || ' | ' || p.estado || ' | ' || p.posicao || ' | ' || COALESCE(p.rounds_jogador2, 0) || ' | ' || COALESCE(p.rounds_jogador1, 0)
                FROM partida p LEFT JOIN jogador j1 ON p.jogador_id1 = j1.id
                                               LEFT JOIN jogador j2 ON p.jogador_id2 = j2.id
                                               LEFT JOIN jogador jv ON p.vencedor_id = jv.id
                                               WHERE p.campeonato_id = %d;
                """.formatted(campeonato_id);
        String result = Database.executarQuery(sqlBuscarPartidas);
        return result;
    }

    public String buscarPartidaPorId(int partidaId) {
        String sqlPartidaBuscada = 
                """
                                    SELECT p.id || ' | ' ||
                                           COALESCE(j1.nome, 'A definir') || ' | ' ||
                                           COALESCE(j2.nome, 'A definir') || ' | ' ||
                                           COALESCE(jv.nome, 'A definir') || ' | ' ||
                                           p.fase || ' | ' ||
                                           p.estado || ' | ' ||
                                           p.posicao || ' | ' ||
                                           COALESCE(p.rounds_jogador1, 0) || ' | ' ||
                                           COALESCE(p.rounds_jogador2, 0) || ' | ' ||
                                           p.campeonato_id
                                    FROM partida p
                                    LEFT JOIN jogador j1 ON p.jogador_id1 = j1.id
                                    LEFT JOIN jogador j2 ON p.jogador_id2 = j2.id
                                    LEFT JOIN jogador jv ON p.vencedor_id = jv.id
                                    WHERE p.id = %d;
                """.formatted(partidaId);
        String result = Database.executarQuery(sqlPartidaBuscada).trim();
        return result;
    }
}
