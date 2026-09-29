package eduar.studymindredacao.adapter.in.web;

import eduar.studymindredacao.adapter.in.web.dto.EnviarRedacaoRequest;
import eduar.studymindredacao.adapter.in.web.dto.RedacaoResponse;
import eduar.studymindredacao.adapter.in.web.dto.RedacaoResumoResponse;
import eduar.studymindredacao.adapter.in.web.security.UsuarioAutenticado;
import eduar.studymindredacao.application.usecase.ConsultarRedacoesService;
import eduar.studymindredacao.application.usecase.EnviarRedacaoService;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/redacoes")
public class RedacaoController {
    private final EnviarRedacaoService enviarRedacao;
    private final ConsultarRedacoesService consultarRedacoes;

    public RedacaoController(EnviarRedacaoService enviarRedacao, ConsultarRedacoesService consultarRedacoes) {
        this.enviarRedacao = enviarRedacao;
        this.consultarRedacoes = consultarRedacoes;
    }

    /** Envia a redação e devolve já corrigida (fluxo síncrono, pode levar alguns segundos). */
    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public RedacaoResponse enviar(
            @AuthenticationPrincipal UsuarioAutenticado usuario,
            @RequestBody @Valid EnviarRedacaoRequest request
    ) {
        var detalhada = enviarRedacao.enviar(usuario.id(), request.temaId(), request.tipo(), request.texto());
        return RedacaoResponse.de(detalhada);
    }

    @GetMapping
    public List<RedacaoResumoResponse> listar(@AuthenticationPrincipal UsuarioAutenticado usuario) {
        return consultarRedacoes.listar(usuario.id()).stream().map(RedacaoResumoResponse::de).toList();
    }

    @GetMapping("/{id}")
    public RedacaoResponse buscar(@AuthenticationPrincipal UsuarioAutenticado usuario, @PathVariable UUID id) {
        return RedacaoResponse.de(consultarRedacoes.buscar(usuario.id(), id));
    }
}
