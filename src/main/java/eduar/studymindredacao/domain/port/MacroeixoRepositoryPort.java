package eduar.studymindredacao.domain.port;

import eduar.studymindredacao.domain.model.Macroeixo;
import eduar.studymindredacao.domain.model.ProblemaSocial;

import java.util.List;

/** Taxonomia do Repertório (só leitura: o conteúdo entra pelas migrations de seed). */
public interface MacroeixoRepositoryPort {
    /** Macroeixos na ordem de exibição. */
    List<Macroeixo> listarPorOrdem();

    /** Todos os problemas sociais, na ordem do macroeixo e depois por nome. */
    List<ProblemaSocial> listarProblemas();
}
