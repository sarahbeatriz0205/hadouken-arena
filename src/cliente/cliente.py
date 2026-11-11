from zeep import Client

wsdl_campeonato = "http://127.0.0.1:9876/campeonato?wsdl"
wsdl_partida = "http://127.0.0.1:9876/partida?wsdl"
wsdl_chave = "http://127.0.0.1:9876/chave?wsdl"

client_campeonato = Client(wsdl=wsdl_campeonato)
client_partida = Client(wsdl=wsdl_partida)
client_chave = Client(wsdl=wsdl_chave)

resultado = client_campeonato.service.criarCampeonato("Pistão", 4);
print(resultado)
outro_result = client_campeonato.service.inscreverJogador("Ryu", 1)
print(outro_result)