package eduar.studymindredacao.domain.model;

import java.util.UUID;

/**
 * Problema social dentro de um macroeixo (ex.: saude-mental em saude).
 * É o elo entre repertórios e, no futuro, os temas de redação.
 * Guarda só o slug do macroeixo: quem precisa do macroeixo completo o busca pelo slug.
 */
public record ProblemaSocial(UUID id, String slug, String nome, String macroeixoSlug) {
    public static final int TAMANHO_MAXIMO_SLUG = 60;
    public static final int TAMANHO_MAXIMO_NOME = 160;

    public ProblemaSocial {
        Validacoes.validarSlug(slug, "slug", TAMANHO_MAXIMO_SLUG);
        Validacoes.validarTextoObrigatorio(nome, "nome", TAMANHO_MAXIMO_NOME);
        Validacoes.validarSlug(macroeixoSlug, "macroeixoSlug", Macroeixo.TAMANHO_MAXIMO_SLUG);
    }
}
