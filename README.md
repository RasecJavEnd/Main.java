Sistema de Eventos em Java

📌 Visão Geral

Este projeto é um sistema de gerenciamento de eventos desenvolvido em Java. Ele permite que usuários cadastrem eventos, confirmem participação, visualizem eventos futuros, em andamento ou já ocorridos, e gerenciem suas inscrições. O sistema também salva os dados em arquivo (events.data), garantindo persistência entre execuções.

🛠️ Funcionalidades Principais

Cadastro de Usuário: Nome, e-mail, cidade e telefone.

Cadastro de Eventos: Cada evento possui ID único, nome, endereço, categoria, data/horário e descrição.

Gerenciamento de Participação:

Confirmar presença em eventos futuros

Cancelar participação

Listar eventos confirmados pelo usuário

Classificação de Eventos:

Próximos eventos

Eventos ocorrendo agora

Eventos já encerrados

Persistência de Dados:

Eventos salvos em arquivo (events.data)

Carregamento automático ao iniciar o programa

📂 Estrutura do Código

🔹 Categoria

Enum que define os tipos de eventos disponíveis. Inclui métodos para exibir categorias e selecionar por índice.

🔹 Usuario

Classe imutável que representa o usuário do sistema, com atributos como nome, e-mail, cidade e telefone.

🔹 Evento

Classe que representa um evento. Principais funcionalidades:

Serialização e desserialização para salvar/carregar eventos

Controle de participantes (adicionar/remover)

Verificação de status do evento: Ocorrendo agora, Já ocorreu, Próximo

🔹 SistemaEventos

Classe principal que gerencia toda a lógica do sistema:

Cadastro de usuário

Menu interativo

Listagem e ordenação de eventos

Confirmação/cancelamento de participação

Persistência em arquivo

🔹 Main

Classe de entrada do programa, responsável por iniciar o sistema.

📖 Fluxo de Execução

O programa inicia carregando os eventos salvos.

Solicita o cadastro do usuário.

Exibe o menu principal com opções:

Cadastrar evento

Listar eventos (todos, próximos, em andamento, passados)

Confirmar ou cancelar participação

Salvar eventos

Sair do sistema

O usuário interage com o sistema via console.

💡 Destaques Técnicos

Uso de coleções Java (List, Set) para armazenar eventos e participantes.

Manipulação de datas e horários com LocalDateTime e DateTimeFormatter.

Persistência simples com arquivos de texto (BufferedReader, BufferedWriter).
