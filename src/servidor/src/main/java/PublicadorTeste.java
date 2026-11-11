import jakarta.xml.ws.Endpoint;
import services.*;

public class PublicadorTeste {
    public static void main(String[] args) {
        Endpoint.publish(
                "http://127.0.0.1:9876/campeonato",
                new CampeonatoService()
        );
    }
}
