const url = "http://localhost:8000";
let partidaId = 0;

function detalharPartida() {
    const params = new URLSearchParams(window.location.search);
    const idPartida = params.get("id");

    const campNome = params.get("camp");
    const btnVoltar = document.getElementById("btn-voltar");
    if (campNome && btnVoltar) {
        btnVoltar.href = `detalhamento_campeonato.html?nome=${encodeURIComponent(campNome)}`;
    }

    const xml = `<soapenv:Envelope xmlns:soapenv="http://schemas.xmlsoap.org/soap/envelope/" xmlns:int="http://interfaces/">
       <soapenv:Header/>
       <soapenv:Body>
          <int:buscarPartidaPorId>
             <arg0>${idPartida}</arg0>
          </int:buscarPartidaPorId>
       </soapenv:Body>
    </soapenv:Envelope>`;

    fetch(url, {
        method: "POST",
        headers: { "Content-Type": "text/xml" },
        body: xml
    })
        .then(response => response.text())
        .then(xmlResposta => {
            const parser = new DOMParser();
            const xmlDoc = parser.parseFromString(xmlResposta, "text/xml");
            const retorno = xmlDoc.getElementsByTagName("return")[0]?.textContent.trim();

            if (!retorno) return;

            const [id, nomeJogador1, nomeJogador2, nomeVencedor, fase, estado, posicao, roundsJogador1, roundsJogador2, campeonatoId] = retorno.split(" | ");

            partidaId = id;

            document.getElementById("partida-titulo").textContent = `${nomeJogador1} vs ${nomeJogador2}`;
            document.getElementById("partida-fase").textContent = `Fase: ${fase} - Posição: ${posicao}`;
            document.getElementById("partida-estado").textContent = estado;
            document.getElementById("j1-nome").textContent = nomeJogador1;
            document.getElementById("j2-nome").textContent = nomeJogador2;
        });
}

document.addEventListener("DOMContentLoaded", detalharPartida);

function iniciarPartida() {
    const params = new URLSearchParams(window.location.search);
    const idPartida = params.get("id");

    const xml = `<soapenv:Envelope xmlns:soapenv="http://schemas.xmlsoap.org/soap/envelope/" xmlns:int="http://interfaces/">
       <soapenv:Header/>
       <soapenv:Body>
          <int:iniciarPartidaLocal>
             <arg0>${idPartida}</arg0>
          </int:iniciarPartidaLocal>
       </soapenv:Body>
    </soapenv:Envelope>`;

    fetch(url, {
        method: "POST",
        headers: { "Content-Type": "text/xml" },
        body: xml
    })
        .then(response => response.text())
        .then(xmlResposta => {
            const parser = new DOMParser();
            const xmlDoc = parser.parseFromString(xmlResposta, "text/xml");
            const retorno = xmlDoc.getElementsByTagName("return")[0]?.textContent.trim();

            document.getElementById("partida-estado").textContent = retorno;

            detalharPartida();
        });
}

const btnIniciarPartida = document.getElementById("btn-iniciar-partida");
btnIniciarPartida.addEventListener("click", iniciarPartida);