package ping.data;

import com.docler.ping.data.DataService;
import com.docler.ping.model.ExecutionResultEntity;
import com.docler.ping.model.Operation;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import java.util.Map;

public final class DataServiceTest {

    private static final String URI = "uri";

    private static final String ICMP_PING = "icmpPing";

    private static final String TCP_PING = "tcpPing";

    private static final String ROUTE_TRACE = "trace";

    private final DataService dataService = new DataService();

    @Test
    public void persistAndGetTest() {
        dataService.persist(URI, Operation.ICMP_PING, new ExecutionResultEntity(0, ICMP_PING));
        Map<Operation, ExecutionResultEntity> expected = Map.of(Operation.ICMP_PING, new ExecutionResultEntity(0, ICMP_PING));
        Map<Operation, ExecutionResultEntity> actual = dataService.get(URI);
        Assertions.assertEquals(expected, actual);
        dataService.persist(URI, Operation.TCP_PING, new ExecutionResultEntity(0, TCP_PING));
        dataService.persist(URI, Operation.ROUTE_TRACE, new ExecutionResultEntity(0, ROUTE_TRACE));
        expected = Map.of(
                Operation.ICMP_PING, new ExecutionResultEntity(0, ICMP_PING),
                Operation.TCP_PING, new ExecutionResultEntity(0, TCP_PING),
                Operation.ROUTE_TRACE, new ExecutionResultEntity(0, ROUTE_TRACE)
        );
        actual = dataService.get(URI);
        Assertions.assertEquals(expected, actual);
    }

}
