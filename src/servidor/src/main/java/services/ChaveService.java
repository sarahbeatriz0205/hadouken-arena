package services;
import core.domain.entities.*;
import core.domain.entities.enums.Fase;
import core.utils.grafo.Vertice;
import interfaces.InterfaceChave;
import jakarta.jws.WebService;
import java.util.ArrayList;

@WebService(endpointInterface = "interfaces.InterfaceChave")
public class ChaveService implements InterfaceChave {
    public Chave criarChave(ArrayList<Jogador> jogadores) {
        Chave chaveamento = new Chave();
        PartidaService servico = new PartidaService();
        int qtdPartidasIniciais = jogadores.size() / 2;
        Fase faseAtual = Fase.descobrirFase(qtdPartidasIniciais);
        int posicao = 1;
        ArrayList<Vertice> partidasFaseAtual = new ArrayList<Vertice>();

        for (int i=0; i < jogadores.size(); i++) {
            Jogador[] jogadoresAtuais = new Jogador[]{ jogadores.get(i*2), jogadores.get((i*2)+1) };
            Partida partida = servico.criarPartida(jogadoresAtuais, faseAtual, posicao);
            chaveamento.getChaveamento().inserirVertice(partida);
            Vertice vInicio = chaveamento.getChaveamento().vertices().getLast();
            partidasFaseAtual.add(vInicio);
            posicao++;
        }

        int qtdProximasPartidas = qtdPartidasIniciais / 2;
        posicao = 1;
        while (qtdProximasPartidas > 1) {
            Fase proximaFase = Fase.descobrirFase(qtdProximasPartidas);
            ArrayList<Vertice> proximasPartidas = new ArrayList<Vertice>();

            for (int j=0; j < qtdProximasPartidas; j++) {
                Partida partidaFutura = servico.criarPartida(new Jogador[2], proximaFase, posicao);
                chaveamento.getChaveamento().inserirVertice(partidaFutura);
                Vertice vFuturo = chaveamento.getChaveamento().vertices().getLast();
                proximasPartidas.add(vFuturo);

                Vertice partidaAnterior1 = partidasFaseAtual.get(j * 2);
                Vertice partidaAnterior2 = partidasFaseAtual.get((j * 2) + 1);

                chaveamento.getChaveamento().inserirAresta(partidaAnterior1, vFuturo, null, true);
                chaveamento.getChaveamento().inserirAresta(partidaAnterior2, vFuturo, null, true);
                posicao++;
            }

            partidasFaseAtual = proximasPartidas;
            qtdProximasPartidas /= 2;
        }
        return chaveamento;
    }
}
