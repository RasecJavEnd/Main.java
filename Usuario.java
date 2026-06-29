public final class Usuario {
    private final String nome;
    private final String email;
    private final String cidade;
    private final String telefone;

    public Usuario(String nome, String email, String cidade, String telefone) {
        this.nome = nome;
        this.email = email;
        this.cidade = cidade;
        this.telefone = telefone;
    }

    public String getNome() { return nome; }
    public String getEmail() { return email; }
    public String getCidade() { return cidade; }
    public String getTelefone() { return telefone; }

    @Override
    public String toString() {
        return "Nome: " + nome +
                " | Email: " + email +
                " | Cidade: " + cidade +
                " | Telefone: " + telefone;
    }
}
