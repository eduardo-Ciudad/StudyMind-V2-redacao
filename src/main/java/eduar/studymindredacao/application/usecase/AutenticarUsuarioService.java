package eduar.studymindredacao.application.usecase;

import eduar.studymindredacao.domain.exception.CredenciaisInvalidasException;
import eduar.studymindredacao.domain.model.TokenGerado;
import eduar.studymindredacao.domain.port.SenhaEncoderPort;
import eduar.studymindredacao.domain.port.TokenPort;
import eduar.studymindredacao.domain.port.UsuarioRepositoryPort;
import org.springframework.stereotype.Service;

@Service
public class AutenticarUsuarioService {
    private final UsuarioRepositoryPort usuarioRepository;
    private final SenhaEncoderPort senhaEncoder;
    private final TokenPort tokenPort;

    public AutenticarUsuarioService(
            UsuarioRepositoryPort usuarioRepository,
            SenhaEncoderPort senhaEncoder,
            TokenPort tokenPort
    ) {
        this.usuarioRepository = usuarioRepository;
        this.senhaEncoder = senhaEncoder;
        this.tokenPort = tokenPort;
    }

    public TokenGerado autenticar(String email, String senha) {
        if (email == null || email.isBlank() || senha == null || senha.isEmpty()) {
            throw new CredenciaisInvalidasException();
        }
        // Mesma exceção para e-mail inexistente e senha errada: não revela quais e-mails têm conta
        var usuario = usuarioRepository.buscarPorEmail(email)
                .filter(u -> senhaEncoder.confere(senha, u.senhaHash()))
                .orElseThrow(CredenciaisInvalidasException::new);
        return tokenPort.gerar(usuario);
    }
}
