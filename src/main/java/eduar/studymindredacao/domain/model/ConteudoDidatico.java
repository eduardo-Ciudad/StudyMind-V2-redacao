package eduar.studymindredacao.domain.model;

/**
 * Texto pedagógico de um repertório, na ordem em que o aluno lê:
 * o que é, o que lembrar na prova, como usar, um exemplo pronto e o erro a evitar.
 * Todos os campos são opcionais aqui: CF e EVD só têm ideia central e erro comum,
 * e os registros inativos podem estar incompletos. O que é obrigatório para um
 * repertório ativo é validado no próprio Repertorio.
 */
public record ConteudoDidatico(
        String ideiaCentral,
        String lembreNaProva,
        String comoUsar,
        String exemploAplicacao,
        String erroComum
) {
    public ConteudoDidatico {
        ideiaCentral = nuloSeVazio(ideiaCentral);
        lembreNaProva = nuloSeVazio(lembreNaProva);
        comoUsar = nuloSeVazio(comoUsar);
        exemploAplicacao = nuloSeVazio(exemploAplicacao);
        erroComum = nuloSeVazio(erroComum);
    }

    public boolean temIdeiaCentral() {
        return ideiaCentral != null;
    }

    public boolean temExemploAplicacao() {
        return exemploAplicacao != null;
    }

    private static String nuloSeVazio(String valor) {
        return valor == null || valor.isBlank() ? null : valor.strip();
    }
}
