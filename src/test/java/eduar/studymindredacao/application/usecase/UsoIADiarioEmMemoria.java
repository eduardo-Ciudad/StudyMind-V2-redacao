package eduar.studymindredacao.application.usecase;

import eduar.studymindredacao.domain.model.UsoIADiario;
import eduar.studymindredacao.domain.port.UsoIADiarioRepositoryPort;

import java.time.LocalDate;
import java.util.HashMap;
import java.util.Map;
import java.util.Optional;
import java.util.UUID;

/** Imita as queries atômicas do adapter real (sem a parte de concorrência, que é do Postgres). */
class UsoIADiarioEmMemoria implements UsoIADiarioRepositoryPort {
    private record Chave(UUID usuarioId, LocalDate data) {
    }

    private final Map<Chave, UsoIADiario> usos = new HashMap<>();

    @Override
    public UsoIADiario salvar(UsoIADiario uso) {
        usos.put(new Chave(uso.usuarioId(), uso.data()), uso);
        return uso;
    }

    @Override
    public Optional<UsoIADiario> buscarPorId(UUID id) {
        return usos.values().stream().filter(u -> id.equals(u.id())).findFirst();
    }

    @Override
    public Optional<UsoIADiario> buscarPorUsuarioIdEData(UUID usuarioId, LocalDate data) {
        return Optional.ofNullable(usos.get(new Chave(usuarioId, data)));
    }

    @Override
    public void excluirPorId(UUID id) {
        usos.values().removeIf(u -> id.equals(u.id()));
    }

    @Override
    public boolean reservarCorrecao(UUID usuarioId, LocalDate data, int limite) {
        var atual = usoDoDia(usuarioId, data);
        if (limite <= 0 || atual.qtdCorrecoes() >= limite) {
            return false;
        }
        salvar(new UsoIADiario(atual.id(), usuarioId, data, atual.tokensEntrada(), atual.tokensSaida(),
                atual.qtdCorrecoes() + 1, atual.qtdRoadmaps()));
        return true;
    }

    @Override
    public void liberarCorrecao(UUID usuarioId, LocalDate data) {
        var atual = usoDoDia(usuarioId, data);
        salvar(new UsoIADiario(atual.id(), usuarioId, data, atual.tokensEntrada(), atual.tokensSaida(),
                Math.max(atual.qtdCorrecoes() - 1, 0), atual.qtdRoadmaps()));
    }

    @Override
    public int somarCorrecoesDoDia(LocalDate data) {
        return usos.values().stream().filter(u -> data.equals(u.data())).mapToInt(UsoIADiario::qtdCorrecoes).sum();
    }

    @Override
    public void registrarTokens(UUID usuarioId, LocalDate data, int tokensEntrada, int tokensSaida) {
        var atual = usoDoDia(usuarioId, data);
        salvar(new UsoIADiario(atual.id(), usuarioId, data, atual.tokensEntrada() + tokensEntrada,
                atual.tokensSaida() + tokensSaida, atual.qtdCorrecoes(), atual.qtdRoadmaps()));
    }

    UsoIADiario usoDoDia(UUID usuarioId, LocalDate data) {
        return buscarPorUsuarioIdEData(usuarioId, data)
                .orElse(new UsoIADiario(UUID.randomUUID(), usuarioId, data, 0, 0, 0, 0));
    }
}
