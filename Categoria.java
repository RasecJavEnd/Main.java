public enum Categoria {
    FESTA,
    ESPORTIVO,
    SHOW,
    FEIRA,
    PALESTRA,
    TEATRO,
    OUTRO;

    public static void exibirCategorias() {
        Categoria[] categorias = Categoria.values();
        for (int i = 0; i < categorias.length; i++) {
            System.out.println((i + 1) + " - " + categorias[i]);
        }
    }

    public static Categoria porIndice(int indice) {
        Categoria[] categorias = Categoria.values();
        if (indice < 1 || indice > categorias.length) {
            return OUTRO;
        }
        return categorias[indice - 1];
    }
}
