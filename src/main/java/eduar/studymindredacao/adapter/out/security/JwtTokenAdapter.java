package eduar.studymindredacao.adapter.out.security;

import com.auth0.jwt.JWT;
import com.auth0.jwt.algorithms.Algorithm;
import com.auth0.jwt.exceptions.JWTVerificationException;
import com.auth0.jwt.interfaces.DecodedJWT;
import com.auth0.jwt.interfaces.JWTVerifier;
import eduar.studymindredacao.domain.model.DadosToken;
import eduar.studymindredacao.domain.model.TokenGerado;
import eduar.studymindredacao.domain.model.Usuario;
import eduar.studymindredacao.domain.model.enums.Role;
import eduar.studymindredacao.domain.port.TokenPort;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.time.Clock;
import java.time.Duration;
import java.time.Instant;
import java.util.Objects;
import java.util.Optional;
import java.util.UUID;

@Component
public class JwtTokenAdapter implements TokenPort {
    static final int TAMANHO_MINIMO_SECRET = 32;
    private static final String CLAIM_EMAIL = "email";
    private static final String CLAIM_ROLE = "role";

    private final Algorithm algoritmo;
    private final JWTVerifier verificador;
    private final String issuer;
    private final Duration expiracao;
    private final Clock clock;

    @Autowired
    public JwtTokenAdapter(
            @Value("${security.jwt.secret}") String secret,
            @Value("${security.jwt.issuer}") String issuer,
            @Value("${security.jwt.expiracao-minutos}") long expiracaoMinutos
    ) {
        this(secret, issuer, Duration.ofMinutes(expiracaoMinutos), Clock.systemUTC());
    }

    JwtTokenAdapter(String secret, String issuer, Duration expiracao, Clock clock) {
        if (secret == null || secret.length() < TAMANHO_MINIMO_SECRET) {
            throw new IllegalStateException(
                    "security.jwt.secret (JWT_SECRET) deve ter pelo menos " + TAMANHO_MINIMO_SECRET + " caracteres"
            );
        }
        this.algoritmo = Algorithm.HMAC256(secret);
        this.issuer = Objects.requireNonNull(issuer, "issuer");
        this.expiracao = Objects.requireNonNull(expiracao, "expiracao");
        this.clock = Objects.requireNonNull(clock, "clock");
        this.verificador = JWT.require(algoritmo).withIssuer(issuer).build();
    }

    @Override
    public TokenGerado gerar(Usuario usuario) {
        Objects.requireNonNull(usuario.id(), "usuario precisa estar persistido para gerar token");
        Instant agora = clock.instant();
        Instant expiraEm = agora.plus(expiracao);
        String token = JWT.create()
                .withIssuer(issuer)
                .withSubject(usuario.id().toString())
                .withClaim(CLAIM_EMAIL, usuario.email())
                .withClaim(CLAIM_ROLE, usuario.role().name())
                .withIssuedAt(agora)
                .withExpiresAt(expiraEm)
                .sign(algoritmo);
        return new TokenGerado(token, expiraEm);
    }

    @Override
    public Optional<DadosToken> validar(String token) {
        if (token == null || token.isBlank()) {
            return Optional.empty();
        }
        try {
            DecodedJWT jwt = verificador.verify(token);
            return Optional.of(new DadosToken(
                    UUID.fromString(jwt.getSubject()),
                    jwt.getClaim(CLAIM_EMAIL).asString(),
                    Role.valueOf(jwt.getClaim(CLAIM_ROLE).asString())
            ));
        } catch (JWTVerificationException | IllegalArgumentException | NullPointerException e) {
            return Optional.empty();
        }
    }
}
