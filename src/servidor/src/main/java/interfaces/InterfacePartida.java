package interfaces;
import jakarta.jws.WebMethod;
import jakarta.jws.WebService;

@WebService 
public interface InterfacePartida {
    @WebMethod String iniciarPartidaLocal(int partidaId);
}
