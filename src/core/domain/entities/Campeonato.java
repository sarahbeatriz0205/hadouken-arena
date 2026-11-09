package core.domain.entities;
import java.util.ArrayList;
import java.time.LocalDate;

import core.domain.entities.enums.EstadoCampeonato;

public class Campeonato {
    private int id;
    private String nome;
    private ArrayList<Jogador> jogadores;
    private LocalDate dataCriacao;
    private Chave chaveamento;
    private int qtdJogadores;
    private EstadoCampeonato estado;

    public Campeonato(String nome, int qtdJogadores) {
        this.nome = nome;
        this.qtdJogadores = qtdJogadores;
        this.jogadores = new ArrayList<Jogador>();
        this.dataCriacao = LocalDate.now();
        this.estado = EstadoCampeonato.CRIADO;
    }

    public void inserirChaveamento() {
        Chave chave = new Chave();
        this.chaveamento = chave;
    }
}