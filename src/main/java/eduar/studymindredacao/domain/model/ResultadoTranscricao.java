package eduar.studymindredacao.domain.model;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.IntStream;


public record ResultadoTranscricao(
        List<String> linhas,
        String modelo,
        int tokensEntrada,
        int tokensSaida) {

    public static final String MARCADOR_ILEGIVEL = "[ilegível]";

    public ResultadoTranscricao {
        if (linhas == null || linhas.isEmpty()) {
            throw new IllegalArgumentException("a transcrição deve ter ao menos uma linha");
        }
        for (String linha : linhas) {
            if (linha == null) {
                throw new IllegalArgumentException("a transcrição não pode ter linha nula");
            }
            if (linha.contains("\n") || linha.contains("\r")) {
                throw new IllegalArgumentException("cada linha da transcrição deve ser uma única linha da folha");
            }
        }
        if (linhas.stream().allMatch(String::isBlank)) {
            throw new IllegalArgumentException("a transcrição não tem texto");
        }
        if (tokensEntrada < 0 || tokensSaida < 0) {
            throw new IllegalArgumentException("contagem de tokens não pode ser negativa");
        }
        linhas = List.copyOf(linhas);
    }

    public String texto() {
        return String.join("\n", linhas);
    }

    public int qtdLinhasEscritas() {
        return (int) linhas.stream().filter(linha -> !linha.isBlank()).count();
    }

    public List<Integer> linhasComTrechoIlegivel() {
        List<Integer> numeros = new ArrayList<>();
        IntStream.range(0, linhas.size())
                .filter(i -> linhas.get(i).contains(MARCADOR_ILEGIVEL))
                .forEach(i -> numeros.add(i + 1));
        return List.copyOf(numeros);
    }

    public boolean temTrechoIlegivel() {
        return linhas.stream().anyMatch(linha -> linha.contains(MARCADOR_ILEGIVEL));
    }
}