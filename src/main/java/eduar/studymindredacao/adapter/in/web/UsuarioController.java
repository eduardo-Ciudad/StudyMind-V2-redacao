package eduar.studymindredacao.adapter.in.web;

import eduar.studymindredacao.adapter.in.web.security.UsuarioAutenticado;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/usuarios")
public class UsuarioController {

    @GetMapping("/me")
    public UsuarioAutenticado me(@AuthenticationPrincipal UsuarioAutenticado usuario) {
        return usuario;
    }
}
