package eduar.studymindredacao.application.usecase;

import eduar.studymindredacao.domain.exception.CredenciaisInvalidasException;
import eduar.studymindredacao.domain.model.DadosToken;
import eduar.studymindredacao.domain.model.TokenGerado;
import eduar.studymindredacao.domain.model.Usuario;
import eduar.studymindredacao.domain.port.TokenPort;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.time.Instant;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

class AutenticarUsuarioServiceTest {
    private AutenticarUsuarioService service;

    private final TokenPort tokenFake = new TokenPort() {
        @Override
        public TokenGerado gerar(Usuario usuario) {
            return new TokenGerado("token-de-" + usuario.email(), Instant.now().plusSeconds(3600));
        }

        @Override
        public Optional<DadosToken> validar(String token) {
            return Optional.empty();
        }
    };

    @BeforeEach
    void setUp() {
        var repository = new UsuarioRepositoryEmMemoria();
        var encoder = new SenhaEncoderFake();
        new CadastrarUsuarioService(repository, encoder, PoliticaCadastro.aberta()).cadastrar("Eduardo", "edu@exemplo.com", "senhaSegura123");
        service = new AutenticarUsuarioService(repository, encoder, tokenFake);
    }

    @Test
    void geraTokenComCredenciaisCorretas() {
        var token = service.autenticar("EDU@exemplo.com", "senhaSegura123");

        assertThat(token.token()).isEqualTo("token-de-edu@exemplo.com");
    }

    @Test
    void rejeitaSenhaErrada() {
        assertThatThrownBy(() -> service.autenticar("edu@exemplo.com", "senhaErrada"))
                .isInstanceOf(CredenciaisInvalidasException.class);
    }

    @Test
    void rejeitaEmailInexistenteComAMesmaExcecao() {
        assertThatThrownBy(() -> service.autenticar("ninguem@exemplo.com", "senhaSegura123"))
                .isInstanceOf(CredenciaisInvalidasException.class);
    }

    @Test
    void rejeitaCamposVazios() {
        assertThatThrownBy(() -> service.autenticar("", ""))
                .isInstanceOf(CredenciaisInvalidasException.class);
    }
}
