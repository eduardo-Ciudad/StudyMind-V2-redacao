package eduar.studymindredacao.adapter.in.web.dto;

import eduar.studymindredacao.domain.model.enums.OrigemRedacao;
import eduar.studymindredacao.domain.model.enums.TipoRedacao;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;

import java.util.UUID;

public record EnviarRedacaoRequest(
        @NotNull UUID temaId,
        @NotNull TipoRedacao tipo,
        @NotBlank @Size(max = 5000) String texto,
        // opcional: o editor não manda; a tela de transcrição manda MANUSCRITO
        OrigemRedacao origem
) {
}
