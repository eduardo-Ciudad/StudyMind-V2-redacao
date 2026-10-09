package eduar.studymindredacao.domain.model;

import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;

class ConteudoDidaticoTest {

    @Test
    void textoEmBrancoViraNulo() {
        var conteudo = new ConteudoDidatico("Ideia", "  ", "", null, "Erro");

        assertThat(conteudo.lembreNaProva()).isNull();
        assertThat(conteudo.comoUsar()).isNull();
        assertThat(conteudo.exemploAplicacao()).isNull();
    }

    @Test
    void removeEspacosNasPontas() {
        var conteudo = new ConteudoDidatico("  Ideia central  ", null, null, null, null);

        assertThat(conteudo.ideiaCentral()).isEqualTo("Ideia central");
    }

    @Test
    void indicaSeTemIdeiaCentralEExemplo() {
        var completo = new ConteudoDidatico("Ideia", null, null, "Exemplo", null);
        var vazio = new ConteudoDidatico(null, null, null, null, null);

        assertThat(completo.temIdeiaCentral()).isTrue();
        assertThat(completo.temExemploAplicacao()).isTrue();
        assertThat(vazio.temIdeiaCentral()).isFalse();
        assertThat(vazio.temExemploAplicacao()).isFalse();
    }
}
