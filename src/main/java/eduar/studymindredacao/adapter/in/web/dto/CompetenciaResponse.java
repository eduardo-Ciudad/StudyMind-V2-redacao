package eduar.studymindredacao.adapter.in.web.dto;

import eduar.studymindredacao.domain.model.CompetenciaAvaliada;

import java.util.List;

public record CompetenciaResponse(
        int numero,
        int nota,
        String nivelReferencia,
        String resumo,
        List<String> problemas
) {
    public static CompetenciaResponse de(CompetenciaAvaliada competencia) {
        return new CompetenciaResponse(
                competencia.numero(),
                competencia.nota(),
                competencia.nivelReferencia(),
                competencia.resumo(),
                competencia.problemasIdentificados()
        );
    }
}
