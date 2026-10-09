package eduar.studymindredacao.domain.model;

import java.util.UUID;

/**
 * Grande área temática da taxonomia do Repertório (ex.: Saúde, Educação).
 * O slug é o identificador público (filtros e URL); a ordem define a exibição nos filtros.
 */
public record Macroeixo(UUID id, String slug, String nome, int ordem) {
    public static final int TAMANHO_MAXIMO_SLUG = 60;
    public static final int TAMANHO_MAXIMO_NOME = 120;

    public Macroeixo {
        Validacoes.validarSlug(slug, "slug", TAMANHO_MAXIMO_SLUG);
        Validacoes.validarTextoObrigatorio(nome, "nome", TAMANHO_MAXIMO_NOME);
        if (ordem <= 0) {
            throw new IllegalArgumentException("ordem deve ser positiva");
        }
    }
}
