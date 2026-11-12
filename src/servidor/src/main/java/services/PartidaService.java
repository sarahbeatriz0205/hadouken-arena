package services;
import core.domain.entities.Jogador;
import core.domain.entities.Partida;
import core.domain.entities.enums.Fase;
import db.Database;

public class PartidaService {
    public Partida criarPartida(Jogador[] jogadores, Fase fase, int posicaoFase) {
        return new Partida(jogadores, fase, posicaoFase);
    }

    // RF04 - Início de partida
    public String iniciarPartidaLocal(int partidaId) {
        String partida = buscarPartidaPorId(partidaId);
        String[] particaoPartida = partida.split(",");
        String estado = particaoPartida[6]; // posicao possivel do estado
        String sqlAlterarEstadoPartida = 
                """
                UPDATE partida p SET p.estado='%s' WHERE p.id = %d;
                """.formatted("AGUARDANDO_EMULADOR", partidaId);
        Database.executarComando(sqlAlterarEstadoPartida);

        // gambiarra pra saber se o estado ta mudando no banco
        partida = buscarPartidaPorId(partidaId);
        String[] particaoPartidaAtualizada = partida.split(",");
        String estadoNovo = particaoPartidaAtualizada[6];
        return "Estado velho: " + estado + " | " + "Estado novo: " + estadoNovo;
    }

    public String buscarPartidasPorCampeonato(int campeonato_id) {
        String sqlBuscarPartidas =
                """
                SELECT p.jogador_id1 || ' | ' || p.jogador_id2 || ' | ' || p.vencedor_id || ' | ' || p.fase || ' | ' || p.estado || ' | ' || p.posicao || ' | ' || p.rounds_jogador2 || ' | ' || p.rounds_jogador1
                FROM partida p WHERE p.campeonato_id = %d;
                """.formatted(campeonato_id);
        String result = Database.executarQuery(sqlBuscarPartidas);
        return result;
    }

    public String buscarPartidaPorId(int partidaId) {
        String sqlPartidaBuscada = 
                """
                SELECT 1 FROM partida p WHERE p.id = %d; 
                """.formatted(partidaId);
        String result = Database.executarQuery(sqlPartidaBuscada);
        return result;
    }
}
