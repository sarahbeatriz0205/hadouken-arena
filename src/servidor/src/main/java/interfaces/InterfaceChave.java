package interfaces;
import core.domain.entities.Chave;
import core.domain.entities.Jogador;
import jakarta.jws.WebMethod;
import jakarta.jws.WebService;

import java.util.ArrayList;

@WebService
public interface InterfaceChave {
    @WebMethod Chave criarChave(ArrayList<Jogador> jogadores);
}
