package ping.executor;

import com.docler.ping.data.DataService;
import com.docler.ping.executor.Executor;
import com.docler.ping.executor.ExecutorFactory;
import com.docler.ping.executor.GlobalExecutor;
import com.docler.ping.model.ExecutionResultEntity;
import com.docler.ping.model.Operation;
import com.docler.ping.model.Task;
import com.docler.ping.report.ReportService;
import com.docler.ping.utils.Clock;
import java.util.List;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;

final class GlobalExecutorTest {

    private static final String ID = "google.com";

    private static final String URI = "http://www.google.com/";

    private static final String RESULT_RESULT = "result" + System.lineSeparator() + "result";

    private final ExecutorFactory executorFactory;

    private final DataService dataService;

    private final ReportService reportService;

    private final GlobalExecutor globalExecutor;

    GlobalExecutorTest() {
        this.executorFactory = Mockito.mock(ExecutorFactory.class);
        this.dataService = Mockito.mock(DataService.class);
        this.reportService = Mockito.mock(ReportService.class);
        final Clock clock = Mockito.mock(Clock.class);
        Mockito.when(clock.timestamp()).thenReturn(0L);
        this.globalExecutor = new GlobalExecutor(executorFactory, dataService, reportService, clock);
    }

    @Test
    void executeTest() {
        final List<Task> tasks = List.of(
                new Task(ID, URI, Operation.ICMP_PING, 1000)
        );
        final Executor executor = Mockito.mock(Executor.class);
        Mockito.when(executorFactory.getExecutor(Operation.ICMP_PING)).thenReturn(executor);
        Mockito.when(executorFactory.getExecutor(Operation.TCP_PING)).thenReturn(executor);
        Mockito.when(executor.execute(URI)).thenReturn(RESULT_RESULT);
        globalExecutor.execute(tasks);
        Mockito.verify(dataService, Mockito.timeout(500).times(1)).persist(ID, Operation.ICMP_PING, new ExecutionResultEntity(0, RESULT_RESULT));
        Mockito.verify(reportService, Mockito.timeout(500).times(0)).report(Mockito.anyString());
    }

    @Test
    void executeTest_skipping() {
        final List<Task> tasks = List.of(
                new Task(ID, URI, Operation.ICMP_PING, 98765),
                new Task(ID, URI, Operation.ICMP_PING, 98765)
        );
        final Executor executor = Mockito.mock(Executor.class);
        Mockito.when(executorFactory.getExecutor(Operation.ICMP_PING)).thenReturn(executor);
        Mockito.when(executor.execute(URI)).thenReturn(RESULT_RESULT);
        globalExecutor.execute(tasks);
        Mockito.verify(dataService, Mockito.timeout(1234).times(1)).persist(ID, Operation.ICMP_PING, new ExecutionResultEntity(0, RESULT_RESULT));
        Mockito.verify(reportService, Mockito.timeout(1234).times(0)).report(Mockito.anyString());
    }

    @Test
    void executeTest_report() {
        final List<Task> tasks = List.of(
                new Task(ID, URI, Operation.ICMP_PING, 1000)
        );
        final Executor executor = Mockito.mock(Executor.class);
        Mockito.when(executorFactory.getExecutor(Operation.ICMP_PING)).thenReturn(executor);
        Mockito.when(executorFactory.getExecutor(Operation.TCP_PING)).thenReturn(executor);
        Mockito.when(executorFactory.getExecutor(Operation.ROUTE_TRACE)).thenReturn(executor);
        Mockito.when(executor.execute(URI)).thenThrow(RuntimeException.class);
        globalExecutor.execute(tasks);
        Mockito.verify(dataService, Mockito.timeout(500).times(0)).persist(Mockito.anyString(), Mockito.any(Operation.class), Mockito.any(ExecutionResultEntity.class));
        Mockito.verify(reportService, Mockito.timeout(500).times(1)).report(ID);
    }

}
