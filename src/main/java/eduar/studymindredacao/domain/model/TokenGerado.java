package eduar.studymindredacao.domain.model;

import java.time.Instant;

public record TokenGerado(String token, Instant expiraEm) {
    public TokenGerado {
        Validacoes.validarTextoObrigatorio(token, "token", null);
        Validacoes.requererNaoNulo(expiraEm, "expiraEm");
    }
}
