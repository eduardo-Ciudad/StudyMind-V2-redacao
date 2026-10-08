package eduar.studymindredacao.adapter.out.ia;

import eduar.studymindredacao.domain.model.ResultadoTranscricao;
import org.springframework.core.io.ClassPathResource;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.io.InputStream;
import java.nio.charset.StandardCharsets;

@Component
class PromptTranscricaoBuilder {
    static final String RECURSO = "prompts/transcricao-v1.txt";
    private static final String PLACEHOLDER_QUANTIDADE = "{{quantidade_imagens}}";
    private static final String PLACEHOLDER_ILEGIVEL = "{{marcador_ilegivel}}";

    private final String template;

    PromptTranscricaoBuilder() {
        this(RECURSO);
    }

    PromptTranscricaoBuilder(String recurso) {
        this.template = carregarTemplate(recurso);
    }

    String construir(int quantidadeImagens) {
        if (quantidadeImagens < 1) {
            throw new IllegalArgumentException("a transcrição precisa de ao menos uma imagem");
        }
        String imagens = quantidadeImagens == 1 ? "1 imagem" : quantidadeImagens + " imagens";
        return template
                .replace(PLACEHOLDER_QUANTIDADE, imagens)
                .replace(PLACEHOLDER_ILEGIVEL, ResultadoTranscricao.MARCADOR_ILEGIVEL);
    }

    private static String carregarTemplate(String recurso) {
        try (InputStream in = new ClassPathResource(recurso).getInputStream()) {
            String conteudo = new String(in.readAllBytes(), StandardCharsets.UTF_8);
            if (!conteudo.contains(PLACEHOLDER_QUANTIDADE) || !conteudo.contains(PLACEHOLDER_ILEGIVEL)) {
                throw new IllegalStateException(
                        recurso + " deve conter " + PLACEHOLDER_QUANTIDADE + " e " + PLACEHOLDER_ILEGIVEL
                );
            }
            return conteudo;
        } catch (IOException e) {
            throw new IllegalStateException("Não foi possível carregar " + recurso, e);
        }
    }
}