package ping.executor;

import com.docler.ping.executor.TCPPingExecutor;
import com.github.tomakehurst.wiremock.WireMockServer;
import com.github.tomakehurst.wiremock.client.WireMock;
import com.github.tomakehurst.wiremock.core.WireMockConfiguration;
import com.github.tomakehurst.wiremock.http.Fault;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.io.IOException;

import static com.github.tomakehurst.wiremock.client.WireMock.aResponse;
import static com.github.tomakehurst.wiremock.client.WireMock.okJson;

final class TCPPingExecutorTest {

    private static final String URI = "http://localhost:18081";

    private final TCPPingExecutor tcpPingExecutor;

    private final WireMockServer wireMockServer;

    TCPPingExecutorTest() {
        this.tcpPingExecutor = new TCPPingExecutor(1000);
        this.wireMockServer = new WireMockServer(WireMockConfiguration.options().port(18081));
    }

    @BeforeEach
    void init() {
        wireMockServer.start();
    }

    @AfterEach
    void tearDown() {
        wireMockServer.stop();
    }

    @Test
    void executeTest_200() {
        wireMockServer.stubFor(WireMock.get("/").willReturn(okJson("").withStatus(200).withFixedDelay(500)));
        final String actual = tcpPingExecutor.execute(URI);
        Assertions.assertTrue(actual.contains("{\"url\":\"http://localhost:18081\","));
        Assertions.assertTrue(actual.contains("\"httpStatus\":\"200\""));
    }

    @Test
    void executeTest_500() {
        wireMockServer.stubFor(WireMock.get("/").willReturn(aResponse().withStatus(500).withFixedDelay(500)));
        final String actual = tcpPingExecutor.execute(URI);
        Assertions.assertTrue(actual.contains("{\"url\":\"http://localhost:18081\","));
        Assertions.assertTrue(actual.contains("\"httpStatus\":\"500\""));

    }

    @Test
    void executeTest_error() {
        wireMockServer.stubFor(WireMock.get("/").willReturn(aResponse().withFault(Fault.EMPTY_RESPONSE)));
        Assertions.assertThrows(IOException.class, () -> tcpPingExecutor.execute(URI));
    }

}
