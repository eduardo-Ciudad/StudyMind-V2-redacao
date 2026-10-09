package eduar.studymindredacao.domain.model;

import eduar.studymindredacao.domain.model.enums.Dificuldade;
import eduar.studymindredacao.domain.model.enums.Nivel;

/**
 * Como a pesquisa classificou o repertório: risco de uso errado, dificuldade, saturação e notas da rubrica.
 * Tudo opcional: CF e EVD não trazem essa classificação, e as notas ficam nulas até a pesquisa de prioridade.
 * Risco e saturação vão para o aluno como selo; a pontuação fica só no backend.
 */
public record Curadoria(Nivel riscoUso, Dificuldade dificuldade, Nivel saturacao, Pontuacao pontuacao) {

    public static Curadoria vazia() {
        return new Curadoria(null, null, null, null);
    }

    public boolean temPontuacao() {
        return pontuacao != null;
    }
}
