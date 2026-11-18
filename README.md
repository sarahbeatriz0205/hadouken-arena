# Hadouken Arena - Gerenciador de Campeonatos de Luta

Projeto acadêmico desenvolvido na disciplina de **Desenvolvimento de Sistemas Distribuídos**, do curso de **Análise e Desenvolvimento de Sistemas (IFRN)** com o objetivo de gerenciar e automatizar o acompanhamento de campeonatos de **Street Fighter II: The World Warrior (SNES)**, eliminando a digitação manual de placares e resultados através da integração direta de um emulador com uma aplicação distribuída em tempo real utilizando WebSocket e protocolo SOAP para operações síncronas.

## O que contém no projeto
- Cliente Python 

##  Estrutura de pastas
```text
HADOUKEN-ARENA/
├── docs/
├── src/
│   ├── cliente/
│   │   ├── ui/
│   │   │   ├── css/
│   │   │   │   ├── forms.css
│   │   │   │   └── index.css
│   │   │   ├── scripts/
│   │   │   │   ├── detalhamento_partida.js
│   │   │   │   ├── detalhamento.js
│   │   │   │   ├── forms.js
│   │   │   │   ├── index.js
│   │   │   │   └── websocket.js
│   │   │   ├── detalhamento_campeonato.html
│   │   │   ├── detalhamento_partida.html
│   │   │   ├── forms.html
│   │   │   └── index.html
│   │   └── gateway.py
│   ├── intermediario/
│   │   ├── src/main/java/
│   │   │   ├── core/
│   │   │   │   └── DadosPartida.java
│   │   │   ├── emulador/
│   │   │   │   ├── leitor.lua
│   │   │   │   └── Listener.java
│   │   │   ├── Manipulador.java
│   │   │   └── ServidorWebsocket.java
│   │   └── pom.xml
│   ├── servidor/
│   │   ├── src/main/java/
│   │   │   ├── core/
│   │   │   │   ├── domain/entities/
│   │   │   │   │   ├── enums/
│   │   │   │   │   │   ├── EstadoCampeonato.java
│   │   │   │   │   │   ├── EstadoPartida.java
│   │   │   │   │   │   ├── Fase.java
│   │   │   │   │   │   └── TipoFim.java
│   │   │   │   │   ├── Campeonato.java
│   │   │   │   │   ├── Chave.java
│   │   │   │   │   ├── Jogador.java
│   │   │   │   │   ├── Partida.java
│   │   │   │   │   └── Round.java
│   │   │   │   └── utils/grafo/
│   │   │   │       ├── Adjacencia.java
│   │   │   │       ├── Aresta.java
│   │   │   │       ├── Grafo.java
│   │   │   │       └── Vertice.java
│   │   │   ├── db/
│   │   │   │   └── Database.java
│   │   │   ├── interfaces/
│   │   │   │   ├── InterfaceCampeonato.java
│   │   │   │   ├── InterfaceChave.java
│   │   │   │   └── InterfacePartida.java
│   │   │   ├── services/
│   │   │   │   ├── CampeonatoService.java
│   │   │   │   ├── ChaveService.java
│   │   │   │   └── PartidaService.java
│   │   │   └── PublicadorTeste.java
│   │   ├── pom.xml
│   │   └── .gitignore
│   └── pom.xml
├── .gitignore
├── LICENSE
└── README.md
```
