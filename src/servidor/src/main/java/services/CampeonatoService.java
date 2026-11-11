package services;
import db.Database;
import interfaces.InterfaceCampeonato;
import core.domain.entities.*;
import jakarta.jws.WebService;

import java.util.ArrayList;

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
        return c.getNome();
    }

    public String inscreverJogador(String nomeJogador, int campeonatoId) {
        String sqlBuscarCampeonato =
                """
                SELECT 1 FROM campeonato WHERE id = %d;
                """.formatted(campeonatoId);
        String result = Database.executarQuery(sqlBuscarCampeonato);
        if (!result.isEmpty()) {
            Jogador jogador = new Jogador(nomeJogador);
            String sqlInserirJogador =
                    """
                    INSERT INTO jogador (nome, campeonato_id) VALUES ('%s', %d);
                    """.formatted(jogador.getNome(), campeonatoId);
            Database.executarComando(sqlInserirJogador);
            return "Jogador: " + jogador.getNome();
        }
        else {
            return "Erro!";
        }
    }

    public String gerarChave(int campeonatoId) {
        String sqlVerificarCampeonato =
                """
                SELECT c.qtdJogadores from campeonato c WHERE c.id = %d;
                """.formatted(campeonatoId);
        String result = Database.executarQuery(sqlVerificarCampeonato);

        String sqlVerificarJogadores =
                """
                SELECT GROUP_CONCAT(j.nome) from jogador j WHERE j.campeonato_id = %d;
                """.formatted(campeonatoId);
        String resultJogador = Database.executarQuery(sqlVerificarJogadores);

        int qtd = Integer.parseInt(result);

        ArrayList<Jogador> jogadores = new ArrayList<Jogador>();
        String[] jogadoresNomes = resultJogador.split(",");
        if (qtd == 4 || qtd == 8) {
            ChaveService chaveService = new ChaveService();
            for (String s : jogadoresNomes) {
                Jogador j = new Jogador(s.trim());
                jogadores.add(j);
            }
            String ch = chaveService.criarChave(jogadores, campeonatoId);
            return ch;
        }
        else {
            return "Se chegar aqui: Não tá criando as partidas";
        }
    }

    public String listarCampeonatos() {
        String sqlListarCampeonatos =
                """
                SELECT GROUP_CONCAT(c.nome) from campeonato c;
                """;
        String result = Database.executarQuery(sqlListarCampeonatos);
        return result;
    }

    public String detalharCampeonato(int campeonatoId) {
        String busca = getCampeonatoPorId(campeonatoId);
        return busca;
    }

    public int getCampeonatoPorNome(String nome) {
        String sqlBuscarCamp =
                """
                SELECT c.id from campeonato c WHERE c.nome = '%s';
                """.formatted(nome);
        String result = Database.executarQuery(sqlBuscarCamp);
        int campeonatoId = Integer.parseInt(result);
        return campeonatoId;
    }

    public String getCampeonatoPorId(int id) {
        String sqlBuscarCamp =
                """
                SELECT c.nome || ' | ' || c.dataCriacao || ' | ' || c.estado || ' | ' || c.qtdJogadores 
                FROM campeonato c WHERE c.id = %d;
                """.formatted(id);
        String result = Database.executarQuery(sqlBuscarCamp);
        return result;
    }
}
