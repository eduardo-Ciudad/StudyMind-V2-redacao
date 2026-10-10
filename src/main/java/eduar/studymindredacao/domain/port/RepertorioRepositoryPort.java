package eduar.studymindredacao.domain.port;

import eduar.studymindredacao.domain.model.FiltroRepertorio;
import eduar.studymindredacao.domain.model.Pagina;
import eduar.studymindredacao.domain.model.Repertorio;

import java.util.Optional;

/** Repertórios (só leitura: o conteúdo entra pelas migrations de seed). Só devolve registros ativos. */
public interface RepertorioRepositoryPort {
    /** Página de repertórios ativos que atendem a todos os filtros, em ordem alfabética do nome. */
    Pagina<Repertorio> buscar(FiltroRepertorio filtro);

    /** Repertório ativo pelo código (ex.: REP-025). Inativo ou inexistente: vazio. */
    Optional<Repertorio> buscarAtivoPorCodigo(String codigo);
}
