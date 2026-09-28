package eduar.studymindredacao.adapter.in.web;

import eduar.studymindredacao.adapter.in.web.dto.CadastroRequest;
import eduar.studymindredacao.adapter.in.web.dto.LoginRequest;
import eduar.studymindredacao.adapter.in.web.dto.TokenResponse;
import eduar.studymindredacao.adapter.in.web.dto.UsuarioResponse;
import eduar.studymindredacao.application.usecase.AutenticarUsuarioService;
import eduar.studymindredacao.application.usecase.CadastrarUsuarioService;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/auth")
public class AuthController {
    private final CadastrarUsuarioService cadastrarUsuario;
    private final AutenticarUsuarioService autenticarUsuario;

    public AuthController(CadastrarUsuarioService cadastrarUsuario, AutenticarUsuarioService autenticarUsuario) {
        this.cadastrarUsuario = cadastrarUsuario;
        this.autenticarUsuario = autenticarUsuario;
    }

    @PostMapping("/cadastro")
    @ResponseStatus(HttpStatus.CREATED)
    public UsuarioResponse cadastrar(@RequestBody @Valid CadastroRequest request) {
        var usuario = cadastrarUsuario.cadastrar(request.nome(), request.email(), request.senha());
        return UsuarioResponse.de(usuario);
    }

    @PostMapping("/login")
    public TokenResponse login(@RequestBody @Valid LoginRequest request) {
        return TokenResponse.de(autenticarUsuario.autenticar(request.email(), request.senha()));
    }
}
