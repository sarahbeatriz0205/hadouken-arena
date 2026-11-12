package db;
import java.lang.reflect.Array;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.Statement;
import java.util.ArrayList;
import java.sql.ResultSet;
import java.sql.SQLException;

public class Database {
    private static final String url = "jdbc:sqlite:hadouken_db.db";

    public static Connection conectar() {
        try {
            return DriverManager.getConnection(url);
        } catch (SQLException e) {
            System.out.println("Erro ao conectar: " + e.getMessage());
            return null;
        }
    }

    public static void criarTabelas() {
        String sqlTabelaCampeonato =
                """
                CREATE TABLE IF NOT EXISTS campeonato (
                    id INTEGER PRIMARY KEY AUTOINCREMENT,
                    nome TEXT NOT NULL,
                    dataCriacao TEXT NOT NULL, 
                    qtdJogadores INT NOT NULL,
                    estado TEXT NOT NULL
                );    
                """;

        String sqlTabelaJogador =
                """
                CREATE TABLE IF NOT EXISTS jogador (
                    id INTEGER PRIMARY KEY AUTOINCREMENT,
                    campeonato_id INTEGER,
                    nome TEXT NOT NULL,
                    FOREIGN KEY (campeonato_id) REFERENCES campeonato(id)
                );    
                """;

        String sqlTabelaPartida =
                """
                CREATE TABLE IF NOT EXISTS partida (
                    id INTEGER PRIMARY KEY AUTOINCREMENT,
                    jogador_id1 INTEGER,
                    jogador_id2 INTEGER,
                    vencedor_id INTEGER,
                    campeonato_id INTEGER,
                    fase TEXT NOT NULL,
                    estado TEXT NOT NULL,
                    posicao INTEGER,
                    proxima_partida_id INTEGER,
                    rounds_jogador1 INTEGER DEFAULT 0,
                    rounds_jogador2 INTEGER DEFAULT 0,
                    FOREIGN KEY (campeonato_id) REFERENCES campeonato(id),
                    FOREIGN KEY (jogador_id1) REFERENCES jogador(id),
                    FOREIGN KEY (jogador_id2) REFERENCES jogador(id),
                    FOREIGN KEY (vencedor_id) REFERENCES jogador(id),
                    FOREIGN KEY (proxima_partida_id) REFERENCES partida(id)
                );    
                """;
        try (Connection conn = conectar();
             Statement s = conn.createStatement()) {
            if (conn != null) {
                s.execute(sqlTabelaCampeonato);
                s.execute(sqlTabelaJogador);
                s.execute(sqlTabelaPartida);
            }
        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }
    }

    public static void executarComando(String comando) {
        try (Connection conn = conectar();
             Statement stmt = conn.createStatement()) {
            stmt.execute(comando);
        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }
    }

    public static String executarQuery(String comando) {
        String resultados = ""; // gambiarra pra nao afetar o restante dos metodos que usam esse metodo
        try (Connection conn = conectar();
             Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery(comando)) {

            while (rs.next()) {
                resultados += rs.getString(1) + "\n";
            }
        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }
        return resultados;
    }

    public static void main(String[] args) {
        criarTabelas();
    }
}