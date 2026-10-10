package eduar.studymindredacao.domain.model;

import eduar.studymindredacao.domain.model.enums.FuncaoArgumentativa;
import eduar.studymindredacao.domain.model.enums.TipoEntidade;

/**
 * Filtros combináveis da listagem de repertórios. Todos opcionais (null = sem filtro).
 * Só valida: o tamanho padrão é aplicado por quem monta o filtro (controller), usando TAMANHO_PADRAO.
 *
 * @param macroeixo slug do macroeixo (ex.: saude)
 * @param problema  slug do problema social (ex.: saude-mental)
 * @param tipo      tipo de entidade (pessoa, conceito, obra...)
 * @param funcao    função argumentativa
 * @param busca     texto livre; em branco vira null
 * @param pagina    índice da página, começando em 0
 * @param tamanho   itens por página, de 1 a TAMANHO_MAXIMO
 */
public record FiltroRepertorio(
        String macroeixo,
        String problema,
        TipoEntidade tipo,
        FuncaoArgumentativa funcao,
        String busca,
        int pagina,
        int tamanho
) {
    public static final int TAMANHO_PADRAO = 20;
    public static final int TAMANHO_MAXIMO = 50;
    public static final int BUSCA_MAXIMO = 100;
    private static final int SLUG_MAXIMO = 60;

    public FiltroRepertorio {
        macroeixo = slugOpcional(macroeixo, "macroeixo");
        problema = slugOpcional(problema, "problema");
        busca = buscaOpcional(busca);
        if (pagina < 0) {
            throw new IllegalArgumentException("pagina não pode ser negativa");
        }
        if (tamanho < 1 || tamanho > TAMANHO_MAXIMO) {
            throw new IllegalArgumentException("tamanho deve estar entre 1 e " + TAMANHO_MAXIMO);
        }
    }

    /** Sem filtros, com o tamanho padrão. */
    public static FiltroRepertorio semFiltros(int pagina) {
        return new FiltroRepertorio(null, null, null, null, null, pagina, TAMANHO_PADRAO);
    }

    private static String slugOpcional(String valor, String campo) {
        if (valor == null || valor.isBlank()) {
            return null;
        }
        Validacoes.validarSlug(valor, campo, SLUG_MAXIMO);
        return valor;
    }

    private static String buscaOpcional(String valor) {
        if (valor == null || valor.isBlank()) {
            return null;
        }
        var limpa = valor.strip();
        Validacoes.validarTamanhoMaximo(limpa, "busca", BUSCA_MAXIMO);
        return limpa;
    }
}
