package eduar.studymindredacao.domain.model;

import java.time.OffsetDateTime;
import java.util.Set;
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
    private static final Set<Short> NIVEIS_VALIDOS = Set.of(
            (short) 0, (short) 40, (short) 80, (short) 120, (short) 160, (short) 200
    );

    public Avaliacao {
        Validacoes.requererNaoNulo(redacaoId, "redacaoId");
        validarNota(notaC1, "notaC1");
        validarNota(notaC2, "notaC2");
        validarNota(notaC3, "notaC3");
        validarNota(notaC4, "notaC4");
        validarNota(notaC5, "notaC5");
        Validacoes.validarTamanhoMaximo(modeloIa, "modeloIa", 50);

        short soma = (short) (notaC1 + notaC2 + notaC3 + notaC4 + notaC5);
        if (notaTotal == null) {
            notaTotal = soma;
        } else if (notaTotal != soma) {
            throw new IllegalArgumentException(
                    "notaTotal (" + notaTotal + ") difere da soma das competências (" + soma + ")"
            );
        }
    }

    private static void validarNota(Short nota, String campo) {
        Validacoes.requererNaoNulo(nota, campo);
        if (!NIVEIS_VALIDOS.contains(nota)) {
            throw new IllegalArgumentException(
                    campo + " deve ser um dos níveis oficiais: 0, 40, 80, 120, 160 ou 200"
            );
        }
    }
}
