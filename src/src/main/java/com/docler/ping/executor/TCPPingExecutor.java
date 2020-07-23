package com.docler.ping.executor;

import lombok.AllArgsConstructor;
import lombok.SneakyThrows;

import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.time.Duration;
import java.util.concurrent.TimeUnit;

public class TCPPingExecutor implements Executor {

    private final HttpClient httpClient;

    public TCPPingExecutor(final long timeout) {
        this.httpClient = HttpClient.newBuilder().version(HttpClient.Version.HTTP_1_1).connectTimeout(Duration.ofSeconds(timeout)).build();
    }

    @SneakyThrows
    @Override
    public String execute(final String uri) {
        final HttpRequest request = HttpRequest.newBuilder(URI.create(uri)).GET().build();
        final long start = System.nanoTime();
        final HttpResponse<String> response = httpClient.send(request, HttpResponse.BodyHandlers.ofString());
        final long duration = System.nanoTime() - start;
        final long responseTime = TimeUnit.NANOSECONDS.toMillis(duration);
        final PingResult result = new PingResult(uri, response.statusCode(), responseTime);
        return result.toString();
    }

    @AllArgsConstructor
    private static final class PingResult {

        private final String url;

        private final int httpStatus;

        private final long responseTime;

        @Override
        public String toString() {
            return "{" +
                           "\"url\":\"" + url + "\"," +
                           "\"httpStatus\":\""+ httpStatus + "\"," +
                           "\"duration\":\"" + responseTime + "\"" +
                   "}";
        }

    }

}
