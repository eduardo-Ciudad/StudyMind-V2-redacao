package eduar.studymindredacao.adapter.out.ia;

import eduar.studymindredacao.application.usecase.LogSeguro;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.MediaType;
import org.springframework.http.client.JdkClientHttpRequestFactory;
import org.springframework.stereotype.Component;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.client.HttpServerErrorException;
import org.springframework.web.client.ResourceAccessException;
import org.springframework.web.client.RestClient;
import tools.jackson.databind.json.JsonMapper;
import tools.jackson.databind.node.ArrayNode;
import tools.jackson.databind.node.ObjectNode;

import java.net.http.HttpClient;
import java.time.Duration;
import java.util.List;
import java.util.function.Function;

/**
 * Chamada ao generateContent do Gemini, compartilhada pelos adapters de IA.
 * Cuida do HTTP, do envelope da resposta e das novas tentativas; quem chama
 * só monta as partes (prompt, imagens) e interpreta o texto gerado.
 */
@Component
class GeminiClient {
    static final int MAX_TENTATIVAS = 2;
    private static final Logger log = LoggerFactory.getLogger(GeminiClient.class);

    private final RestClient restClient;
    private final JsonMapper json;
    private final GeminiEnvelopeParser envelopeParser;
    private final String apiKey;

    GeminiClient(
            @Value("${gemini.base-url}") String baseUrl,
            @Value("${gemini.api-key}") String apiKey,
            @Value("${gemini.timeout-segundos}") int timeoutSegundos
    ) {
        this.apiKey = apiKey;
        this.json = JsonMapper.builder().build();
        this.envelopeParser = new GeminiEnvelopeParser(json);

        var httpClient = HttpClient.newBuilder()
                .connectTimeout(Duration.ofSeconds(10))
                .build();
        var requestFactory = new JdkClientHttpRequestFactory(httpClient);
        requestFactory.setReadTimeout(Duration.ofSeconds(timeoutSegundos));
        this.restClient = RestClient.builder()
                .baseUrl(baseUrl)
                .requestFactory(requestFactory)
                .build();
    }

    /**
     * Envia as partes ao modelo e devolve o que {@code interpretar} produzir a partir da resposta.
     * Repete até {@link #MAX_TENTATIVAS} vezes em falha passageira ou resposta mal formada,
     * inclusive quando a própria interpretação lança {@link GeminiException} repetível.
     * Qualquer outra exceção da interpretação sobe na hora, sem nova tentativa.
     *
     * @throws GeminiException quando as tentativas acabam ou a falha não vale repetir
     */
    <T> T gerar(String modelo, double temperatura, List<GeminiParte> partes, Function<GeminiResposta, T> interpretar) {
        if (modelo == null || modelo.isBlank()) {
            throw new IllegalArgumentException("modelo é obrigatório");
        }
        if (partes == null || partes.isEmpty()) {
            throw new IllegalArgumentException("a requisição ao Gemini precisa de ao menos uma parte");
        }
        String corpo = montarCorpo(partes, temperatura);
        GeminiException ultimaFalha = null;

        for (int tentativa = 1; tentativa <= MAX_TENTATIVAS; tentativa++) {
            try {
                String respostaHttp = restClient.post()
                        .uri("/models/{modelo}:generateContent", modelo)
                        .header("x-goog-api-key", apiKey)
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON)
                        .body(corpo)
                        .retrieve()
                        .body(String.class);
                return interpretar.apply(envelopeParser.ler(respostaHttp, modelo));
            } catch (HttpClientErrorException e) {
                // 4xx (chave inválida, cota estourada, requisição malformada): repetir não resolve
                throw GeminiException.definitiva(
                        "Gemini recusou a requisição (HTTP " + e.getStatusCode().value() + ")", e
                );
            } catch (HttpServerErrorException | ResourceAccessException e) {
                ultimaFalha = GeminiException.repetivel("Gemini indisponível: " + e.getMessage(), e);
            } catch (GeminiException e) {
                if (!e.podeRepetir()) {
                    throw e;
                }
                ultimaFalha = e;
            }
            log.warn("Falha na chamada ao Gemini {} (tentativa {}/{}): {}",
                    modelo, tentativa, MAX_TENTATIVAS, LogSeguro.limpar(ultimaFalha.getMessage()));
        }
        throw ultimaFalha;
    }

    String montarCorpo(List<GeminiParte> partes, double temperatura) {
        ObjectNode corpo = json.createObjectNode();
        ArrayNode nos = corpo.putArray("contents")
                .addObject()
                .put("role", "user")
                .putArray("parts");
        for (GeminiParte parte : partes) {
            if (parte instanceof GeminiParte.Texto texto) {
                nos.addObject().put("text", texto.texto());
            } else if (parte instanceof GeminiParte.Imagem imagem) {
                nos.addObject()
                        .putObject("inlineData")
                        .put("mimeType", imagem.mimeType())
                        .put("data", imagem.dadosBase64());
            }
        }
        corpo.putObject("generationConfig")
                .put("temperature", temperatura)
                .put("responseMimeType", "application/json");
        return json.writeValueAsString(corpo);
    }
}