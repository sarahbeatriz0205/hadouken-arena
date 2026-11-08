# Documento de Visão: Hadouken Arena (Gerenciador de campeonatos de Street Fighter II - The World Warrior)

## Histórico de Revisões

| Data | Versão | Descrição | Autores |
| :-: | :-: | :-: | :-: |
| 07/11/2026 | 1 | Versão inicial | Sarah Beatriz |

## 1. Visão Geral do Sistema Proposto

O sistema tem como principal objetivo gerenciar e automatizar o acompanhamento de campeonatos de Street Fighter II: The World Warrior, eliminando a digitação manual de placares e resultados. O jogo roda no emulador BizHawk, e um script Lua lê a memória (vida dos dois lutadores e timer) e repassa as leituras a um programa intermediário, que as converte em eventos (luta iniciada, vida atualizada e round encerrado) e os envia ao servidor por WebSocket. O servidor decide o vencedor de cada round, atualiza a chave do campeonato e distribui as mudanças, em tempo real, aos clientes conectados. O projeto busca simplificar a vida do organizador e permitir que o público acompanhe cada luta enquanto ela acontece.

## 2. Descrição do Problema

|  |  |
| :-- | :-- |
| **Problema** | Atualizar manualmente o placar e a chave durante um campeonato de jogos de luta |
| **Afeta** | Organizadores, que precisam registrar os resultados, e espectadores, que precisam acompanhar o andamento |
| **Impacta** | Em atrasos, erros de placar e na falta de acompanhamento em tempo real |
| **Solução** | Desenvolvimento de um sistema que lê os eventos diretamente do jogo, atualiza a chave automaticamente e publica as mudanças aos clientes em tempo real |

## 3. Descrição dos Usuários

| Usuário | Descrição | Responsabilidades |
| :-: | :-: | :-: |
| **Organizador** | Pessoa responsável pelo campeonato, que usa o painel do cliente | Criar o campeonato e cadastrar  jogadores antes de a chave ser gerada e iniciar as partidas. Também pode acompanhar a chave e os resultados. |
| **Espectador** | Pessoa que acompanha o campeonato, sem necessidade de login | Visualizar a chave, os resultados e a partida em andamento. Não realiza nenhuma alteração no sistema. |
| **Emulador** | Ator de sistema: o conjunto formado pelo emulador BizHawk, o script Lua que lê a memória do jogo e o programa intermediário que envia os eventos ao servidor | Enviar ao servidor os eventos da luta: luta iniciada, vida atualizada e round encerrado (com a vida final dos dois jogadores e o timer). |

## 4. Descrição do Ambiente dos Usuários

| Usuário | Ambiente operacional |
| :-: | :-: |
| **Organizador** | Tem acesso à criação do campeonato, ao cadastro de jogadores, ao início das partidas e a todas as telas de consulta. |
| **Espectador** | Consulta a chave e as partidas e recebe as atualizações em tempo real. Não tem acesso às operações do organizador. |

## 5. Principais Necessidades dos Usuários

1. **Organizadores**
   - Criar um campeonato escolhendo se ele terá 4 ou 8 jogadores;
   - Cadastrar os jogadores no campeonato
   - Iniciar cada partida indicando qual confronto da chave será jogado;
   - Ver o campeonato avançar sozinho até a final.
2. **Espectadores**
   - Acessar o acompanhamento sem login ou cadastro;
   - Ver a chave com os confrontos e os resultados atualizados;
   - Acompanhar a vida dos lutadores e o placar de rounds da partida em andamento;
   - Receber as mudanças na hora, sem precisar recarregar a tela.

## 6. Regras de Negócio

| ID | Regra | Descrição |
| :-: | :-- | :-- |
| RN01 | Capacidade do Campeonato | A capacidade de um campeonato é de 4 ou 8 jogadores, definida na criação e imutável depois disso. |
| RN02 | Cadastro de Jogadores | O organizador cadastra jogadores até o número de inscritos ser igual à capacidade. Depois disso, novos cadastros são recusados. |
| RN03 | Geração Automática da Chave | Quando o último jogador é cadastrado, o sistema sorteia os confrontos e gera a chave automaticamente, uma única vez. |
| RN04 | Aviso de Chave Gerada | Depois que a chave é gravada, o servidor publica um evento via WebSocket aos clientes conectados para que suas telas sejam atualizadas. |
| RN05 | Chave Definitiva | A chave gerada não pode ser refeita nem alterada. Não é possível cadastrar jogadores depois de ela ser gerada. |
| RN06 | Estrutura da Chave | Com 4 jogadores a chave tem Semifinal e Final (3 partidas). Com 8 jogadores tem também as Quartas de final (7 partidas). A eliminação é simples. |
| RN07 | Partida Ativa Única | Só pode existir uma partida ativa (Aguardando emulador ou Em andamento) por vez. Iniciar uma partida enquanto outra está ativa é recusado. |
| RN08 | Início de Partida | O organizador inicia uma partida que esteja Não iniciada e que já tenha os dois jogadores definidos. Ela passa para o estado "Aguardando emulador". |
| RN09 | Confirmação pelo Emulador | Quando o emulador informa que a luta começou, a partida passa de Aguardando emulador para Em andamento. |
| RN10 | Eventos sem Partida Ativa | Eventos recebidos do emulador quando não há partida ativa são ignorados e registrados em um log em arquivo. Nenhuma mudança é publicada aos clientes. |
| RN11 | Atualização de Vida | Cada dano recebido atualiza a vida do round em andamento. O dado é gravado antes de ser publicado aos clientes. |
| RN12 | Finalização de Partida | Uma partida é disputada em melhor de 3 rounds. Quando um jogador vence o número de rounds necessário, seja no segundo ou no terceiro round, a partida passa para Finalizada e o vencedor é registrado. |
| RN13 | Avanço na Chave | Ao finalizar uma partida, o vencedor é colocado na partida seguinte. A finalização e o avanço ocorrem na mesma transação. |
| RN14 | Resultado Definitivo | O resultado informado pelo emulador é definitivo. O sistema não oferece correção manual de resultados. |
| RN15 | Partida sem Cancelamento | Não há operação para cancelar uma partida em Aguardando emulador ou Em andamento. Se ela travar, o campeonato fica bloqueado e a recuperação exige intervenção direta no banco de dados. |
| RN16 | Encerramento do Campeonato | Quando a Final é finalizada, o campeonato passa para o estado Encerrado e nenhuma outra partida pode ser iniciada. |
| RN17 | Visibilidade | A chave, os resultados e a partida em andamento podem ser consultados por qualquer espectador, sem login. |
| RN18 | Vencedor do Round | Ao receber o fim de um round, o servidor decide o vencedor comparando a vida final dos dois jogadores: quem tem mais vida vence, seja por KO (um dos jogadores com vida zero) ou por tempo esgotado. Vidas iguais resultam em empate no round. O fim da partida também é decidido pelo servidor, quando um jogador atinge o número de rounds necessário. |

