package ping.executor;

import com.docler.ping.executor.RouteTraceExecutor;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.nio.charset.StandardCharsets;

public final class RouteTraceExecutorTest {

    private static final String URI = "http://www.google.com/";

    private static final String COMMAND = "command";

    private static final String RESULT_RESULT = "result" + System.lineSeparator() + "result";

    private final Runtime runtime;

    private final RouteTraceExecutor routeTraceExecutor;

    public RouteTraceExecutorTest() {
        this.runtime = Mockito.mock(Runtime.class);
        this.routeTraceExecutor = new RouteTraceExecutor(COMMAND, runtime);
    }

    @Test
    public void executeTest() throws IOException {
        final Process process = Mockito.mock(Process.class);
        Mockito.when(runtime.exec(COMMAND + " " + URI)).thenReturn(process);
        final ByteArrayInputStream is = new ByteArrayInputStream(StandardCharsets.UTF_8.encode(RESULT_RESULT).array());
        Mockito.when(process.getInputStream()).thenReturn(is);
        final String actual = routeTraceExecutor.execute(URI);
        Assertions.assertEquals(RESULT_RESULT, actual);
    }

    @Test
    public void executeTest_Error() throws IOException {
        Mockito.when(runtime.exec(COMMAND + " " + URI)).thenThrow(RuntimeException.class);
        Assertions.assertThrows(RuntimeException.class, () -> routeTraceExecutor.execute(URI));
    }

}
