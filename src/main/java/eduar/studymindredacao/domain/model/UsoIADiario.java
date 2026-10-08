package eduar.studymindredacao.domain.model;

import java.time.LocalDate;
import java.util.UUID;

public record UsoIADiario(
        UUID id,
        UUID usuarioId,
        LocalDate data,
        Integer tokensEntrada,
        Integer tokensSaida,
        Integer qtdCorrecoes,
        Integer qtdRoadmaps,
        Integer qtdTranscricoes
) {
    public UsoIADiario {
        Validacoes.requererNaoNulo(usuarioId, "usuarioId");
        Validacoes.requererNaoNulo(data, "data");
        Validacoes.requererNaoNulo(tokensEntrada, "tokensEntrada");
        Validacoes.requererNaoNulo(tokensSaida, "tokensSaida");
        Validacoes.requererNaoNulo(qtdCorrecoes, "qtdCorrecoes");
        Validacoes.requererNaoNulo(qtdRoadmaps, "qtdRoadmaps");
        Validacoes.requererNaoNulo(qtdTranscricoes, "qtdTranscricoes");
    }
}
