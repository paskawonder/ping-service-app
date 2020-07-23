package ping.executor;

import com.docler.ping.executor.Executor;
import com.docler.ping.executor.ExecutorFactory;
import com.docler.ping.executor.ICMPPingExecutor;
import com.docler.ping.executor.RouteTraceExecutor;
import com.docler.ping.executor.TCPPingExecutor;
import com.docler.ping.model.Operation;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;

public final class ExecutorFactoryTest {

    private final ICMPPingExecutor icmpPingExecutor;

    private final TCPPingExecutor tcpPingExecutor;

    private final RouteTraceExecutor routeTraceExecutor;

    private final ExecutorFactory executorFactory;

    public ExecutorFactoryTest() {
        this.icmpPingExecutor = Mockito.mock(ICMPPingExecutor.class);
        this.tcpPingExecutor = Mockito.mock(TCPPingExecutor.class);
        this.routeTraceExecutor = Mockito.mock(RouteTraceExecutor.class);
        this.executorFactory = new ExecutorFactory(icmpPingExecutor, tcpPingExecutor, routeTraceExecutor);
    }

    @Test
    public void getExecutorTest_icmpPing() {
        final Executor actual = executorFactory.getExecutor(Operation.ICMP_PING);
        Assertions.assertEquals(icmpPingExecutor, actual);
    }

    @Test
    public void getExecutorTest_tcpPing() {
        final Executor actual = executorFactory.getExecutor(Operation.TCP_PING);
        Assertions.assertEquals(tcpPingExecutor, actual);
    }

    @Test
    public void getExecutorTest_routeTrace() {
        final Executor actual = executorFactory.getExecutor(Operation.ROUTE_TRACE);
        Assertions.assertEquals(routeTraceExecutor, actual);
    }

    @Test
    public void getExecutorTest_unsupported() {
        Assertions.assertThrows(UnsupportedOperationException.class, () -> executorFactory.getExecutor(null));
    }

}
