package ping.converter;

import com.docler.ping.converter.ExecutionResultEntityReportConverter;
import com.docler.ping.model.ExecutionResultEntity;
import com.docler.ping.model.Operation;
import com.docler.ping.model.Report;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import java.util.Collections;
import java.util.Map;

public class ExecutionResultEntityReportConverterTest {

    private static final String ID = "id";

    private static final String ICMP_PING = "icmpPing";

    private static final String TCP_PING = "tcpPing";

    private static final String ROUTE_TRACE = "trace";

    private final ExecutionResultEntityReportConverter subject = new ExecutionResultEntityReportConverter();

    @Test
    public void convertTest() {
        final long timestamp = System.currentTimeMillis();
        Map<Operation, ExecutionResultEntity> data = Map.of(
                Operation.ICMP_PING, new ExecutionResultEntity(timestamp, ICMP_PING),
                Operation.TCP_PING, new ExecutionResultEntity(timestamp, TCP_PING),
                Operation.ROUTE_TRACE, new ExecutionResultEntity(timestamp, ROUTE_TRACE)
        );
        Report expected = new Report(ID, ICMP_PING, TCP_PING, ROUTE_TRACE);
        Report actual = subject.convert(ID, data);
        Assertions.assertEquals(expected, actual);
        data = Collections.emptyMap();
        expected = new Report(ID, null, null, null);
        actual = subject.convert(ID, data);
        Assertions.assertEquals(expected, actual);
    }

}
