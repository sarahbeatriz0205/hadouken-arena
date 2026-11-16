from zeep import Client
from http.server import BaseHTTPRequestHandler, HTTPServer
import xml.etree.ElementTree as et

wsdl_campeonato = "http://127.0.0.1:9876/campeonato?wsdl"
wsdl_partida = "http://127.0.0.1:9876/partida?wsdl"
wsdl_chave = "http://127.0.0.1:9876/chave?wsdl"

client_campeonato = Client(wsdl=wsdl_campeonato)
client_partida = Client(wsdl=wsdl_partida)
client_chave = Client(wsdl=wsdl_chave)

class Gateway(BaseHTTPRequestHandler):
    def _enviar_cors(self):
        self.send_header("Access-Control-Allow-Origin", "*")
        self.send_header("Access-Control-Allow-Methods", "GET, POST, OPTIONS")
        self.send_header("Access-Control-Allow-Headers", "Content-Type")

    def do_OPTIONS(self):
        self.send_response(204)
        self._enviar_cors()
        self.end_headers()

    def do_POST(self):
        tamanho = int(self.headers.get("Content-Length", 0))
        xml_recebido = self.rfile.read(tamanho).decode("utf-8")
        raiz = et.fromstring(xml_recebido)

        operacao = raiz.find(".//{*}criarCampeonato")

        if raiz.find(".//{*}criarCampeonato") is not None:
            nome = raiz.find(".//{*}arg0").text
            qtd = int(raiz.find(".//{*}arg1").text)
            camp_criado = client_campeonato.service.criarCampeonato(nome, qtd)
            xml_resposta = f"""<resposta>
                <return>
                    <nome>{camp_criado.nome}</nome>
                    <qtdJogadores>{camp_criado.qtdJogadores}</qtdJogadores>
                </return>
            </resposta>"""
        elif raiz.find(".//{*}listarCampeonatos") is not None:
            lista = client_campeonato.service.listarCampeonatos()
            xml_resposta = f"""<resposta>
                <return>
                    {lista}
                </return>
            </resposta>"""
        elif raiz.find(".//{*}detalharCampeonato") is not None:
            nome_camp = raiz.find(".//{*}arg0").text
            camp_id = client_campeonato.service.getCampeonatoPorNome(nome_camp)
            detalhes_str = client_campeonato.service.detalharCampeonato(camp_id)

            xml_resposta = f"<resposta><return>{camp_id} | {detalhes_str}</return></resposta>"
        elif raiz.find(".//{*}listarJogadoresPorCampeonato") is not None:
            camp_id = int(raiz.find(".//{*}arg0").text)
            jogadores = client_campeonato.service.listarJogadoresPorCampeonato(camp_id)

            tags_return = "".join(f"<return>{j}</return>" for j in (jogadores or []))
            xml_resposta = f"<resposta>{tags_return}</resposta>"

        elif raiz.find(".//{*}inscreverJogador") is not None:
            nome_jogador = raiz.find(".//{*}arg0").text
            camp_id = int(raiz.find(".//{*}arg1").text)
            retorno = client_campeonato.service.inscreverJogador(nome_jogador, camp_id)

            xml_resposta = f"<resposta><return>{retorno}</return></resposta>"
        elif raiz.find(".//{*}gerarChave") is not None:
            camp_id = int(raiz.find(".//{*}arg0").text)
            retorno = client_campeonato.service.gerarChave(camp_id)

            xml_resposta = f"<resposta><return>{retorno}</return></resposta>"
        elif raiz.find(".//{*}exibirChaveamentoPorCampeonato") is not None:
            camp_id = int(raiz.find(".//{*}arg0").text)
            retorno = client_chave.service.exibirChaveamentoPorCampeonato(camp_id)

            tags_return = "".join(f"<return>{p}</return>" for p in retorno)
            xml_resposta = f"<resposta>{tags_return}</resposta>"
        elif raiz.find(".//{*}buscarPartidaPorId") is not None:
            partida_id = int(raiz.find(".//{*}arg0").text)
            retorno = client_partida.service.buscarPartidaPorId(partida_id)

            xml_resposta= f"<resposta><return>{retorno}</return></resposta>"


        self.send_response(200)
        self.send_header("Content-Type", "text/xml; charset=utf-8")
        self._enviar_cors()
        self.end_headers()
        self.wfile.write(xml_resposta.encode("utf-8"))


def run(server_class=HTTPServer, handler_class=BaseHTTPRequestHandler):
    server_address = ('localhost', 8000)
    httpd = server_class(server_address, handler_class)
    httpd.serve_forever()

run(handler_class=Gateway)