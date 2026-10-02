package eduar.studymindredacao.adapter.in.web;

import eduar.studymindredacao.adapter.in.web.dto.EvolucaoResponse;
import eduar.studymindredacao.adapter.in.web.dto.UsoDiarioResponse;
import eduar.studymindredacao.adapter.in.web.security.UsuarioAutenticado;
import eduar.studymindredacao.application.usecase.ConsultarEvolucaoService;
import eduar.studymindredacao.application.usecase.ConsultarUsoDiarioService;
import eduar.studymindredacao.application.usecase.GerenciarMeusTemasService;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

import java.util.UUID;

@RestController
@RequestMapping("/usuarios")
public class UsuarioController {
    private final ConsultarUsoDiarioService consultarUsoDiario;
    private final ConsultarEvolucaoService consultarEvolucao;
    private final GerenciarMeusTemasService gerenciarMeusTemas;

    public UsuarioController(
            ConsultarUsoDiarioService consultarUsoDiario,
            ConsultarEvolucaoService consultarEvolucao,
            GerenciarMeusTemasService gerenciarMeusTemas
    ) {
        this.consultarUsoDiario = consultarUsoDiario;
        this.consultarEvolucao = consultarEvolucao;
        this.gerenciarMeusTemas = gerenciarMeusTemas;
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

    /** Adiciona um tema possível à lista do aluno. Idempotente. */
    @PutMapping("/me/temas/{temaId}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void adicionarTema(@AuthenticationPrincipal UsuarioAutenticado usuario, @PathVariable UUID temaId) {
        gerenciarMeusTemas.adicionar(usuario.id(), temaId);
    }

    /** Remove um tema possível da lista do aluno. Idempotente; não afeta redações já enviadas. */
    @DeleteMapping("/me/temas/{temaId}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void removerTema(@AuthenticationPrincipal UsuarioAutenticado usuario, @PathVariable UUID temaId) {
        gerenciarMeusTemas.remover(usuario.id(), temaId);
    }
}
