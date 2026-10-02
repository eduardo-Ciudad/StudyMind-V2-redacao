package eduar.studymindredacao.application.usecase;

import eduar.studymindredacao.domain.model.PrevisaoTema;
import eduar.studymindredacao.domain.model.Tema;

/** Modelo de leitura da tela de temas possíveis: o tema, o material de estudo e se o aluno já o adicionou. */
public record TemaPossivel(Tema tema, PrevisaoTema previsao, boolean adicionado) {
}
