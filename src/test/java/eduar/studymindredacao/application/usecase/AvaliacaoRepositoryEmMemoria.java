package eduar.studymindredacao.application.usecase;

import eduar.studymindredacao.domain.model.Avaliacao;
import eduar.studymindredacao.domain.port.AvaliacaoRepositoryPort;

import java.time.OffsetDateTime;
import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.UUID;

class AvaliacaoRepositoryEmMemoria implements AvaliacaoRepositoryPort {
    private final Map<UUID, Avaliacao> avaliacoes = new HashMap<>();

    @Override
    public Avaliacao salvar(Avaliacao a) {
        UUID id = a.id() != null ? a.id() : UUID.randomUUID();
        var salva = new Avaliacao(id, a.redacaoId(), a.notaC1(), a.notaC2(), a.notaC3(), a.notaC4(), a.notaC5(),
                a.notaTotal(), a.anulada(), a.motivoAnulacao(), a.competencias(), a.pontosFortes(),
                a.pontosDesenvolvimento(), a.diagnostico(), a.modeloIa(), a.tokensEntrada(), a.tokensSaida(),
                a.respostaBrutaJson(), a.avaliadoEm() != null ? a.avaliadoEm() : OffsetDateTime.now());
        avaliacoes.put(id, salva);
        return salva;
    }

    @Override
    public Optional<Avaliacao> buscarPorId(UUID id) {
        return Optional.ofNullable(avaliacoes.get(id));
    }

    @Override
    public Optional<Avaliacao> buscarPorRedacaoId(UUID redacaoId) {
        return avaliacoes.values().stream().filter(a -> a.redacaoId().equals(redacaoId)).findFirst();
    }

    @Override
    public List<Avaliacao> buscarPorRedacaoIds(Collection<UUID> redacaoIds) {
        return avaliacoes.values().stream().filter(a -> redacaoIds.contains(a.redacaoId())).toList();
    }

    @Override
    public void excluirPorId(UUID id) {
        avaliacoes.remove(id);
    }

    List<Avaliacao> todas() {
        return List.copyOf(avaliacoes.values());
    }
}
