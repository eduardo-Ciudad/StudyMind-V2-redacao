package eduar.studymindredacao.adapter.out.security;

import eduar.studymindredacao.domain.port.SenhaEncoderPort;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Component;

@Component
public class BCryptSenhaEncoderAdapter implements SenhaEncoderPort {
    private final BCryptPasswordEncoder encoder = new BCryptPasswordEncoder();

    @Override
    public String codificar(String senhaPura) {
        return encoder.encode(senhaPura);
    }

    @Override
    public boolean confere(String senhaPura, String senhaHash) {
        if (senhaPura == null || senhaHash == null) {
            return false;
        }
        return encoder.matches(senhaPura, senhaHash);
    }
}
