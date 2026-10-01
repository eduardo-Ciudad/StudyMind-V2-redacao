package eduar.studymindredacao.adapter.in.web;

import eduar.studymindredacao.adapter.in.web.dto.UsoDiarioResponse;
import eduar.studymindredacao.adapter.in.web.security.UsuarioAutenticado;
import eduar.studymindredacao.application.usecase.ConsultarUsoDiarioService;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/usuarios")
public class UsuarioController {
    private final ConsultarUsoDiarioService consultarUsoDiario;

    public UsuarioController(ConsultarUsoDiarioService consultarUsoDiario) {
        this.consultarUsoDiario = consultarUsoDiario;
    }

    @GetMapping("/me")
    public UsuarioAutenticado me(@AuthenticationPrincipal UsuarioAutenticado usuario) {
        return usuario;
    }

    /** Saldo de correções de hoje: usadas, limite, restantes e quando o contador zera. */
    @GetMapping("/me/correcoes-hoje")
    public UsoDiarioResponse correcoesHoje(@AuthenticationPrincipal UsuarioAutenticado usuario) {
        return UsoDiarioResponse.de(consultarUsoDiario.consultar(usuario.id()));
    }
}
