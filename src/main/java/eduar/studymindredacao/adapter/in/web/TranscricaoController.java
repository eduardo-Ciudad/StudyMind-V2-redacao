package eduar.studymindredacao.adapter.in.web;

import eduar.studymindredacao.adapter.in.web.dto.TranscricaoResponse;
import eduar.studymindredacao.adapter.in.web.security.UsuarioAutenticado;
import eduar.studymindredacao.application.usecase.TranscreverRedacaoService;
import eduar.studymindredacao.domain.model.ImagemRedacao;
import org.springframework.http.MediaType;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestPart;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.io.UncheckedIOException;
import java.util.ArrayList;
import java.util.List;

/**
 * Transcrição da foto de uma redação manuscrita (#15). A imagem é lida em memória e descartada:
 * não vai para disco nem para o banco, e nem ela nem o texto transcrito vão para o log.
 */
@RestController
@RequestMapping("/transcricoes")
public class TranscricaoController {
    private final TranscreverRedacaoService transcreverRedacao;

    public TranscricaoController(TranscreverRedacaoService transcreverRedacao) {
        this.transcreverRedacao = transcreverRedacao;
    }

    /** Recebe 1 ou 2 fotos no campo "imagens" e devolve o texto para o aluno revisar antes de enviar. */
    @PostMapping(consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public TranscricaoResponse transcrever(
            @AuthenticationPrincipal UsuarioAutenticado usuario,
            @RequestPart(name = "imagens", required = false) List<MultipartFile> imagens
    ) {
        // sem o campo, a lista chega nula: o caso de uso responde 400 com motivo QUANTIDADE_INVALIDA
        var resultado = transcreverRedacao.transcrever(usuario.id(), paraImagens(imagens));
        return TranscricaoResponse.de(resultado);
    }

    private static List<ImagemRedacao> paraImagens(List<MultipartFile> arquivos) {
        if (arquivos == null) {
            return List.of();
        }
        List<ImagemRedacao> imagens = new ArrayList<>();
        for (MultipartFile arquivo : arquivos) {
            try {
                // o tipo é decidido pelos bytes (magic bytes), nunca pelo Content-Type ou nome do arquivo
                imagens.add(ImagemRedacao.de(arquivo.getBytes()));
            } catch (IOException e) {
                throw new UncheckedIOException("não foi possível ler a imagem enviada", e);
            }
        }
        return imagens;
    }
}
