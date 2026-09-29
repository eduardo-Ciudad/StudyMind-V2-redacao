package eduar.studymindredacao.adapter.out.ia;

import eduar.studymindredacao.domain.exception.AvaliacaoIAException;
import eduar.studymindredacao.domain.model.ResultadoAvaliacaoIA;
import eduar.studymindredacao.domain.model.SolicitacaoAvaliacao;
import eduar.studymindredacao.domain.port.AvaliacaoIAPort;
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
import tools.jackson.databind.node.ObjectNode;

import java.net.http.HttpClient;
import java.time.Duration;

@Component
public class GeminiAvaliacaoAdapter implements AvaliacaoIAPort {
    private static final Logger log = LoggerFactory.getLogger(GeminiAvaliacaoAdapter.class);
    static final int MAX_TENTATIVAS = 2;

    private final PromptAvaliacaoBuilder promptBuilder;
    private final GeminiRespostaParser parser;
    private final JsonMapper json;
    private final RestClient restClient;
    private final String modelo;
    private final String apiKey;
    private final double temperatura;

    public GeminiAvaliacaoAdapter(
            PromptAvaliacaoBuilder promptBuilder,
            @Value("${gemini.base-url}") String baseUrl,
            @Value("${gemini.modelo}") String modelo,
            @Value("${gemini.api-key}") String apiKey,
            @Value("${gemini.temperatura}") double temperatura,
            @Value("${gemini.timeout-segundos}") int timeoutSegundos
    ) {
        this.promptBuilder = promptBuilder;
        this.json = JsonMapper.builder().build();
        this.parser = new GeminiRespostaParser(json);
        this.modelo = modelo;
        this.apiKey = apiKey;
        this.temperatura = temperatura;

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

    @Override
    public ResultadoAvaliacaoIA avaliar(SolicitacaoAvaliacao solicitacao) {
        String corpo = montarCorpo(promptBuilder.construir(solicitacao));
        AvaliacaoIAException ultimaFalha = null;

        for (int tentativa = 1; tentativa <= MAX_TENTATIVAS; tentativa++) {
            try {
                String resposta = restClient.post()
                        .uri("/models/{modelo}:generateContent", modelo)
                        .header("x-goog-api-key", apiKey)
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON)
                        .body(corpo)
                        .retrieve()
                        .body(String.class);
                return parser.interpretar(resposta, modelo);
            } catch (HttpClientErrorException e) {
                // 4xx (chave inválida, cota estourada, requisição malformada): repetir não resolve
                throw new AvaliacaoIAException("Gemini recusou a requisição (HTTP " + e.getStatusCode().value() + ")", e);
            } catch (HttpServerErrorException | ResourceAccessException e) {
                ultimaFalha = new AvaliacaoIAException("Gemini indisponível: " + e.getMessage(), e);
            } catch (AvaliacaoIAException e) {
                ultimaFalha = e;
            }
            log.warn("Falha ao avaliar redação no Gemini (tentativa {}/{}): {}",
                    tentativa, MAX_TENTATIVAS, ultimaFalha.getMessage());
        }
        throw ultimaFalha;
    }

    private String montarCorpo(String prompt) {
        ObjectNode corpo = json.createObjectNode();
        corpo.putArray("contents")
                .addObject()
                .put("role", "user")
                .putArray("parts")
                .addObject()
                .put("text", prompt);
        corpo.putObject("generationConfig")
                .put("temperature", temperatura)
                .put("responseMimeType", "application/json");
        return json.writeValueAsString(corpo);
    }
}
