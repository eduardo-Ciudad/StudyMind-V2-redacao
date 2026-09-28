package eduar.studymindredacao.domain.port;

import eduar.studymindredacao.domain.model.DadosToken;
import eduar.studymindredacao.domain.model.TokenGerado;
import eduar.studymindredacao.domain.model.Usuario;

import java.util.Optional;

public interface TokenPort {
    TokenGerado gerar(Usuario usuario);

    Optional<DadosToken> validar(String token);
}
