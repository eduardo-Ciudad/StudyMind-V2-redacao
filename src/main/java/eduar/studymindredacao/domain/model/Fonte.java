package eduar.studymindredacao.domain.model;

import eduar.studymindredacao.domain.model.enums.TipoFonte;

import java.util.Locale;

/**
 * Fonte de um repertório. A url é opcional: algumas evidências da pesquisa só citam a fonte pelo nome.
 * Quando existe, precisa ser http(s), porque o frontend a exibe como link.
 */
public record Fonte(TipoFonte tipo, String descricao, String url) {
    public static final int TAMANHO_MAXIMO_URL = 500;

    public Fonte {
        Validacoes.requererNaoNulo(tipo, "tipo");
        Validacoes.validarTextoObrigatorio(descricao, "descricao", null);
        if (url != null && url.isBlank()) {
            url = null;
        }
        if (url != null) {
            url = url.strip();
            Validacoes.validarTamanhoMaximo(url, "url", TAMANHO_MAXIMO_URL);
            var minuscula = url.toLowerCase(Locale.ROOT);
            if (!minuscula.startsWith("https://") && !minuscula.startsWith("http://")) {
                throw new IllegalArgumentException("url deve começar com http:// ou https://");
            }
        }
    }
}
