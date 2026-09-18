package eduar.studymindredacao.domain.model;



import java.util.List;

public record ResultadoAvaliacaoIA(
        boolean anulada,
        String motivoAnulacao,
        List<CompetenciaAvaliada> competencias,
        List<String> pontosFortes,
        List<String> pontosDesenvolvimento,
        String diagnostico,
        String modelo,
        int tokensEntrada,
        int tokensSaida,
        String respostaBrutaJson) {

    public ResultadoAvaliacaoIA {
        if (competencias == null || competencias.size() != 5) {
            throw new IllegalArgumentException("a avaliação deve conter exatamente 5 competências");
        }
        long distintas = competencias.stream().map(CompetenciaAvaliada::numero).distinct().count();
        if (distintas != 5) {
            throw new IllegalArgumentException("as competências devem ser 1 a 5, sem repetição");
        }
        if (anulada) {
            if (motivoAnulacao == null || motivoAnulacao.isBlank()) {
                throw new IllegalArgumentException("motivoAnulacao é obrigatório quando anulada");
            }
            if (competencias.stream().anyMatch(c -> c.nota() != 0)) {
                throw new IllegalArgumentException("redação anulada deve ter todas as notas zeradas");
            }
        }
        if (tokensEntrada < 0 || tokensSaida < 0) {
            throw new IllegalArgumentException("contagem de tokens não pode ser negativa");
        }
        competencias = List.copyOf(competencias);
        pontosFortes = pontosFortes == null ? List.of() : List.copyOf(pontosFortes);
        pontosDesenvolvimento = pontosDesenvolvimento == null ? List.of() : List.copyOf(pontosDesenvolvimento);
    }

    public int notaTotal() {
        return competencias.stream().mapToInt(CompetenciaAvaliada::nota).sum();
    }
}