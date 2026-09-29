package eduar.studymindredacao.adapter.out.ia;

import eduar.studymindredacao.domain.exception.AvaliacaoIAException;
import eduar.studymindredacao.domain.model.CompetenciaAvaliada;
import eduar.studymindredacao.domain.model.ResultadoAvaliacaoIA;
import tools.jackson.core.JacksonException;
import tools.jackson.databind.JsonNode;
import tools.jackson.databind.json.JsonMapper;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Converte a resposta HTTP do Gemini (generateContent) em {@link ResultadoAvaliacaoIA}.
 * Qualquer resposta fora do formato esperado vira {@link AvaliacaoIAException}.
 */
public class GeminiRespostaParser {
    private static final Pattern BLOCO_MARKDOWN = Pattern.compile("^```(?:json)?\\s*(.*?)\\s*```$", Pattern.DOTALL);
    private static final Pattern NUMERO_COMPETENCIA = Pattern.compile("(?i)^\\s*c?\\s*([1-5])\\s*$");
    private static final int TAMANHO_MAXIMO_MODELO = 50;

    private final JsonMapper json;

    public GeminiRespostaParser(JsonMapper json) {
        this.json = json;
    }

    public ResultadoAvaliacaoIA interpretar(String respostaHttp, String modeloConfigurado) {
        JsonNode raiz = lerJson(respostaHttp, "resposta do Gemini não é JSON válido");

        String bloqueio = texto(raiz.path("promptFeedback").path("blockReason"));
        if (bloqueio != null) {
            throw new AvaliacaoIAException("Gemini bloqueou o prompt: " + bloqueio);
        }

        JsonNode candidato = raiz.path("candidates").path(0);
        if (candidato.isMissingNode()) {
            throw new AvaliacaoIAException("resposta do Gemini sem candidatos");
        }

        String textoGerado = extrairTexto(candidato);
        if (textoGerado.isBlank()) {
            throw new AvaliacaoIAException(
                    "Gemini não gerou texto (finishReason=" + texto(candidato.path("finishReason")) + ")"
            );
        }

        JsonNode avaliacao = lerJson(removerBlocoMarkdown(textoGerado), "avaliação gerada não é JSON válido");
        JsonNode uso = raiz.path("usageMetadata");

        try {
            return new ResultadoAvaliacaoIA(
                    avaliacao.path("anulada").asBoolean(false),
                    texto(avaliacao.path("motivo_anulacao")),
                    competencias(avaliacao),
                    listaDeTexto(avaliacao.path("pontos_fortes")),
                    listaDeTexto(avaliacao.path("pontos_desenvolvimento")),
                    texto(avaliacao.path("diagnostico")),
                    modelo(raiz, modeloConfigurado),
                    uso.path("promptTokenCount").asInt(0),
                    uso.path("candidatesTokenCount").asInt(0),
                    json.writeValueAsString(avaliacao)
            );
        } catch (IllegalArgumentException e) {
            throw new AvaliacaoIAException("avaliação gerada é inconsistente: " + e.getMessage(), e);
        }
    }

    private List<CompetenciaAvaliada> competencias(JsonNode avaliacao) {
        Map<Integer, List<String>> problemasPorCompetencia = new HashMap<>();
        for (JsonNode problema : avaliacao.path("problemas_identificados")) {
            Integer numero = numeroDaCompetencia(problema.path("competencia"));
            String descricao = texto(problema.path("descricao"));
            if (numero != null && descricao != null) {
                problemasPorCompetencia.computeIfAbsent(numero, n -> new ArrayList<>()).add(descricao);
            }
        }

        List<CompetenciaAvaliada> competencias = new ArrayList<>();
        for (JsonNode item : avaliacao.path("avaliacao_por_competencia")) {
            Integer numero = numeroDaCompetencia(item.path("competencia"));
            if (numero == null) {
                throw new IllegalArgumentException("competência inválida: " + item.path("competencia"));
            }
            if (!item.path("nota").isNumber()) {
                throw new IllegalArgumentException("nota ausente ou não numérica na C" + numero);
            }
            competencias.add(new CompetenciaAvaliada(
                    numero,
                    item.path("nota").asInt(),
                    texto(item.path("nivel_referencia")),
                    texto(item.path("resumo")),
                    problemasPorCompetencia.getOrDefault(numero, List.of())
            ));
        }
        return competencias;
    }

    private static Integer numeroDaCompetencia(JsonNode no) {
        if (no.isInt()) {
            int numero = no.asInt();
            return numero >= 1 && numero <= 5 ? numero : null;
        }
        String valor = texto(no);
        if (valor == null) {
            return null;
        }
        Matcher matcher = NUMERO_COMPETENCIA.matcher(valor);
        return matcher.matches() ? Integer.parseInt(matcher.group(1)) : null;
    }

    private static String extrairTexto(JsonNode candidato) {
        StringBuilder texto = new StringBuilder();
        for (JsonNode parte : candidato.path("content").path("parts")) {
            if (!parte.path("thought").asBoolean(false)) {
                texto.append(parte.path("text").asString(""));
            }
        }
        return texto.toString().trim();
    }

    static String removerBlocoMarkdown(String texto) {
        Matcher matcher = BLOCO_MARKDOWN.matcher(texto.trim());
        return matcher.matches() ? matcher.group(1) : texto.trim();
    }

    private static String modelo(JsonNode raiz, String modeloConfigurado) {
        String modelo = texto(raiz.path("modelVersion"));
        if (modelo == null) {
            modelo = modeloConfigurado;
        }
        return modelo != null && modelo.length() > TAMANHO_MAXIMO_MODELO
                ? modelo.substring(0, TAMANHO_MAXIMO_MODELO)
                : modelo;
    }

    private static List<String> listaDeTexto(JsonNode no) {
        List<String> itens = new ArrayList<>();
        for (JsonNode item : no) {
            String valor = texto(item);
            if (valor != null) {
                itens.add(valor);
            }
        }
        return itens;
    }

    /** Texto do nó, ou null se ausente, nulo ou em branco (evita a string "null" do NullNode). */
    private static String texto(JsonNode no) {
        if (no == null || no.isMissingNode() || no.isNull()) {
            return null;
        }
        String valor = no.asString("").strip();
        return valor.isEmpty() ? null : valor;
    }

    private JsonNode lerJson(String conteudo, String mensagemErro) {
        if (conteudo == null || conteudo.isBlank()) {
            throw new AvaliacaoIAException(mensagemErro + " (vazio)");
        }
        try {
            return json.readTree(conteudo);
        } catch (JacksonException e) {
            throw new AvaliacaoIAException(mensagemErro, e);
        }
    }
}
