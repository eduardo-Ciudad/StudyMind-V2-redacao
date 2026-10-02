package eduar.studymindredacao.application.usecase;

import eduar.studymindredacao.domain.exception.TemaNaoAdicionavelException;
import eduar.studymindredacao.domain.exception.TemaNaoEncontradoException;
import eduar.studymindredacao.domain.model.enums.OrigemTema;
import eduar.studymindredacao.domain.port.TemaRepositoryPort;
import eduar.studymindredacao.domain.port.UsuarioTemaRepositoryPort;
import org.springframework.stereotype.Service;

import java.util.UUID;

/**
 * Adiciona e remove temas possíveis da lista do aluno.
 * Remover não afeta redações já enviadas: elas apontam para o tema, não para a lista.
 */
@Service
public class GerenciarMeusTemasService {
    private final TemaRepositoryPort temaRepository;
    private final UsuarioTemaRepositoryPort usuarioTemaRepository;

    public GerenciarMeusTemasService(TemaRepositoryPort temaRepository, UsuarioTemaRepositoryPort usuarioTemaRepository) {
        this.temaRepository = temaRepository;
        this.usuarioTemaRepository = usuarioTemaRepository;
    }

    public void adicionar(UUID usuarioId, UUID temaId) {
        exigirTemaPossivel(temaId, true);
        usuarioTemaRepository.adicionar(usuarioId, temaId);
    }

    public void remover(UUID usuarioId, UUID temaId) {
        exigirTemaPossivel(temaId, false);
        usuarioTemaRepository.remover(usuarioId, temaId);
    }

    /** Para remover, aceita tema inativo: o aluno precisa conseguir tirar da lista um tema que foi desativado. */
    private void exigirTemaPossivel(UUID temaId, boolean exigirAtivo) {
        var tema = temaRepository.buscarPorId(temaId)
                .filter(t -> !exigirAtivo || Boolean.TRUE.equals(t.ativo()))
                .orElseThrow(() -> new TemaNaoEncontradoException(temaId));
        if (tema.origem() != OrigemTema.PREVISAO) {
            throw new TemaNaoAdicionavelException(temaId);
        }
    }
}
