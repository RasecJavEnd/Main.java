import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.Scanner;

public final class SistemaEventos {
    private static final String ARQUIVO = "events.data";
    private static final DateTimeFormatter INPUT_FORMATTER =
            DateTimeFormatter.ofPattern("dd/MM/yyyy HH:mm");

    private final List<Evento> eventos;
    private final Scanner scanner;
    private Usuario usuarioAtual;

    public SistemaEventos() {
        this.eventos = new ArrayList<>();
        this.scanner = new Scanner(System.in);
    }

    // Método principal chamado pelo Main
    public void iniciar() {
        carregarEventos();
        cadastrarUsuario();
        menu();
    }

    private void cadastrarUsuario() {
        System.out.println("=== CADASTRO DO USUÁRIO ===");

        System.out.print("Nome: ");
        String nome = scanner.nextLine();

        System.out.print("Email: ");
        String email = scanner.nextLine();

        System.out.print("Cidade: ");
        String cidade = scanner.nextLine();

        System.out.print("Telefone: ");
        String telefone = scanner.nextLine();

        usuarioAtual = new Usuario(nome, email, cidade, telefone);

        System.out.println("\nUsuário cadastrado com sucesso.");
        System.out.println(usuarioAtual);
    }

    private void menu() {
        int opcao;
        do {
            System.out.println("\n=== SISTEMA DE EVENTOS ===");
            System.out.println("Usuário: " + usuarioAtual.getNome() + " | Cidade: " + usuarioAtual.getCidade());
            System.out.println("1 - Cadastrar evento");
            System.out.println("2 - Listar todos os eventos");
            System.out.println("3 - Listar próximos eventos");
            System.out.println("4 - Listar eventos ocorrendo agora");
            System.out.println("5 - Listar eventos passados");
            System.out.println("6 - Confirmar participação");
            System.out.println("7 - Ver eventos confirmados");
            System.out.println("8 - Cancelar participação");
            System.out.println("9 - Salvar eventos");
            System.out.println("0 - Sair");
            System.out.print("Escolha uma opção: ");

            opcao = lerInteiro();

            switch (opcao) {
                case 1 -> cadastrarEvento();
                case 2 -> listarTodosEventos();
                case 3 -> listarProximosEventos();
                case 4 -> listarEventosOcorrendoAgora();
                case 5 -> listarEventosPassados();
                case 6 -> confirmarParticipacao();
                case 7 -> listarEventosConfirmados();
                case 8 -> cancelarParticipacao();
                case 9 -> {
                    salvarEventos();
                    System.out.println("Eventos salvos com sucesso.");
                }
                case 0 -> {
                    salvarEventos();
                    System.out.println("Encerrando o programa.");
                }
                default -> System.out.println("Opção inválida.");
            }
        } while (opcao != 0);
    }

    // Cadastro de evento
    private void cadastrarEvento() {
        System.out.println("\n=== CADASTRO DE EVENTO ===");

        System.out.print("Nome: ");
        String nome = scanner.nextLine();

        System.out.print("Endereço: ");
        String endereco = scanner.nextLine();

        System.out.println("Categorias:");
        Categoria.exibirCategorias();
        System.out.print("Escolha a categoria: ");
        int indiceCategoria = lerInteiro();
        Categoria categoria = Categoria.porIndice(indiceCategoria);

        LocalDateTime horario = lerDataHora();

        System.out.print("Descrição: ");
        String descricao = scanner.nextLine();

        int id = gerarNovoId();
        Evento evento = new Evento(id, nome, endereco, categoria, horario, descricao);

        eventos.add(evento);
        ordenarEventos();
        salvarEventos();

        System.out.println("Evento cadastrado com sucesso.");
    }

    // Listagens
    private void listarTodosEventos() {
        System.out.println("\n=== TODOS OS EVENTOS ===");
        listarEventos(eventos);
    }

    private void listarProximosEventos() {
        System.out.println("\n=== PRÓXIMOS EVENTOS ===");
        List<Evento> proximos = new ArrayList<>();
        for (Evento evento : eventos) {
            if (!evento.jaOcorreu()) {
                proximos.add(evento);
            }
        }
        listarEventos(proximos);
    }

    private void listarEventosOcorrendoAgora() {
        System.out.println("\n=== EVENTOS OCORRENDO AGORA ===");
        List<Evento> atuais = new ArrayList<>();
        for (Evento evento : eventos) {
            if (evento.estaOcorrendoAgora()) {
                atuais.add(evento);
            }
        }
        listarEventos(atuais);
    }

