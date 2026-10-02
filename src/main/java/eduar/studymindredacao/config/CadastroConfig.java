package eduar.studymindredacao.config;

import eduar.studymindredacao.application.usecase.PoliticaCadastro;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class CadastroConfig {

    @Bean
    PoliticaCadastro politicaCadastro(
            @Value("${app.cadastro.aberto}") boolean aberto,
            @Value("${app.cadastro.emails-permitidos:}") String emailsPermitidos
    ) {
        return PoliticaCadastro.de(aberto, emailsPermitidos);
    }
}
