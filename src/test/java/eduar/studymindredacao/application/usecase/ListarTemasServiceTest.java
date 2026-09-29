package eduar.studymindredacao.application.usecase;

import eduar.studymindredacao.domain.model.Tema;
import eduar.studymindredacao.domain.model.enums.OrigemTema;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;

class ListarTemasServiceTest {

    @Test
    void listaSoTemasAtivos() {
        var temas = new TemaRepositoryEmMemoria();
        temas.salvar(new Tema(null, "Ativo", null, OrigemTema.ENEM_OFICIAL, (short) 2024, true, null));
        temas.salvar(new Tema(null, "Desativado", null, OrigemTema.AUTORAL, null, false, null));

        var ativos = new ListarTemasService(temas).listarAtivos();

        assertThat(ativos).extracting(Tema::titulo).containsExactly("Ativo");
    }
}