    private void listarEventosPassados() {
        System.out.println("\n=== EVENTOS PASSADOS ===");
        List<Evento> passados = new ArrayList<>();
        for (Evento evento : eventos) {
            if (evento.jaOcorreu()) {
                passados.add(evento);
            }
        }
        listarEventos(passados);
    }

    private void listarEventosConfirmados() {
        System.out.println("\n=== EVENTOS CONFIRMADOS ===");
        List<Evento> confirmados = new ArrayList<>();
        for (Evento evento : eventos) {
            if (evento.participa(usuarioAtual.getEmail())) {
                confirmados.add(evento);
            }
        }
        listarEventos(confirmados);
    }

    private void listarEventos(List<Evento> lista) {
        if (lista.isEmpty()) {
            System.out.println("Nenhum evento encontrado.");
            return;
        }

        List<Evento> copia = new ArrayList<>(lista);
        copia.sort(Comparator.comparing(Evento::getHorario));

        for (Evento evento : copia) {
            System.out.println("\n----------------------------");
            System.out.println(evento);
            if (evento.participa(usuarioAtual.getEmail())) {
                System.out.println("Participação confirmada por você.");
            }
        }
    }

    // Participação
    private void confirmarParticipacao() {
        if (eventos.isEmpty()) {
            System.out.println("Não há eventos cadastrados.");
            return;
        }

        listarTodosEventos();
        System.out.print("\nDigite o ID do evento: ");
        int id = lerInteiro();

        Evento evento = buscarEventoPorId(id);
        if (evento == null) {
            System.out.println("Evento não encontrado.");
            return;
        }

        if (evento.jaOcorreu()) {
            System.out.println("Não é possível participar de um evento já encerrado.");
            return;
        }

        evento.adicionarParticipante(usuarioAtual.getEmail());
        salvarEventos();
        System.out.println("Participação confirmada com sucesso.");
    }

    private void cancelarParticipacao() {
        List<Evento> confirmados = new ArrayList<>();
        for (Evento evento : eventos) {
            if (evento.participa(usuarioAtual.getEmail())) {
                confirmados.add(evento);
            }
        }

        if (confirmados.isEmpty()) {
            System.out.println("Você não possui participações confirmadas.");
            return;
        }

        listarEventos(confirmados);
        System.out.print("\nDigite o ID do evento para cancelar: ");
        int id = lerInteiro();

        Evento evento = buscarEventoPorId(id);
        if (evento == null || !evento.participa(usuarioAtual.getEmail())) {
            System.out.println("Evento inválido.");
            return;
        }

        evento.removerParticipante(usuarioAtual.getEmail());
        salvarEventos();
        System.out.println("Participação cancelada com sucesso.");
    }

    // Utilitários
    private Evento buscarEventoPorId(int id) {
        for (Evento evento : eventos) {
            if (evento.getId() == id) {
                return evento;
            }
        }
        return null;
    }

    private int gerarNovoId() {
        int maiorId = 0;
        for (Evento evento : eventos) {
            if (evento.getId() > maiorId) {
                maiorId = evento.getId();
            }
        }
        return maiorId + 1;
    }

    private void ordenarEventos() {
        eventos.sort(Comparator.comparing(Evento::getHorario));
    }

    private int lerInteiro() {
        while (true) {
            try {
                return Integer.parseInt(scanner.nextLine());
            } catch (NumberFormatException e) {
                System.out.print("Digite um número válido: ");
            }
        }
    }

    private LocalDateTime lerDataHora() {
        while (true) {
            try {
                System.out.print("Horário do evento (dd/MM/yyyy HH:mm): ");
                String entrada = scanner.nextLine();
                return LocalDateTime.parse(entrada, INPUT_FORMATTER);
            } catch (DateTimeParseException e) {
                System.out.println("Formato inválido.");
            }
        }
    }

    // Persistência
    private void salvarEventos() {
        Path caminho = Paths.get(ARQUIVO);
        try (BufferedWriter writer = Files.newBufferedWriter(caminho)) {
            for (Evento evento : eventos) {
                writer.write(evento.serializar());
                writer.newLine();
            }
        } catch (IOException e) {
            System.out.println("Erro ao salvar eventos: " + e.getMessage());
        }
    }

    private void carregarEventos() {
        Path caminho = Paths.get(ARQUIVO);
        if (!Files.exists(caminho)) {
            return;
        }

        try (BufferedReader reader = Files.newBufferedReader(caminho)) {
            String linha;
            while ((linha = reader.readLine()) != null) {
                Evento evento = Evento.desserializar(linha);
                if (evento != null) {
                    eventos.add(evento);
                }
            }
            ordenarEventos();
        } catch (IOException e) {
            System.out.println("Erro ao carregar eventos: " + e.getMessage());
        }
    }
}
