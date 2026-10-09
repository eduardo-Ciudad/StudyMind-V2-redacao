package eduar.studymindredacao.domain.model;

/**
 * Notas da rubrica da pesquisa (1 a 5 em cada critério), usadas só para ordenação e curadoria.
 * Não vão na resposta pública da API. O repertório pode não ter pontuação (campo nulo):
 * a pesquisa V3 não trouxe notas para CF, EVD e REP-048 a 062.
 */
public record Pontuacao(
        int versatilidade,
        int autoridade,
        int compreensao,
        int aplicabilidade,
        int especificidade,
        int originalidade
) {
    public static final int NOTA_MINIMA = 1;
    public static final int NOTA_MAXIMA = 5;

    public Pontuacao {
        validarNota(versatilidade, "versatilidade");
        validarNota(autoridade, "autoridade");
        validarNota(compreensao, "compreensao");
        validarNota(aplicabilidade, "aplicabilidade");
        validarNota(especificidade, "especificidade");
        validarNota(originalidade, "originalidade");
    }

    private static void validarNota(int nota, String campo) {
        if (nota < NOTA_MINIMA || nota > NOTA_MAXIMA) {
            throw new IllegalArgumentException(
                    campo + " deve estar entre " + NOTA_MINIMA + " e " + NOTA_MAXIMA
            );
        }
    }
}
