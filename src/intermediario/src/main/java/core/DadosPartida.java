package core;

public class DadosPartida {
    private int vidaJogador1;
    private int vidaJogador2;
    private int timer;

    public DadosPartida(int vidaJogador1, int vidaJogador2, int timer) {
        this.vidaJogador1 = vidaJogador1;
        this.vidaJogador2 = vidaJogador2;
        this.timer = timer;
    }

    public int getVidaJogador1() {
        return this.vidaJogador1;
    }

    public int getVidaJogador2() {
        return this.vidaJogador2;
    }

    public int getTimer() {
        return this.timer;
    }

    public void setVidaJogador1(int v1) {
        this.vidaJogador1 = v1;
    }

    public void setVidaJogador2(int v2) {
        this.vidaJogador2 = v2;
    }

    public void setTimer(int t) {
        this.timer = t;
    }
}
