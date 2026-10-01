package eduar.studymindredacao.application.usecase;

import java.time.OffsetDateTime;

/** Saldo de correções do aluno no dia corrente (fuso da aplicação). */
public record UsoDiario(int usadas, int limite, int restantes, OffsetDateTime renovaEm) {
}
