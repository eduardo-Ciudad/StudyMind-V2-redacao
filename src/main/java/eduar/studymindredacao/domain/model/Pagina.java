package eduar.studymindredacao.domain.model;

import java.util.List;

/**
 * Uma página de resultados. Tipo próprio do domínio para o Page do Spring não vazar para as portas.
 *
 * @param itens      itens desta página (lista imutável)
 * @param pagina     índice da página, começando em 0
 * @param tamanho    tamanho de página pedido
 * @param totalItens total de itens em todas as páginas
 */
public record Pagina<T>(List<T> itens, int pagina, int tamanho, long totalItens) {

    public Pagina {
        itens = List.copyOf(Validacoes.requererNaoNulo(itens, "itens"));
        if (pagina < 0) {
            throw new IllegalArgumentException("pagina não pode ser negativa");
        }
        if (tamanho < 1) {
            throw new IllegalArgumentException("tamanho deve ser pelo menos 1");
        }
        if (totalItens < 0) {
            throw new IllegalArgumentException("totalItens não pode ser negativo");
        }
    }

    public int totalPaginas() {
        return (int) ((totalItens + tamanho - 1) / tamanho);
    }

    public boolean temProxima() {
        return pagina + 1 < totalPaginas();
    }
}
