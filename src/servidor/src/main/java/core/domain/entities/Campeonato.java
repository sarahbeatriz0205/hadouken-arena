package core.domain.entities;
import java.util.ArrayList;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import jakarta.xml.bind.annotation.XmlRootElement;
import jakarta.xml.bind.annotation.XmlAccessorType;
import jakarta.xml.bind.annotation.XmlAccessType;

import core.domain.entities.enums.EstadoCampeonato;

@XmlRootElement(name = "Campeonato")
@XmlAccessorType(XmlAccessType.FIELD)
public class Campeonato {
    private int id;
    private String nome;
    private ArrayList<Jogador> jogadores;
    private LocalDate dataCriacao;
    private Chave chaveamento;
    private int qtdJogadores;
    private EstadoCampeonato estado;

    public Campeonato() {

    }

    public Campeonato(String nome, int qtdJogadores) {
        this.nome = nome;
        this.qtdJogadores = qtdJogadores;
        this.jogadores = new ArrayList<Jogador>();
        this.dataCriacao = LocalDate.now();
        this.estado = EstadoCampeonato.CRIADO;
    }

    public String getNome() {
        return this.nome;
    }

    public String getDataCriacao() {
        DateTimeFormatter formato = DateTimeFormatter.ofPattern("dd/MM/yyyy");
        return this.dataCriacao.format(formato);
    }

    public int getQtdJogadores() {
        return this.qtdJogadores;
    }

    public String getEstado() {
        return this.estado.toString();
    }

    public void setChaveamento(Chave c) {
        this.chaveamento = c;
    }

    public void setJogadores(Jogador j) {
        this.jogadores.add(j);
    }
}