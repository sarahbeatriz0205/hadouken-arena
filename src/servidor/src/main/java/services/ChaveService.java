package services;
import core.domain.entities.*;
import core.domain.entities.enums.Fase;
import core.utils.grafo.Vertice;
import db.Database;

import java.util.ArrayList;

public class ChaveService {
    public String criarChave(ArrayList<Jogador> jogadores, int campeonato_id) {
        Chave chaveamento = new Chave();
        PartidaService servico = new PartidaService();
        int qtdPartidasIniciais = jogadores.size() / 2;
        Fase faseAtual = Fase.descobrirFase(qtdPartidasIniciais);
        int posicao = 1;
        int partidasCriadas = 0;
        ArrayList<Vertice> partidasFaseAtual = new ArrayList<Vertice>();

        for (int i = 0; i < qtdPartidasIniciais; i++) {
            Jogador[] jogadoresAtuais = new Jogador[]{ jogadores.get(i * 2), jogadores.get((i * 2) + 1) };
            Partida partida = servico.criarPartida(jogadoresAtuais, faseAtual, posicao);
            chaveamento.getChaveamento().inserirVertice(partida);
            Vertice vInicio = chaveamento.getChaveamento().vertices().getLast();
            partidasFaseAtual.add(vInicio);

            String sqlVerificaJogador =
                    """
                    SELECT GROUP_CONCAT(j.id) FROM jogador j WHERE j.nome IN ('%s', '%s') AND j.campeonato_id = %d;
                    """.formatted(jogadoresAtuais[0].getNome(), jogadoresAtuais[1].getNome(), campeonato_id);

            String result = Database.executarQuery(sqlVerificaJogador);

            int idJogador1 = 0;
            int idJogador2 = 0;

            if (result != null && !result.trim().isEmpty()) {
                String[] partes = result.split(",");
                if (partes.length > 0) idJogador1 = Integer.parseInt(partes[0].trim());
                if (partes.length > 1) idJogador2 = Integer.parseInt(partes[1].trim());
            }

            String sqlInserirPartida =
                    """
                    INSERT INTO partida (jogador_id1, jogador_id2, campeonato_id, fase, estado, posicao) 
                    VALUES (%d, %d, %d, '%s', '%s', %d);
                    """.formatted(
                            idJogador1,
                            idJogador2,
                            campeonato_id,
                            faseAtual.name(),
                            partida.getEstado(),
                            posicao
                    );

            Database.executarComando(sqlInserirPartida);
            posicao++;
            partidasCriadas++;
        }

        int qtdProximasPartidas = qtdPartidasIniciais / 2;
        posicao = 1;
        while (qtdProximasPartidas > 1) {
            Fase proximaFase = Fase.descobrirFase(qtdProximasPartidas);
            ArrayList<Vertice> proximasPartidas = new ArrayList<Vertice>();

            for (int j = 0; j < qtdProximasPartidas; j++) {
                Partida partidaFutura = servico.criarPartida(new Jogador[2], proximaFase, posicao);
                chaveamento.getChaveamento().inserirVertice(partidaFutura);
                Vertice vFuturo = chaveamento.getChaveamento().vertices().getLast();
                proximasPartidas.add(vFuturo);

                Vertice partidaAnterior1 = partidasFaseAtual.get(j * 2);
                Vertice partidaAnterior2 = partidasFaseAtual.get((j * 2) + 1);

                chaveamento.getChaveamento().inserirAresta(partidaAnterior1, vFuturo, null, true);
                chaveamento.getChaveamento().inserirAresta(partidaAnterior2, vFuturo, null, true);

                String sqlInserirPartida =
                        """
                        INSERT INTO partida (jogador_id1, jogador_id2, campeonato_id, fase, estado, posicao) 
                        VALUES (NULL, NULL, %d, '%s', '%s', %d);
                        """.formatted(campeonato_id, faseAtual.name(), partidaFutura.getEstado(), posicao);
                Database.executarComando(sqlInserirPartida);
                posicao++;
                partidasCriadas++;
            }

            partidasFaseAtual = proximasPartidas;
            qtdProximasPartidas /= 2;
        }
        return "Quantidade de jogadores: " + jogadores.size() + " | " + "Quantidade de partidas criadas: " + partidasCriadas;
    }
}