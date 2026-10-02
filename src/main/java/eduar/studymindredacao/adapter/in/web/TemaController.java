package eduar.studymindredacao.adapter.in.web;

import eduar.studymindredacao.adapter.in.web.dto.TemaPossivelResponse;
import eduar.studymindredacao.adapter.in.web.dto.TemaResponse;
import eduar.studymindredacao.adapter.in.web.security.UsuarioAutenticado;
import eduar.studymindredacao.application.usecase.ListarTemasPossiveisService;
import eduar.studymindredacao.application.usecase.ListarTemasService;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/temas")
public class TemaController {
    private final ListarTemasService listarTemas;
    private final ListarTemasPossiveisService listarTemasPossiveis;

    public TemaController(ListarTemasService listarTemas, ListarTemasPossiveisService listarTemasPossiveis) {
        this.listarTemas = listarTemas;
        this.listarTemasPossiveis = listarTemasPossiveis;
    }

    /** Oficiais e autorais, mais os temas possíveis que o aluno adicionou (campo origem = PREVISAO). */
    @GetMapping
    public List<TemaResponse> listar(@AuthenticationPrincipal UsuarioAutenticado usuario) {
        return listarTemas.listarDisponiveis(usuario.id()).stream().map(TemaResponse::de).toList();
    }

    /** Os temas possíveis do ENEM, em ordem de ranking, com o material de estudo de cada um. */
    @GetMapping("/possiveis")
    public List<TemaPossivelResponse> possiveis(@AuthenticationPrincipal UsuarioAutenticado usuario) {
        return listarTemasPossiveis.listar(usuario.id()).stream().map(TemaPossivelResponse::de).toList();
    }
}
