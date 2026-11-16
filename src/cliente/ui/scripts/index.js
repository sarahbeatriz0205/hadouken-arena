function carregarCampeonatos() {
    const xml = `<soapenv:Envelope xmlns:soapenv="http://schemas.xmlsoap.org/soap/envelope/" xmlns:int="http://interfaces/">
   <soapenv:Header/>
   <soapenv:Body>
      <int:listarCampeonatos/>
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
            const retorno = xmlDoc.getElementsByTagName("return")[0].textContent.trim();

            const listaContainer = document.getElementById("lista-camps");
            listaContainer.innerHTML = "";

            if (!retorno) return;

            const nomes = retorno.split(",");

            nomes.forEach(nome => {
                const novaDiv = document.createElement("div");
                novaDiv.className = "tournament-item";

                novaDiv.innerHTML = `
                <div class="tournament-info">
                    <strong>${nome}</strong>
                </div>
                <button type="button" class="btn-detalhar">Detalhar</button>
           
            `;
                const btnDetalhar = novaDiv.querySelector(".btn-detalhar");
                btnDetalhar.addEventListener("click", () => {
                    window.location.href = `detalhamento_campeonato.html?nome=${encodeURIComponent(nome)}`;
                });

                listaContainer.appendChild(novaDiv);
            });
        });
}

window.addEventListener("DOMContentLoaded", carregarCampeonatos);