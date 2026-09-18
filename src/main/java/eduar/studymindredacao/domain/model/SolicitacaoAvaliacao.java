package eduar.studymindredacao.domain.model;

public record SolicitacaoAvaliacao(String tema, String textoRedacao) {
    public SolicitacaoAvaliacao{
        if (tema == null || tema.isBlank()) {
            throw new IllegalArgumentException("tema é obrigatorio");
        }
        if (textoRedacao == null || textoRedacao.isBlank()) {
            throw new IllegalArgumentException("textoRedacao é obrigatório");
        }
        tema = tema.strip();
        textoRedacao = textoRedacao.strip();
    }
}
