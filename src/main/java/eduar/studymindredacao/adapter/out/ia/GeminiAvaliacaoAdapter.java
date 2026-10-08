package eduar.studymindredacao.adapter.out.ia;

import eduar.studymindredacao.domain.exception.AvaliacaoIAException;
import eduar.studymindredacao.domain.model.ResultadoAvaliacaoIA;
import eduar.studymindredacao.domain.model.SolicitacaoAvaliacao;
import eduar.studymindredacao.domain.port.AvaliacaoIAPort;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import tools.jackson.databind.json.JsonMapper;

import java.util.List;


@Component
public class GeminiAvaliacaoAdapter implements AvaliacaoIAPort {
    private final PromptAvaliacaoBuilder promptBuilder;
    private final GeminiClient client;
    private final GeminiRespostaParser parser;
    private final String modelo;
    private final double temperatura;

    public GeminiAvaliacaoAdapter(
            PromptAvaliacaoBuilder promptBuilder,
            GeminiClient client,
            @Value("${gemini.modelo}") String modelo,
            @Value("${gemini.temperatura}") double temperatura
    ) {
        this.promptBuilder = promptBuilder;
        this.client = client;
        this.parser = new GeminiRespostaParser(JsonMapper.builder().build());
        this.modelo = modelo;
        this.temperatura = temperatura;
    }

    @Override
    public ResultadoAvaliacaoIA avaliar(SolicitacaoAvaliacao solicitacao) {
        List<GeminiParte> partes = List.of(new GeminiParte.Texto(promptBuilder.construir(solicitacao)));
        try {
            return client.gerar(modelo, temperatura, partes, parser::interpretar);
        } catch (GeminiException e) {
            throw new AvaliacaoIAException(e.getMessage(), e);
        }
    }
}