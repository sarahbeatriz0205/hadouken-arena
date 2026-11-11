package core.domain.entities;
import jakarta.xml.bind.annotation.XmlAccessType;
import jakarta.xml.bind.annotation.XmlAccessorType;
import jakarta.xml.bind.annotation.XmlRootElement;

@XmlRootElement(name = "Campeonato")
@XmlAccessorType(XmlAccessType.FIELD)
public class Jogador {
    private int id;
    private String nome;

    public Jogador() {

    }

    public Jogador(String nome) {
        this.nome = nome;
    }

    public String getNome() {
        return this.nome;
    }

    public int getId() {
        return this.id;
    }
}
