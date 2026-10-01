package eduar.studymindredacao.application.usecase;

import eduar.studymindredacao.domain.model.Avaliacao;

/** Notas (ou médias) das cinco competências; o total é sempre a soma delas. */
public record NotasPorCompetencia(int c1, int c2, int c3, int c4, int c5) {

    static NotasPorCompetencia de(Avaliacao avaliacao) {
        return new NotasPorCompetencia(
                avaliacao.notaC1(), avaliacao.notaC2(), avaliacao.notaC3(), avaliacao.notaC4(), avaliacao.notaC5()
        );
    }

    public int total() {
        return c1 + c2 + c3 + c4 + c5;
    }

    /** Nota da competência pelo número (1 a 5). */
    public int daCompetencia(int numero) {
        return switch (numero) {
            case 1 -> c1;
            case 2 -> c2;
            case 3 -> c3;
            case 4 -> c4;
            case 5 -> c5;
            default -> throw new IllegalArgumentException("competência inválida: " + numero);
        };
    }
}
