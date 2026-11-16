const url = "http://localhost:8000";

function criarCampeonato(event) {
    event.preventDefault();

    const nomeCamp = document.getElementById("nome-camp").value;
    const qtdJogadores = parseInt(document.getElementById("qtd-jog").value);

    const xml = `<soapenv:Envelope xmlns:soapenv="http://schemas.xmlsoap.org/soap/envelope/" xmlns:int="http://interfaces/">
   <soapenv:Header/>
   <soapenv:Body>
      <int:criarCampeonato>
         <arg0>${nomeCamp}</arg0>
         <arg1>${qtdJogadores}</arg1>
      </int:criarCampeonato>
   </soapenv:Body>
</soapenv:Envelope>`;

    fetch(url, {
        method: "POST",
        headers: {
            "Content-Type": "text/xml"
        },
        body: xml
    })
        .then(response => response.text())
        .then(() => {
            window.location.href = "index.html";
        });
}

const acaoCriar = document.getElementById("btn-criar-camp");
acaoCriar.addEventListener("click", criarCampeonato);