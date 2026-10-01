package eduar.studymindredacao.adapter.in.web.dto;

import eduar.studymindredacao.application.usecase.UsoDiario;

import java.time.OffsetDateTime;

public record UsoDiarioResponse(int usadas, int limite, int restantes, OffsetDateTime renovaEm) {
    public static UsoDiarioResponse de(UsoDiario uso) {
        return new UsoDiarioResponse(uso.usadas(), uso.limite(), uso.restantes(), uso.renovaEm());
    }
}
