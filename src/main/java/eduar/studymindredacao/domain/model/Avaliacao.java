package eduar.studymindredacao.domain.model;

import java.time.OffsetDateTime;
import java.util.UUID;

public record Avaliacao(
        UUID id,
        UUID redacaoId,
        Short notaC1,
        Short notaC2,
        Short notaC3,
        Short notaC4,
        Short notaC5,
        Short notaTotal,
        String pontosFortes,
        String pontosDesenvolvimento,
        String diagnostico,
        String modeloIa,
        Integer tokensEntrada,
        Integer tokensSaida,
        String respostaBrutaJson,
        OffsetDateTime avaliadoEm
) {
    public Avaliacao {
        Validacoes.requererNaoNulo(redacaoId, "redacaoId");
        validarNota(notaC1, "notaC1");
        validarNota(notaC2, "notaC2");
        validarNota(notaC3, "notaC3");
        validarNota(notaC4, "notaC4");
        validarNota(notaC5, "notaC5");
        Validacoes.requererNaoNulo(notaTotal, "notaTotal");
        Validacoes.validarTamanhoMaximo(modeloIa, "modeloIa", 50);
    }

    private static void validarNota(Short nota, String campo) {
        Validacoes.requererNaoNulo(nota, campo);
        if (nota < 0 || nota > 200) {
            throw new IllegalArgumentException(campo + " deve estar entre 0 e 200");
        }
    }
}
