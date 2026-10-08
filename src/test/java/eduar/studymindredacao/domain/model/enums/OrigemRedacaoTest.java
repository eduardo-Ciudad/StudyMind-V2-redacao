package eduar.studymindredacao.domain.model.enums;

import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;

class OrigemRedacaoTest {


    @Test
    void valoresBatemComOCheckDoBanco() {
        assertThat(OrigemRedacao.values())
                .extracting(Enum::name)
                .containsExactly("DIGITADO", "MANUSCRITO");
    }
}