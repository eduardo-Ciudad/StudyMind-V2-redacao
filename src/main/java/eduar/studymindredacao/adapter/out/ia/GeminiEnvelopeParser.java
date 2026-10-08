package eduar.studymindredacao.adapter.out.ia;

import tools.jackson.core.JacksonException;
import tools.jackson.databind.JsonNode;
import tools.jackson.databind.json.JsonMapper;

import java.util.regex.Matcher;
import java.util.regex.Pattern;


class GeminiEnvelopeParser {
    private static final Pattern BLOCO_MARKDOWN = Pattern.compile("^```(?:json)?\\s*(.*?)\\s*```$", Pattern.DOTALL);
    static final int TAMANHO_MAXIMO_MODELO = 50;

    private final JsonMapper json;

    GeminiEnvelopeParser(JsonMapper json) {
        this.json = json;
    }

    GeminiResposta ler(String respostaHttp, String modeloConfigurado) {
        JsonNode raiz = lerJson(respostaHttp);

        String bloqueio = texto(raiz.path("promptFeedback").path("blockReason"));
        if (bloqueio != null) {
            throw GeminiException.repetivel("Gemini bloqueou o prompt: " + bloqueio);
        }

        JsonNode candidato = raiz.path("candidates").path(0);
        if (candidato.isMissingNode()) {
            throw GeminiException.repetivel("resposta do Gemini sem candidatos");
        }

        String textoGerado = removerBlocoMarkdown(extrairTexto(candidato));
        if (textoGerado.isBlank()) {
            throw GeminiException.repetivel(
                    "Gemini não gerou texto (finishReason=" + texto(candidato.path("finishReason")) + ")"
            );
        }

        JsonNode uso = raiz.path("usageMetadata");
        return new GeminiResposta(
                textoGerado,
                modelo(raiz, modeloConfigurado),
                uso.path("promptTokenCount").asInt(0),
                uso.path("candidatesTokenCount").asInt(0) + uso.path("thoughtsTokenCount").asInt(0)
        );
    }

    static String removerBlocoMarkdown(String texto) {
        Matcher matcher = BLOCO_MARKDOWN.matcher(texto.trim());
        return matcher.matches() ? matcher.group(1) : texto.trim();
    }

    private static String extrairTexto(JsonNode candidato) {
        StringBuilder texto = new StringBuilder();
        for (JsonNode parte : candidato.path("content").path("parts")) {
            if (!parte.path("thought").asBoolean(false)) {
                texto.append(parte.path("text").asString(""));
            }
        }
        return texto.toString();
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

    private static String texto(JsonNode no) {
        if (no == null || no.isMissingNode() || no.isNull()) {
            return null;
        }
        String valor = no.asString("").strip();
        return valor.isEmpty() ? null : valor;
    }

    private JsonNode lerJson(String conteudo) {
        if (conteudo == null || conteudo.isBlank()) {
            throw GeminiException.repetivel("resposta do Gemini não é JSON válido (vazio)");
        }
        try {
            return json.readTree(conteudo);
        } catch (JacksonException e) {
            throw GeminiException.repetivel("resposta do Gemini não é JSON válido", e);
        }
    }
}