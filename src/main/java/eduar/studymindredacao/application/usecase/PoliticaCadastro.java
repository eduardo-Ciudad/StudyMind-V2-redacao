package eduar.studymindredacao.application.usecase;

import java.util.Arrays;
import java.util.Locale;
import java.util.Set;
import java.util.stream.Collectors;

/**
 * Quem pode criar conta. Com o cadastro fechado, só os e-mails da lista passam.
 * A lista vem de uma variável de ambiente separada por vírgulas.
 */
public record PoliticaCadastro(boolean aberto, Set<String> emailsPermitidos) {
    public PoliticaCadastro {
        emailsPermitidos = emailsPermitidos == null ? Set.of() : Set.copyOf(emailsPermitidos);
    }

    public static PoliticaCadastro aberta() {
        return new PoliticaCadastro(true, Set.of());
    }

    public static PoliticaCadastro de(boolean aberto, String emailsSeparadosPorVirgula) {
        Set<String> emails = emailsSeparadosPorVirgula == null ? Set.of() : Arrays.stream(emailsSeparadosPorVirgula.split(","))
                .map(e -> e.trim().toLowerCase(Locale.ROOT))
                .filter(e -> !e.isEmpty())
                .collect(Collectors.toUnmodifiableSet());
        return new PoliticaCadastro(aberto, emails);
    }

    /** Recebe o e-mail já normalizado (trim + minúsculas). */
    public boolean permite(String emailNormalizado) {
        return aberto || emailsPermitidos.contains(emailNormalizado);
    }
}
