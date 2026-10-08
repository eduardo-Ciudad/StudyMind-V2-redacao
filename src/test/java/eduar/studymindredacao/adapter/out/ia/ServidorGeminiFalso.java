package eduar.studymindredacao.adapter.out.ia;

import com.sun.net.httpserver.HttpExchange;
import com.sun.net.httpserver.HttpServer;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.nio.charset.StandardCharsets;
import java.util.Deque;
import java.util.List;
import java.util.concurrent.ConcurrentLinkedDeque;
import java.util.concurrent.CopyOnWriteArrayList;


final class ServidorGeminiFalso implements AutoCloseable {

    record Requisicao(String caminho, String apiKey, String corpo) {
    }

    private record Resposta(int status, String corpo) {
    }

    private final HttpServer servidor;
    private final Deque<Resposta> respostas = new ConcurrentLinkedDeque<>();
    private final List<Requisicao> recebidas = new CopyOnWriteArrayList<>();

    private ServidorGeminiFalso() throws IOException {
        servidor = HttpServer.create(new InetSocketAddress("127.0.0.1", 0), 0);
        servidor.createContext("/", this::atender);
        servidor.start();
    }

    static ServidorGeminiFalso iniciar() throws IOException {
        return new ServidorGeminiFalso();
    }

    String baseUrl() {
        return "http://127.0.0.1:" + servidor.getAddress().getPort() + "/v1beta";
    }

    void responder(int status, String corpo) {
        respostas.add(new Resposta(status, corpo));
    }

    List<Requisicao> recebidas() {
        return List.copyOf(recebidas);
    }

    @Override
    public void close() {
        servidor.stop(0);
    }

    private void atender(HttpExchange troca) throws IOException {
        String corpo = new String(troca.getRequestBody().readAllBytes(), StandardCharsets.UTF_8);
        recebidas.add(new Requisicao(
                troca.getRequestURI().getPath(), troca.getRequestHeaders().getFirst("x-goog-api-key"), corpo
        ));
        Resposta resposta = respostas.isEmpty() ? new Resposta(500, "{}") : respostas.poll();
        byte[] bytes = resposta.corpo().getBytes(StandardCharsets.UTF_8);
        troca.getResponseHeaders().add("Content-Type", "application/json");
        troca.sendResponseHeaders(resposta.status(), bytes.length);
        troca.getResponseBody().write(bytes);
        troca.close();
    }
}