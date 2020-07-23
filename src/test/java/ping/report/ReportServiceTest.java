package ping.report;

import com.docler.ping.converter.ExecutionResultEntityReportConverter;
import com.docler.ping.data.DataService;
import com.docler.ping.model.ExecutionResultEntity;
import com.docler.ping.model.Operation;
import com.docler.ping.model.Report;
import com.docler.ping.report.ReportClient;
import com.docler.ping.report.ReportService;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;

import java.util.Map;

final class ReportServiceTest {

    private static final String URI = "uri";

    private static final String ICMP_PING = "icmpPing";

    private static final String TCP_PING = "tcpPing";

    private static final String ROUTE_TRACE = "trace";

    private final DataService dataService;

    private final ReportClient reportClient;

    private final ReportService reportService;

    private final ExecutionResultEntityReportConverter executionResultEntityReportConverter;

    ReportServiceTest() {
        this.dataService = Mockito.mock(DataService.class);
        this.reportClient = Mockito.mock(ReportClient.class);
        this.executionResultEntityReportConverter = Mockito.mock(ExecutionResultEntityReportConverter.class);
        this.reportService = new ReportService(dataService, reportClient, executionResultEntityReportConverter);
    }

    @Test
    void report() {
        final long timestamp = System.currentTimeMillis();
        final Map<Operation, ExecutionResultEntity> data = Map.of(
                Operation.ICMP_PING, new ExecutionResultEntity(timestamp, ICMP_PING),
                Operation.TCP_PING, new ExecutionResultEntity(timestamp, TCP_PING),
                Operation.ROUTE_TRACE, new ExecutionResultEntity(timestamp, ROUTE_TRACE)
        );
        Mockito.when(dataService.get(URI)).thenReturn(data);
        final Report report = new Report(URI, ICMP_PING, TCP_PING, ROUTE_TRACE);
        Mockito.when(executionResultEntityReportConverter.convert(URI, data)).thenReturn(report);
        reportService.report(URI);
        Mockito.verify(reportClient, Mockito.times(1)).submit(report);
    }

}
