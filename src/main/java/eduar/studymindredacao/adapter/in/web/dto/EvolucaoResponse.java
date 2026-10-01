package eduar.studymindredacao.adapter.in.web.dto;

import eduar.studymindredacao.application.usecase.Evolucao;
import eduar.studymindredacao.application.usecase.NotasPorCompetencia;
import eduar.studymindredacao.application.usecase.PontoEvolucao;

import java.time.OffsetDateTime;
import java.util.List;
import java.util.UUID;

public record EvolucaoResponse(
        int totalCorrigidas,
        Integer melhorNota,
        MediaResponse mediaRecente,
        Integer competenciaFoco,
        List<PontoResponse> serie
) {
    public record NotasResponse(int c1, int c2, int c3, int c4, int c5) {
        static NotasResponse de(NotasPorCompetencia notas) {
            return notas == null ? null : new NotasResponse(notas.c1(), notas.c2(), notas.c3(), notas.c4(), notas.c5());
        }
    }

    public record MediaResponse(int c1, int c2, int c3, int c4, int c5, int total) {
        static MediaResponse de(NotasPorCompetencia media) {
            return media == null
                    ? null
                    : new MediaResponse(media.c1(), media.c2(), media.c3(), media.c4(), media.c5(), media.total());
        }
    }

    public record PontoResponse(
            UUID redacaoId,
            OffsetDateTime enviadaEm,
            String temaTitulo,
            boolean anulada,
            int notaTotal,
            NotasResponse notas
    ) {
        static PontoResponse de(PontoEvolucao ponto) {
            return new PontoResponse(
                    ponto.redacaoId(),
                    ponto.enviadaEm(),
                    ponto.temaTitulo(),
                    ponto.anulada(),
                    ponto.notaTotal(),
                    NotasResponse.de(ponto.notas())
            );
        }
    }

    public static EvolucaoResponse de(Evolucao evolucao) {
        return new EvolucaoResponse(
                evolucao.totalCorrigidas(),
                evolucao.melhorNota(),
                MediaResponse.de(evolucao.mediaRecente()),
                evolucao.competenciaFoco(),
                evolucao.serie().stream().map(PontoResponse::de).toList()
        );
    }
}
