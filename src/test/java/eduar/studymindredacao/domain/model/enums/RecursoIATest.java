package eduar.studymindredacao.domain.model.enums;

import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;

class RecursoIATest {

    @Test
    void cadaRecursoTemOProprioRotuloParaAsMensagensDeLimite() {
        assertThat(RecursoIA.CORRECAO.rotuloPlural()).isEqualTo("correções");
        assertThat(RecursoIA.TRANSCRICAO.rotuloPlural()).isEqualTo("transcrições");
    }

    @Test
    void todoRecursoTemRotuloPreenchido() {
        for (RecursoIA recurso : RecursoIA.values()) {
            assertThat(recurso.rotuloPlural()).as(recurso.name()).isNotBlank();
        }
    }
}