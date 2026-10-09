package eduar.studymindredacao.domain.model;

import eduar.studymindredacao.domain.model.enums.TipoEvidencia;

import java.util.List;

/**
 * Dados de um registro do tipo evidência: natureza do dado, população estudada e ano.
 * O ano é opcional (ex.: "atualização contínua" na pesquisa); quando existe, o front
 * usa para avisar que o dado não é recente.
 */
public record DadosEvidencia(List<TipoEvidencia> tipos, String populacao, Integer ano) {
    public static final int ANO_MINIMO = 1900;
    public static final int ANO_MAXIMO = 2100;

    public DadosEvidencia {
        tipos = tipos == null ? List.of() : List.copyOf(tipos);
        populacao = populacao == null || populacao.isBlank() ? null : populacao.strip();
        if (ano != null && (ano < ANO_MINIMO || ano > ANO_MAXIMO)) {
            throw new IllegalArgumentException("ano deve estar entre " + ANO_MINIMO + " e " + ANO_MAXIMO);
        }
    }

    public static DadosEvidencia vazio() {
        return new DadosEvidencia(List.of(), null, null);
    }

    public boolean temTipo() {
        return !tipos.isEmpty();
    }
}
