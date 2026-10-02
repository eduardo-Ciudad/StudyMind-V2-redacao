package eduar.studymindredacao.adapter.in.web;

import eduar.studymindredacao.adapter.in.web.dto.CadastroRequest;
import eduar.studymindredacao.adapter.in.web.dto.LoginRequest;
import eduar.studymindredacao.adapter.in.web.dto.TokenResponse;
import eduar.studymindredacao.adapter.in.web.dto.UsuarioResponse;
import eduar.studymindredacao.adapter.in.web.security.LimitadorDeTentativas;
import eduar.studymindredacao.application.usecase.AutenticarUsuarioService;
import eduar.studymindredacao.application.usecase.CadastrarUsuarioService;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

import java.time.Duration;
import java.util.Locale;

@RestController
@RequestMapping("/auth")
public class AuthController {
    static final int LOGIN_POR_IP_POR_MINUTO = 10;
    static final int LOGIN_POR_EMAIL_POR_MINUTO = 5;
    static final int CADASTRO_POR_IP_POR_HORA = 5;

    private final CadastrarUsuarioService cadastrarUsuario;
    private final AutenticarUsuarioService autenticarUsuario;
    private final LimitadorDeTentativas limitador;

    public AuthController(
            CadastrarUsuarioService cadastrarUsuario,
            AutenticarUsuarioService autenticarUsuario,
            LimitadorDeTentativas limitador
    ) {
        this.cadastrarUsuario = cadastrarUsuario;
        this.autenticarUsuario = autenticarUsuario;
        this.limitador = limitador;
    }

    @PostMapping("/cadastro")
    @ResponseStatus(HttpStatus.CREATED)
    public UsuarioResponse cadastrar(@RequestBody @Valid CadastroRequest request, HttpServletRequest http) {
        limitador.consumir("cadastro-ip:" + http.getRemoteAddr(), CADASTRO_POR_IP_POR_HORA, Duration.ofHours(1));
        var usuario = cadastrarUsuario.cadastrar(request.nome(), request.email(), request.senha());
        return UsuarioResponse.de(usuario);
    }

    @PostMapping("/login")
    public TokenResponse login(@RequestBody @Valid LoginRequest request, HttpServletRequest http) {
        // Por IP segura um atacante só; por e-mail protege a conta mesmo com IPs variados
        limitador.consumir("login-ip:" + http.getRemoteAddr(), LOGIN_POR_IP_POR_MINUTO, Duration.ofMinutes(1));
        limitador.consumir("login-email:" + request.email().trim().toLowerCase(Locale.ROOT),
                LOGIN_POR_EMAIL_POR_MINUTO, Duration.ofMinutes(1));
        return TokenResponse.de(autenticarUsuario.autenticar(request.email(), request.senha()));
    }
}
