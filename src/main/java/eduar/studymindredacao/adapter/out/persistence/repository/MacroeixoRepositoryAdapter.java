package eduar.studymindredacao.adapter.out.persistence.repository;

import eduar.studymindredacao.adapter.out.persistence.mapper.TaxonomiaPersistenceMapper;
import eduar.studymindredacao.domain.model.Macroeixo;
import eduar.studymindredacao.domain.model.ProblemaSocial;
import eduar.studymindredacao.domain.port.MacroeixoRepositoryPort;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Repository
public class MacroeixoRepositoryAdapter implements MacroeixoRepositoryPort {
    private final MacroeixoJpaRepository macroeixos;
    private final ProblemaSocialJpaRepository problemas;

    public MacroeixoRepositoryAdapter(MacroeixoJpaRepository macroeixos, ProblemaSocialJpaRepository problemas) {
        this.macroeixos = macroeixos;
        this.problemas = problemas;
    }

    @Override
    @Transactional(readOnly = true)
    public List<Macroeixo> listarPorOrdem() {
        return macroeixos.findAllByOrderByOrdemAsc().stream()
                .map(TaxonomiaPersistenceMapper::toDomain)
                .toList();
    }

    @Override
    @Transactional(readOnly = true)
    public List<ProblemaSocial> listarProblemas() {
        return problemas.listarComMacroeixo().stream()
                .map(TaxonomiaPersistenceMapper::toDomain)
                .toList();
    }
}
