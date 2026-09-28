package eduar.studymindredacao.adapter.in.web.dto;

import eduar.studymindredacao.domain.model.TokenGerado;

import java.time.Instant;

public record TokenResponse(String accessToken, String tipo, Instant expiraEm) {
    public static TokenResponse de(TokenGerado token) {
        return new TokenResponse(token.token(), "Bearer", token.expiraEm());
    }
}
