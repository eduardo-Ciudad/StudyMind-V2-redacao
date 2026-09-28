package eduar.studymindredacao.domain.port;

public interface SenhaEncoderPort {
    String codificar(String senhaPura);

    boolean confere(String senhaPura, String senhaHash);
}
