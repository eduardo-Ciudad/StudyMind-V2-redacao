package eduar.studymindredacao.domain.model;



import java.util.List;

public record CompetenciaAvaliada(
        int numero,
        int nota,
        String nivelReferencia,
        String resumo,
        List<String> problemasIdentificados) {

    public CompetenciaAvaliada {
        if (numero < 1 || numero > 5) {
            throw new IllegalArgumentException("numero da competência deve estar entre 1 e 5");
        }
        if (nota < 0 || nota > 200 || nota % 40 != 0) {
            throw new IllegalArgumentException(
                    "nota da competência deve ser 0, 40, 80, 120, 160 ou 200 (recebido: " + nota + ")");
        }
        if (resumo == null || resumo.isBlank()) {
            throw new IllegalArgumentException("resumo é obrigatório");
        }
        problemasIdentificados = problemasIdentificados == null
                ? List.of()
                : List.copyOf(problemasIdentificados);
    }
}
