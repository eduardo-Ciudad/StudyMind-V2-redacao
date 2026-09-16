package eduar.studymindredacao.domain.model;

import java.util.Objects;

public final class Validacoes {
    private Validacoes() {
    }

    public static void validarTextoObrigatorio(String valor, String campo, Integer tamanhoMaximo) {
        if (valor == null || valor.isBlank()) {
            throw new IllegalArgumentException(campo + " é obrigatório");
        }
        validarTamanhoMaximo(valor, campo, tamanhoMaximo);
    }

    public static <T> T requererNaoNulo(T valor, String campo) {
        return Objects.requireNonNull(valor, campo + " é obrigatório");
    }

    public static void validarTamanhoMaximo(String valor, String campo, Integer tamanhoMaximo) {
        if (valor != null && tamanhoMaximo != null && valor.length() > tamanhoMaximo) {
            throw new IllegalArgumentException(
                    campo + " deve ter no máximo " + tamanhoMaximo + " caracteres"
            );
        }
    }
}
