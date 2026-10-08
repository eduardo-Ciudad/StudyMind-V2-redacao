package eduar.studymindredacao.adapter.out.ia;

import java.util.Base64;

sealed interface GeminiParte permits GeminiParte.Texto, GeminiParte.Imagem {

    record Texto(String texto) implements GeminiParte {
        public Texto {
            if (texto == null || texto.isBlank()) {
                throw new IllegalArgumentException("texto da parte é obrigatório");
            }
        }
    }

    record Imagem(String mimeType, String dadosBase64) implements GeminiParte {
        public Imagem {
            if (mimeType == null || mimeType.isBlank()) {
                throw new IllegalArgumentException("mimeType da imagem é obrigatório");
            }
            if (dadosBase64 == null || dadosBase64.isEmpty()) {
                throw new IllegalArgumentException("dados da imagem são obrigatórios");
            }
        }

        static Imagem de(String mimeType, byte[] bytes) {
            if (bytes == null || bytes.length == 0) {
                throw new IllegalArgumentException("dados da imagem são obrigatórios");
            }
            return new Imagem(mimeType, Base64.getEncoder().encodeToString(bytes));
        }

        @Override
        public String toString() {
            return "Imagem[mimeType=" + mimeType + ", tamanhoBase64=" + dadosBase64.length() + "]";
        }
    }
}