package eduar.studymindredacao.application.usecase;

import eduar.studymindredacao.domain.port.SenhaEncoderPort;

class SenhaEncoderFake implements SenhaEncoderPort {
    @Override
    public String codificar(String senhaPura) {
        return "hash:" + senhaPura;
    }

    @Override
    public boolean confere(String senhaPura, String senhaHash) {
        return ("hash:" + senhaPura).equals(senhaHash);
    }
}
