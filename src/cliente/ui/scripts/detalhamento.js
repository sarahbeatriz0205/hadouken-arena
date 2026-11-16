const url = "http://localhost:8000";
let campeonatoId = null;

function carregarJogadores() {
    if (!campeonatoId) return;

    const xml = `<soapenv:Envelope xmlns:soapenv="http://schemas.xmlsoap.org/soap/envelope/" xmlns:int="http://interfaces/">
   <soapenv:Header/>
   <soapenv:Body>
      <int:listarJogadoresPorCampeonato>
         <arg0>${campeonatoId}</arg0>
      </int:listarJogadoresPorCampeonato>
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

            const tagsReturn = xmlDoc.getElementsByTagName("return");

            const lista = document.getElementById("lista-jogadores");
            lista.innerHTML = "";

            if (tagsReturn.length === 0) {
                lista.innerHTML = "<li>Nenhum jogador inscrito ainda.</li>";
                return;
            }

            Array.from(tagsReturn).forEach(tag => {
                const nomeJogador = tag.textContent.trim();
                if (!nomeJogador) return;

                const li = document.createElement("li");
                li.textContent = nomeJogador;
                lista.appendChild(li);
            });
        });
}

function detalharCampeonato() {
    const params = new URLSearchParams(window.location.search);
    const nomeParam = params.get("nome");

    if (!nomeParam) return;

    const xml = `<soapenv:Envelope xmlns:soapenv="http://schemas.xmlsoap.org/soap/envelope/" xmlns:int="http://interfaces/">
   <soapenv:Header/>
   <soapenv:Body>
      <int:detalharCampeonato>
         <arg0>${nomeParam}</arg0>
      </int:detalharCampeonato>
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

            const [id, gambiarraMaster, nomeCamp, data, estado, qtd] = retorno.split(" | ");

            campeonatoId = id;

            document.getElementById("camp-nome").textContent = nomeCamp;
            document.getElementById("camp-info").textContent = `Vagas: ${qtd} Jogadores - Criado em ${data}`;
            document.getElementById("camp-estado").textContent = estado;

            carregarJogadores();
            carregarChaveamento();
        });
}

function inscreverJogador() {
    const inputJogador = document.getElementById("nome-jogador");
    const nomeJogador = inputJogador.value.trim();

    if (!nomeJogador || !campeonatoId) return;

    const xml = `<soapenv:Envelope xmlns:soapenv="http://schemas.xmlsoap.org/soap/envelope/" xmlns:int="http://interfaces/">
   <soapenv:Header/>
   <soapenv:Body>
      <int:inscreverJogador>
         <arg0>${nomeJogador}</arg0>
         <arg1>${campeonatoId}</arg1>
      </int:inscreverJogador>
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

            inputJogador.value = "";
            detalharCampeonato();
        });
}

const btnInscrever = document.getElementById("btn-inscrever");
if (btnInscrever) {
    btnInscrever.addEventListener("click", inscreverJogador);
}

window.addEventListener("DOMContentLoaded", detalharCampeonato);

function gerarChaves() {
    const xml = `<soapenv:Envelope xmlns:soapenv="http://schemas.xmlsoap.org/soap/envelope/" xmlns:int="http://interfaces/">
   <soapenv:Header/>
   <soapenv:Body>
      <int:gerarChave>
         <arg0>${campeonatoId}</arg0>
      </int:gerarChave>
   </soapenv:Body>
</soapenv:Envelope>`;

    fetch(url, {
        method: "POST",
        headers: { "Content-Type": "text/xml" },
        body: xml
    })
        .then(response => response.text()).then(xmlResposta => {
        const parser = new DOMParser();
        const xmlDoc = parser.parseFromString(xmlResposta, "text/xml");
        detalharCampeonato();
    });
}

const btnGerar = document.getElementById("btn-iniciar-camp");
if (btnGerar) {
    btnGerar.addEventListener("click", gerarChaves);
}

function carregarChaveamento() {
    if (!campeonatoId) return;

    const xml = `<soapenv:Envelope xmlns:soapenv="http://schemas.xmlsoap.org/soap/envelope/" xmlns:int="http://interfaces/">
   <soapenv:Header/>
   <soapenv:Body>
      <int:exibirChaveamentoPorCampeonato>
         <arg0>${campeonatoId}</arg0>
      </int:exibirChaveamentoPorCampeonato>
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
            const tagsReturn = xmlDoc.getElementsByTagName("return");

            const partidasContainer = document.getElementById("lista-partidas");
            partidasContainer.innerHTML = "";

            if (tagsReturn.length === 0) {
                partidasContainer.innerHTML = "<div>Nenhuma partida gerada ainda.</div>";
                return;
            }

            Array.from(tagsReturn).forEach(tag => {
                const textoBruto = tag.textContent.trim();
                if (!textoBruto) return;

                const [jog1, jog2, fase, estado] = textoBruto.split(" | ");

                const nome1 = (!jog1 || jog1 === "null") ? "A definir" : jog1;
                const nome2 = (!jog2 || jog2 === "null") ? "A definir" : jog2;

                const novaDiv = document.createElement("div");
                novaDiv.className = "lista-partidas";

                novaDiv.innerHTML = `
        <div class="partidas">
            <strong>${nome1} vs ${nome2}</strong>
            <span> (${fase} - ${estado})</span>
        </div>
        <button type="button" class="btn-acompanhar">Acompanhar</button>
    `;

                partidasContainer.appendChild(novaDiv);
            });
        });
}