## 7. Requisitos Funcionais

| Código | Nome | Descrição | Prioridade |
| :-: | :-- | :-- | :-: |
| RF01 | Criação de campeonato | Permitir que o organizador crie um campeonato informando nome, data e capacidade (4 ou 8 jogadores). | Alta |
| RF02 | Cadastro de jogador | Permitir que o organizador cadastre jogadores em um campeonato até ele lotar. | Alta |
| RF03 | Geração da chave | Gerar automaticamente a chave, com sorteio dos confrontos, quando o último jogador é cadastrado. | Alta |
| RF04 | Início de partida | Permitir que o organizador inicie uma partida da chave, colocando-a em Aguardando emulador. | Alta |
| RF05 | Recebimento de eventos | Receber do emulador os eventos de luta iniciada, vida atualizada e round encerrado (com a vida final dos dois jogadores e o timer). | Alta |
| RF06 | Atualização de round e placar | Atualizar a vida do round a cada dano e somar os rounds ganhos de cada jogador quando um round termina, decidindo o vencedor do round pela vida final dos dois jogadores. | Alta |
| RF07 | Finalização e avanço | Finalizar a partida, registrar o vencedor e avançar o vencedor na chave. | Alta |
| RF08 | Consulta da chave | Permitir que usuários consultem a chave e os resultados de um campeonato. | Alta |
| RF09 | Consulta da partida ativa | Permitir que usuários consultem o estado atual da partida em andamento, inclusive ao conectar no meio da luta. | Alta |
| RF10 | Acompanhamento em tempo real | Publicar aos clientes conectados, por WebSocket, os eventos de chave gerada, luta iniciada, vida atualizada, round encerrado e partida encerrada. | Alta |
| RF11 | Registro de eventos ignorados | Registrar em um log em arquivo os eventos do emulador recebidos sem partida ativa. | Baixa |
| RF12 | Listagem de campeonatos | Permitir que usuários consultem a lista de campeonatos para escolher qual deles abrir. | Média |

## 8. Requisitos Não-funcionais

| Código | Nome | Descrição | Categoria | Classificação |
| :-: | :-- | :-- | :-- | :-: |
| RNF01 | Tempo Real | As atualizações de vida e de placar devem chegar às telas abertas em até 1 segundo após o evento no jogo. | Performance | Desejável |
| RNF02 | Consistência | A finalização de uma partida e o avanço na chave devem ocorrer em uma única transação. O mesmo vale para o cadastro do último jogador e a geração da chave. | Confiabilidade | Obrigatório |
| RNF03 | Gravar antes de Publicar | Nenhuma mudança deve ser publicada aos clientes antes de ser gravada no banco de dados. | Confiabilidade | Obrigatório |
| RNF04 | Interoperabilidade | Servidor em Java (Jakarta XML Web Services) e cliente em C# (WCF em .NET 10) devem se comunicar por SOAP, a partir de um contrato WSDL/XSD compartilhado. | Restrição de projeto | Obrigatório |
| RNF05 | Canal em Tempo Real | As atualizações em tempo real devem trafegar por WebSocket, do programa intermediário ao servidor e do servidor aos clientes, estruturadas como envelopes SOAP. | Restrição de projeto | Obrigatório |
| RNF06 | Persistência | Os dados do campeonato devem ser armazenados em SQLite. | Implementação | Obrigatório |
| RNF07 | Recuperação de Estado | Um cliente que conecta no meio de uma partida deve obter o estado atual por consulta ao serviço e passar a receber as atualizações pelo WebSocket. | Confiabilidade | Desejável |
| RNF08 | Mapeamento do Jogo | A leitura da memória do jogo deve ser feita para a versão específica da ROM utilizada. | Restrição de projeto | Obrigatório |