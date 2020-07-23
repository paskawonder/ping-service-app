package com.docler.ping.report;

import com.docler.ping.model.Report;
import lombok.SneakyThrows;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.time.Duration;

public class ReportClient {

    private static final Logger LOG = LogManager.getLogger(ReportService.class);

    private final HttpClient httpClient;

    private final URI uri;

    public ReportClient(final String baseUri, final long timeout) {
        this.httpClient = HttpClient.newBuilder().version(HttpClient.Version.HTTP_1_1).connectTimeout(Duration.ofSeconds(timeout)).build();
        this.uri = URI.create(baseUri);
    }

    @SneakyThrows
    public void submit(final Report report) {
        final String requestBody = report.toString();
        final HttpRequest request = HttpRequest.newBuilder(uri).POST(HttpRequest.BodyPublishers.ofString(requestBody)).build();
        try {
            httpClient.send(request, HttpResponse.BodyHandlers.ofString());
        } catch (final Exception e) {
            LOG.error("Report delivery has failed. Consider the usage of a message broker instead.");
        }
    }

}
