package eduar.studymindredacao.domain.model;

import java.time.OffsetDateTime;
import java.util.Comparator;
import java.util.List;
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
        boolean anulada,
        String motivoAnulacao,
        List<CompetenciaAvaliada> competencias,
        List<String> pontosFortes,
        List<String> pontosDesenvolvimento,
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

        if (anulada && (motivoAnulacao == null || motivoAnulacao.isBlank())) {
            throw new IllegalArgumentException("motivoAnulacao é obrigatório quando a redação é anulada");
        }

        competencias = competencias == null
                ? List.of()
                : competencias.stream().sorted(Comparator.comparingInt(CompetenciaAvaliada::numero)).toList();
        if (!competencias.isEmpty()) {
            validarCompetencias(competencias, notaC1, notaC2, notaC3, notaC4, notaC5);
        }
        pontosFortes = pontosFortes == null ? List.of() : List.copyOf(pontosFortes);
        pontosDesenvolvimento = pontosDesenvolvimento == null ? List.of() : List.copyOf(pontosDesenvolvimento);
    }

    public static Avaliacao de(UUID redacaoId, ResultadoAvaliacaoIA resultado) {
        Validacoes.requererNaoNulo(resultado, "resultado");
        var competencias = resultado.competencias();
        return new Avaliacao(
                null,
                redacaoId,
                notaDa(competencias, 1),
                notaDa(competencias, 2),
                notaDa(competencias, 3),
                notaDa(competencias, 4),
                notaDa(competencias, 5),
                null,
                resultado.anulada(),
                resultado.motivoAnulacao(),
                competencias,
                resultado.pontosFortes(),
                resultado.pontosDesenvolvimento(),
                resultado.diagnostico(),
                resultado.modelo(),
                resultado.tokensEntrada(),
                resultado.tokensSaida(),
                resultado.respostaBrutaJson(),
                null
        );
    }

    private static Short notaDa(List<CompetenciaAvaliada> competencias, int numero) {
        return competencias.stream()
                .filter(c -> c.numero() == numero)
                .findFirst()
                .map(c -> (short) c.nota())
                .orElseThrow(() -> new IllegalArgumentException("competência C" + numero + " ausente"));
    }

    private static void validarCompetencias(List<CompetenciaAvaliada> competencias, Short... notas) {
        if (competencias.size() != 5) {
            throw new IllegalArgumentException("a avaliação deve ter exatamente 5 competências");
        }
        for (int i = 0; i < 5; i++) {
            var competencia = competencias.get(i);
            if (competencia.numero() != i + 1) {
                throw new IllegalArgumentException("as competências devem ser C1 a C5, sem repetição");
            }
            if (competencia.nota() != notas[i]) {
                throw new IllegalArgumentException(
                        "nota da C" + (i + 1) + " (" + competencia.nota() + ") difere de notaC" + (i + 1) + " (" + notas[i] + ")"
                );
            }
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
