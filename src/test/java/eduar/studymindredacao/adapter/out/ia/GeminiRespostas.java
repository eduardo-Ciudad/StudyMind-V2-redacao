package eduar.studymindredacao.adapter.out.ia;

import tools.jackson.databind.json.JsonMapper;
import tools.jackson.databind.node.ObjectNode;

/** Respostas de exemplo no formato do endpoint generateContent do Gemini. */
final class GeminiRespostas {
    private static final JsonMapper JSON = JsonMapper.builder().build();

    static final String AVALIACAO_VALIDA = """
            {
              "anulada": false,
              "motivo_anulacao": null,
              "avaliacao_por_competencia": [
                {"competencia": "C1", "nota": 160, "nivel_referencia": "poucos desvios", "resumo": "Bom domínio da norma."},
                {"competencia": "C2", "nota": 120, "nivel_referencia": "repertório pertinente", "resumo": "Repertório de bolso."},
                {"competencia": "C3", "nota": 120, "nivel_referencia": "argumentos previsíveis", "resumo": "Argumentos genéricos."},
                {"competencia": "C4", "nota": 160, "nivel_referencia": "coesão adequada", "resumo": "Bons conectivos."},
                {"competencia": "C5", "nota": 80, "nivel_referencia": "proposta insuficiente", "resumo": "Falta agente."}
              ],
              "pontos_fortes": ["tese explícita", "boa coesão"],
              "pontos_desenvolvimento": ["detalhar a proposta"],
              "problemas_identificados": [
                {"competencia": "C5", "descricao": "não define quem executa a ação"},
                {"competencia": "C5", "descricao": "sem meio de execução"},
                {"competencia": "C2", "descricao": "cita Bauman sem relacionar ao tema"}
              ],
              "diagnostico": "A maior fragilidade está na C5."
            }
            """;

    static final String AVALIACAO_ANULADA = """
            {
              "anulada": true,
              "motivo_anulacao": "Fuga total ao tema",
              "avaliacao_por_competencia": [
                {"competencia": "C1", "nota": 0, "nivel_referencia": "-", "resumo": "Anulada."},
                {"competencia": "C2", "nota": 0, "nivel_referencia": "-", "resumo": "Anulada."},
                {"competencia": "C3", "nota": 0, "nivel_referencia": "-", "resumo": "Anulada."},
                {"competencia": "C4", "nota": 0, "nivel_referencia": "-", "resumo": "Anulada."},
                {"competencia": "C5", "nota": 0, "nivel_referencia": "-", "resumo": "Anulada."}
              ],
              "pontos_fortes": [],
              "pontos_desenvolvimento": ["ler a proposta com atenção"],
              "problemas_identificados": [],
              "diagnostico": "O texto não trata do tema proposto."
            }
            """;

    private GeminiRespostas() {
    }

    /** Embrulha o texto gerado no envelope HTTP do Gemini. */
    static String envelope(String textoGerado) {
        return envelope(textoGerado, 0);
    }

    /** Mesmo envelope, informando também os tokens de raciocínio (0 = campo ausente). */
    static String envelope(String textoGerado, int tokensRaciocinio) {
        ObjectNode raiz = JSON.createObjectNode();
        ObjectNode candidato = raiz.putArray("candidates").addObject();
        candidato.putObject("content").put("role", "model").putArray("parts").addObject().put("text", textoGerado);
        candidato.put("finishReason", "STOP");
        ObjectNode uso = raiz.putObject("usageMetadata");
        uso.put("promptTokenCount", 2100);
        uso.put("candidatesTokenCount", 650);
        if (tokensRaciocinio > 0) {
            uso.put("thoughtsTokenCount", tokensRaciocinio);
        }
        raiz.put("modelVersion", "gemini-teste-001");
        return JSON.writeValueAsString(raiz);
    }
}
