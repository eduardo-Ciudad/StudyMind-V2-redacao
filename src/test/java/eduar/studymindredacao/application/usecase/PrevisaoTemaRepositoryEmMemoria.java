package eduar.studymindredacao.application.usecase;

import eduar.studymindredacao.domain.model.PrevisaoTema;
import eduar.studymindredacao.domain.port.PrevisaoTemaRepositoryPort;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;

class PrevisaoTemaRepositoryEmMemoria implements PrevisaoTemaRepositoryPort {
    private final List<PrevisaoTema> previsoes = new ArrayList<>();

    void salvar(PrevisaoTema previsao) {
        previsoes.add(previsao);
    }

    @Override
    public List<PrevisaoTema> listarPorRanking() {
        return previsoes.stream().sorted(Comparator.comparingInt(PrevisaoTema::ranking)).toList();
    }
}
