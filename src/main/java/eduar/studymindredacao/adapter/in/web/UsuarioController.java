package eduar.studymindredacao.adapter.in.web;

import eduar.studymindredacao.adapter.in.web.dto.EvolucaoResponse;
import eduar.studymindredacao.adapter.in.web.dto.UsoDiarioResponse;
import eduar.studymindredacao.adapter.in.web.security.UsuarioAutenticado;
import eduar.studymindredacao.application.usecase.ConsultarEvolucaoService;
import eduar.studymindredacao.application.usecase.ConsultarUsoDiarioService;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/usuarios")
public class UsuarioController {
    private final ConsultarUsoDiarioService consultarUsoDiario;
    private final ConsultarEvolucaoService consultarEvolucao;

    public UsuarioController(ConsultarUsoDiarioService consultarUsoDiario, ConsultarEvolucaoService consultarEvolucao) {
        this.consultarUsoDiario = consultarUsoDiario;
        this.consultarEvolucao = consultarEvolucao;
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

    /** Linha do tempo das notas, média recente por competência e a competência foco. */
    @GetMapping("/me/evolucao")
    public EvolucaoResponse evolucao(@AuthenticationPrincipal UsuarioAutenticado usuario) {
        return EvolucaoResponse.de(consultarEvolucao.consultar(usuario.id()));
    }
}
