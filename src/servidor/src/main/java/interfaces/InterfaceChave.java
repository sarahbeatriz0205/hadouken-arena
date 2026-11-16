package interfaces;
import jakarta.jws.WebMethod;
import jakarta.jws.WebService;

@WebService
public interface InterfaceChave {
    @WebMethod String[] exibirChaveamentoPorCampeonato(int campeonato_id);
}
