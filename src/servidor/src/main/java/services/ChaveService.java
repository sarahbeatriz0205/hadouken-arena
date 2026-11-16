package services;
import core.domain.entities.*;
import core.domain.entities.enums.Fase;
import core.utils.grafo.Vertice;
import db.Database;
import interfaces.InterfaceChave;
import jakarta.jws.WebService;

import java.util.ArrayList;

@WebService(endpointInterface = "interfaces.InterfaceChave")
public class ChaveService implements InterfaceChave {
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

            String sqlJ1 = "SELECT id FROM jogador WHERE nome = '%s' AND campeonato_id = %d LIMIT 1;"
                    .formatted(jogadoresAtuais[0].getNome(), campeonato_id);
            String sqlJ2 = "SELECT id FROM jogador WHERE nome = '%s' AND campeonato_id = %d LIMIT 1;"
                    .formatted(jogadoresAtuais[1].getNome(), campeonato_id);

            String res1 = Database.executarQuery(sqlJ1).trim();
            String res2 = Database.executarQuery(sqlJ2).trim();

            if (res1.isEmpty() || res2.isEmpty()) {
                throw new RuntimeException("Erro: Um ou mais jogadores não foram encontrados no banco para o campeonato " + campeonato_id);
            }

            int idJogador1 = Integer.parseInt(res1);
            int idJogador2 = Integer.parseInt(res2);

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
        while (qtdProximasPartidas >= 1) {
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
                        """.formatted(campeonato_id, proximaFase.name(), partidaFutura.getEstado(), posicao);
                Database.executarComando(sqlInserirPartida);
                posicao++;
                partidasCriadas++;
            }

            partidasFaseAtual = proximasPartidas;
            qtdProximasPartidas /= 2;
        }
        return "Quantidade de jogadores: " + jogadores.size() + " | " + "Quantidade de partidas criadas: " + partidasCriadas;
    }

    // RF08 - Consulta da chave
    public String[] exibirChaveamentoPorCampeonato(int campeonato_id) {
        PartidaService partidaServ = new PartidaService();
        String partidasPorCampeonato = partidaServ.buscarPartidasPorCampeonato(campeonato_id);

        //nao sei se é suficiente. tem que testar
        String[] listaDePartidas = partidasPorCampeonato.split("\n");
        return listaDePartidas;
    }
}