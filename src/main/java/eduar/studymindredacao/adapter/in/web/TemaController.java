package eduar.studymindredacao.adapter.in.web;

import eduar.studymindredacao.adapter.in.web.dto.TemaResponse;
import eduar.studymindredacao.application.usecase.ListarTemasService;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/temas")
public class TemaController {
    private final ListarTemasService listarTemas;

    public TemaController(ListarTemasService listarTemas) {
        this.listarTemas = listarTemas;
    }

    @GetMapping
    public List<TemaResponse> listar() {
        return listarTemas.listarAtivos().stream().map(TemaResponse::de).toList();
    }
}
