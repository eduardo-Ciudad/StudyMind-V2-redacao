package eduar.studymindredacao.adapter.out.ia;


record GeminiResposta(String textoGerado, String modelo, int tokensEntrada, int tokensSaida) {

    GeminiResposta {
        if (textoGerado == null || textoGerado.isBlank()) {
            throw new IllegalArgumentException("textoGerado é obrigatório");
        }
        if (modelo == null || modelo.isBlank()) {
            throw new IllegalArgumentException("modelo é obrigatório");
        }
        if (tokensEntrada < 0 || tokensSaida < 0) {
            throw new IllegalArgumentException("contagem de tokens não pode ser negativa");
        }
    }
}