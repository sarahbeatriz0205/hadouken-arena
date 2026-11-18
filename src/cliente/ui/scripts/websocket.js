var socket = new WebSocket("ws://localhost:8080/intermediario/transmissao");
socket.onmessage = atualizaCliente;

function atualizaCliente(e) {
    var retornoVidaJogador1 = document.getElementById("vidaJogador1");
    var retornoVidaJogador2 = document.getElementById("vidaJogador2");
    var retornoTimer = document.getElementById("timer");

    var dadosPartida = JSON.parse(e.data);

    if (dadosPartida.acao === "dadosAtualizados") {
        retornoVidaJogador1.textContent = dadosPartida.vidaJogador1;
        retornoVidaJogador2.textContent = dadosPartida.vidaJogador2;
        retornoTimer.textContent = dadosPartida.timer;
    }
}