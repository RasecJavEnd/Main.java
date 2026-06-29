import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

public final class Evento {
    private static final DateTimeFormatter EXIBICAO_FORMATTER =
            DateTimeFormatter.ofPattern("dd/MM/yyyy HH:mm");

    private static final long DURACAO_PADRAO_HORAS = 2;

    private final int id;
    private final String nome;
    private final String endereco;
    private final Categoria categoria;
    private final LocalDateTime horario;
    private final String descricao;
    private final Set<String> participantes;

    public Evento(int id,
                  String nome,
                  String endereco,
                  Categoria categoria,
                  LocalDateTime horario,
                  String descricao) {
        this.id = id;
        this.nome = nome;
        this.endereco = endereco;
        this.categoria = categoria;
        this.horario = horario;
        this.descricao = descricao;
        this.participantes = new HashSet<>();
    }

    public int getId() { return id; }
    public String getNome() { return nome; }
    public String getEndereco() { return endereco; }
    public Categoria getCategoria() { return categoria; }
    public LocalDateTime getHorario() { return horario; }
    public String getDescricao() { return descricao; }
    public int getQuantidadeParticipantes() { return participantes.size(); }

    public boolean estaOcorrendoAgora() {
        LocalDateTime agora = LocalDateTime.now();
        LocalDateTime fim = horario.plusHours(DURACAO_PADRAO_HORAS);
        return (agora.isEqual(horario) || agora.isAfter(horario)) && agora.isBefore(fim);
    }

    public boolean jaOcorreu() {
        return LocalDateTime.now().isAfter(horario.plusHours(DURACAO_PADRAO_HORAS));
    }

    public boolean participa(String email) {
        return participantes.contains(email.toLowerCase());
    }

    public void adicionarParticipante(String email) {
        participantes.add(email.toLowerCase());
    }

    public void removerParticipante(String email) {
        participantes.remove(email.toLowerCase());
    }

    private static String escapar(String texto) {
        return texto.replace("\\", "\\\\").replace("|", "\\|");
    }

    private static List<String> dividirEscapado(String linha) {
        List<String> partes = new ArrayList<>();
        StringBuilder atual = new StringBuilder();
        boolean escape = false;

        for (char c : linha.toCharArray()) {
            if (escape) {
                atual.append(c);
                escape = false;
            } else if (c == '\\') {
                escape = true;
            } else if (c == '|') {
                partes.add(atual.toString());
                atual.setLength(0);
            } else {
                atual.append(c);
            }
        }
        partes.add(atual.toString());
        return partes;
    }

    public String serializar() {
        String participantesTexto = String.join(",", participantes);
        return id + "|" +
                escapar(nome) + "|" +
                escapar(endereco) + "|" +
                categoria.name() + "|" +
                horario.toString() + "|" +
                escapar(descricao) + "|" +
                escapar(participantesTexto);
    }

    public static Evento desserializar(String linha) {
        List<String> partes = dividirEscapado(linha);
        if (partes.size() < 7) {
            return null;
        }

        int id = Integer.parseInt(partes.get(0));
        String nome = partes.get(1);
        String endereco = partes.get(2);
        Categoria categoria = Categoria.valueOf(partes.get(3));
        LocalDateTime horario = LocalDateTime.parse(partes.get(4));
        String descricao = partes.get(5);
        String participantesTexto = partes.get(6);

        Evento evento = new Evento(id, nome, endereco, categoria, horario, descricao);

        if (!participantesTexto.isBlank()) {
            String[] itens = participantesTexto.split(",");
            for (String item : itens) {
                String email = item.trim();
                if (!email.isBlank()) {
                    evento.adicionarParticipante(email);
                }
            }
        }
        return evento;
    }

    @Override
    public String toString() {
        String status;
        if (estaOcorrendoAgora()) {
            status = "OCORRENDO AGORA";
        } else if (jaOcorreu()) {
            status = "JÁ OCORREU";
        } else {
            status = "PRÓXIMO";
        }

        return "ID: " + id +
                "\nNome: " + nome +
                "\nEndereço: " + endereco +
                "\nCategoria: " + categoria +
                "\nHorário: " + horario.format(EXIBICAO_FORMATTER) +
                "\nDescrição: " + descricao +
                "\nParticipantes confirmados: " + getQuantidadeParticipantes() +
                "\nStatus: " + status;
    }
}
