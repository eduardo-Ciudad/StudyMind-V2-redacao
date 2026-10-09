package eduar.studymindredacao.domain.model;

import eduar.studymindredacao.domain.model.enums.FuncaoArgumentativa;
import eduar.studymindredacao.domain.model.enums.PapelRepertorio;
import eduar.studymindredacao.domain.model.enums.StatusVerificacao;
import eduar.studymindredacao.domain.model.enums.TipoEntidade;

import java.time.LocalDate;
import java.util.EnumSet;
import java.util.List;
import java.util.Set;
import java.util.UUID;
import java.util.regex.Pattern;

/**
 * Um registro da base de Repertório (pessoa, conceito, obra, norma ou dado), vindo da curadoria da pesquisa.
 *
 * <p>Registros inativos podem estar incompletos (ex.: REP-001 a 024, que a pesquisa só trouxe resumidos).
 * Um registro ativo, que o aluno vê, precisa estar completo o bastante para ser usado na redação:
 * ideia central, ao menos um problema social e uma fonte, tipo de evidência quando é dado,
 * e exemplo de aplicação quando é pessoa, conceito ou obra. REJECTED nunca fica ativo.
 */
public record Repertorio(
        UUID id,
        String codigo,
        TipoEntidade tipoEntidade,
        PapelRepertorio papel,
        String nome,
        String subtitulo,
        String tipoDescricao,
        String area,
        String pais,
        ConteudoDidatico conteudo,
        Curadoria curadoria,
        DadosEvidencia evidencia,
        List<FuncaoArgumentativa> funcoesArgumentativas,
        List<String> tiposArgumento,
        List<String> tags,
        List<ProblemaSocial> problemas,
        List<Fonte> fontes,
        StatusVerificacao statusVerificacao,
        LocalDate verificadoEm,
        boolean ativo
) {
    /** REP-025, EVD-001, CF-007. */
    private static final Pattern CODIGO = Pattern.compile("^(REP|EVD|CF)-\\d{3}$");

    /** Tipos que só servem na redação com um exemplo de aplicação pronto. */
    private static final Set<TipoEntidade> EXIGEM_EXEMPLO =
            EnumSet.of(TipoEntidade.PERSON, TipoEntidade.CONCEPT, TipoEntidade.CULTURAL_WORK);

    public static final int TAMANHO_MAXIMO_NOME = 255;
    public static final int TAMANHO_MAXIMO_SUBTITULO = 255;
    public static final int TAMANHO_MAXIMO_TIPO_DESCRICAO = 120;
    public static final int TAMANHO_MAXIMO_AREA = 255;
    public static final int TAMANHO_MAXIMO_PAIS = 80;

    public Repertorio {
        Validacoes.validarTextoObrigatorio(codigo, "codigo", 10);
        if (!CODIGO.matcher(codigo).matches()) {
            throw new IllegalArgumentException("codigo deve seguir o padrão REP-000, EVD-000 ou CF-000");
        }
        Validacoes.requererNaoNulo(tipoEntidade, "tipoEntidade");
        Validacoes.requererNaoNulo(papel, "papel");
        Validacoes.requererNaoNulo(statusVerificacao, "statusVerificacao");
        Validacoes.validarTextoObrigatorio(nome, "nome", TAMANHO_MAXIMO_NOME);
        Validacoes.validarTamanhoMaximo(subtitulo, "subtitulo", TAMANHO_MAXIMO_SUBTITULO);
        Validacoes.validarTamanhoMaximo(tipoDescricao, "tipoDescricao", TAMANHO_MAXIMO_TIPO_DESCRICAO);
        Validacoes.validarTamanhoMaximo(area, "area", TAMANHO_MAXIMO_AREA);
        Validacoes.validarTamanhoMaximo(pais, "pais", TAMANHO_MAXIMO_PAIS);

        conteudo = conteudo == null ? new ConteudoDidatico(null, null, null, null, null) : conteudo;
        curadoria = curadoria == null ? Curadoria.vazia() : curadoria;
        evidencia = evidencia == null ? DadosEvidencia.vazio() : evidencia;
        funcoesArgumentativas = copiar(funcoesArgumentativas);
        tiposArgumento = copiar(tiposArgumento);
        tags = copiar(tags);
        problemas = copiar(problemas);
        fontes = copiar(fontes);

        if (ativo) {
            validarAtivo(codigo, tipoEntidade, statusVerificacao, conteudo, evidencia, problemas, fontes);
        }
    }

    public boolean ehEvidencia() {
        return tipoEntidade == TipoEntidade.EVIDENCE;
    }

    private static void validarAtivo(
            String codigo,
            TipoEntidade tipoEntidade,
            StatusVerificacao statusVerificacao,
            ConteudoDidatico conteudo,
            DadosEvidencia evidencia,
            List<ProblemaSocial> problemas,
            List<Fonte> fontes
    ) {
        if (statusVerificacao == StatusVerificacao.REJECTED) {
            throw new IllegalArgumentException(codigo + ": repertório REJECTED não pode ficar ativo");
        }
        if (!conteudo.temIdeiaCentral()) {
            throw new IllegalArgumentException(codigo + ": repertório ativo precisa de ideia central");
        }
        if (problemas.isEmpty()) {
            throw new IllegalArgumentException(codigo + ": repertório ativo precisa de ao menos um problema social");
        }
        if (fontes.isEmpty()) {
            throw new IllegalArgumentException(codigo + ": repertório ativo precisa de ao menos uma fonte");
        }
        if (tipoEntidade == TipoEntidade.EVIDENCE && !evidencia.temTipo()) {
            throw new IllegalArgumentException(codigo + ": evidência ativa precisa de ao menos um tipo de evidência");
        }
        if (EXIGEM_EXEMPLO.contains(tipoEntidade) && !conteudo.temExemploAplicacao()) {
            throw new IllegalArgumentException(codigo + ": repertório ativo deste tipo precisa de exemplo de aplicação");
        }
    }

    private static <T> List<T> copiar(List<T> lista) {
        return lista == null ? List.of() : List.copyOf(lista);
    }
}
