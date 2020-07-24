package ping.report;

import com.docler.ping.model.Report;
import com.docler.ping.report.ReportClient;
import com.github.tomakehurst.wiremock.WireMockServer;
import com.github.tomakehurst.wiremock.client.WireMock;
import com.github.tomakehurst.wiremock.core.WireMockConfiguration;
import com.github.tomakehurst.wiremock.http.Fault;
import com.github.tomakehurst.wiremock.matching.RequestPatternBuilder;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static com.github.tomakehurst.wiremock.client.WireMock.aResponse;

final class ReportClientTest {

    private static final String URI = "http://localhost:18081";

    private static final String ICMP_PING = "icmpPing";

    private static final String TCP_PING = "tcpPing";

    private static final String TRACE = "trace";

    private final ReportClient reportClient;

    private final WireMockServer wireMockServer;

    ReportClientTest() {
        this.reportClient = new ReportClient(URI, 1000);
        this.wireMockServer = new WireMockServer(WireMockConfiguration.options().port(18081));
    }

    @BeforeEach
    void init() {
        this.wireMockServer.start();
    }

    @AfterEach
    void tearDown() {
        this.wireMockServer.stop();
    }

    @Test
    void submitTest() {
        final Report report = new Report(URI, ICMP_PING, TCP_PING, TRACE);
        wireMockServer.stubFor(WireMock.post("/").willReturn(aResponse().withStatus(200)));
        reportClient.submit(report);
        wireMockServer.verify(
                1,
                RequestPatternBuilder.newRequestPattern().withRequestBody(WireMock.containing("{\"host\":\"http://localhost:18081\",\"icmp_ping\":\"icmpPing\",\"tcp_ping\":\"tcpPing\",\"trace\":\"trace\"}"))
        );
    }

    @Test
    void submitTest_error() {
        final Report report = new Report(URI, ICMP_PING, TCP_PING, TRACE);
        wireMockServer.stubFor(WireMock.post("/").willReturn(aResponse().withFault(Fault.EMPTY_RESPONSE)));
        reportClient.submit(report);
        wireMockServer.verify(
                1,
                RequestPatternBuilder.newRequestPattern().withRequestBody(WireMock.containing("{\"host\":\"http://localhost:18081\",\"icmp_ping\":\"icmpPing\",\"tcp_ping\":\"tcpPing\",\"trace\":\"trace\"}"))
        );
    }

}
