package eduar.studymindredacao.application.usecase;

import eduar.studymindredacao.domain.exception.EmailJaCadastradoException;
import eduar.studymindredacao.domain.model.enums.Role;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

class CadastrarUsuarioServiceTest {
    private UsuarioRepositoryEmMemoria repository;
    private CadastrarUsuarioService service;

    @BeforeEach
    void setUp() {
        repository = new UsuarioRepositoryEmMemoria();
        service = new CadastrarUsuarioService(repository, new SenhaEncoderFake());
    }

    @Test
    void cadastraAlunoComSenhaCodificadaEEmailNormalizado() {
        var usuario = service.cadastrar("  Eduardo  ", "  Edu@Exemplo.COM ", "senhaSegura123");

        assertThat(usuario.id()).isNotNull();
        assertThat(usuario.nome()).isEqualTo("Eduardo");
        assertThat(usuario.email()).isEqualTo("edu@exemplo.com");
        assertThat(usuario.senhaHash()).isEqualTo("hash:senhaSegura123");
        assertThat(usuario.role()).isEqualTo(Role.ALUNO);
    }

    @Test
    void rejeitaEmailJaCadastradoIgnorandoMaiusculas() {
        service.cadastrar("Eduardo", "edu@exemplo.com", "senhaSegura123");

        assertThatThrownBy(() -> service.cadastrar("Outro", "EDU@exemplo.com", "outraSenha123"))
                .isInstanceOf(EmailJaCadastradoException.class);
    }

    @Test
    void rejeitaSenhaCurta() {
        assertThatThrownBy(() -> service.cadastrar("Eduardo", "edu@exemplo.com", "1234567"))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessageContaining("senha");
    }

    @Test
    void rejeitaSenhaAcimaDoLimiteDoBcrypt() {
        String senhaLonga = "a".repeat(73);

        assertThatThrownBy(() -> service.cadastrar("Eduardo", "edu@exemplo.com", senhaLonga))
                .isInstanceOf(IllegalArgumentException.class);
    }
}
