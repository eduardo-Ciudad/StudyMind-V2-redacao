package eduar.studymindredacao.adapter.out.ia;

import eduar.studymindredacao.application.usecase.LogSeguro;
import eduar.studymindredacao.domain.exception.ImagemNaoReconhecidaException;
import eduar.studymindredacao.domain.model.ResultadoTranscricao;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import tools.jackson.core.JacksonException;
import tools.jackson.databind.JsonNode;
import tools.jackson.databind.json.JsonMapper;

import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

class GeminiTranscricaoParser {
    private static final Logger log = LoggerFactory.getLogger(GeminiTranscricaoParser.class);
    private static final Pattern QUEBRA_DE_LINHA = Pattern.compile("\\r\\n|\\r|\\n");
    // UNICODE_CASE: sem ela, o (?i) do Java só ignora caixa em letras ASCII e "[ILEGÍVEL]" escaparia
    private static final Pattern VARIACAO_DO_MARCADOR = Pattern.compile(
            "\\[\\s*ileg[ií]vel\\s*]", Pattern.CASE_INSENSITIVE | Pattern.UNICODE_CASE);

    private final JsonMapper json;

    GeminiTranscricaoParser(JsonMapper json) {
        this.json = json;
    }

    ResultadoTranscricao interpretar(GeminiResposta resposta) {
        JsonNode raiz = lerJson(resposta.textoGerado());

        JsonNode ehRedacao = raiz.path("eh_redacao");
        if (!ehRedacao.isBoolean()) {
            throw GeminiException.repetivel("transcrição gerada sem o campo eh_redacao");
        }
        if (!ehRedacao.asBoolean()) {
            log.info("Gemini não encontrou redação na imagem: {}", LogSeguro.limpar(raiz.path("motivo").asString("")));
            throw new ImagemNaoReconhecidaException();
        }

        List<String> linhas = linhas(raiz.path("linhas"));
        if (linhas.isEmpty()) {
            throw new ImagemNaoReconhecidaException();
        }

        try {
            return new ResultadoTranscricao(linhas, resposta.modelo(), resposta.tokensEntrada(), resposta.tokensSaida());
        } catch (IllegalArgumentException e) {
            throw GeminiException.repetivel("transcrição gerada é inconsistente: " + e.getMessage(), e);
        }
    }

    private static List<String> linhas(JsonNode no) {
        if (!no.isArray()) {
            throw GeminiException.repetivel("transcrição gerada sem a lista de linhas");
        }
        List<String> linhas = new ArrayList<>();
        for (JsonNode item : no) {
            if (item.isNull() || !item.isValueNode()) {
                throw GeminiException.repetivel("transcrição gerada com linha que não é texto");
            }
            String texto = VARIACAO_DO_MARCADOR.matcher(item.asString(""))
                    .replaceAll(Matcher.quoteReplacement(ResultadoTranscricao.MARCADOR_ILEGIVEL));
            // o modelo às vezes junta duas linhas da folha num item só
            linhas.addAll(List.of(QUEBRA_DE_LINHA.split(texto, -1)));
        }
        removerVaziasDasPontas(linhas);
        return linhas;
    }

    private static void removerVaziasDasPontas(List<String> linhas) {
        while (!linhas.isEmpty() && linhas.get(0).isBlank()) {
            linhas.remove(0);
        }
        while (!linhas.isEmpty() && linhas.get(linhas.size() - 1).isBlank()) {
            linhas.remove(linhas.size() - 1);
        }
    }

    private JsonNode lerJson(String conteudo) {
        try {
            return json.readTree(conteudo);
        } catch (JacksonException e) {
            throw GeminiException.repetivel("transcrição gerada não é JSON válido", e);
        }
    }
}