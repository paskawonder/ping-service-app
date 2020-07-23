package ping.executor;

import com.docler.ping.executor.ICMPPingExecutor;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.nio.charset.StandardCharsets;

public final class ICMPPingExecutorTest {

    private static final String URI = "http://www.google.com/";

    private static final String COMMAND = "command";

    private static final String RESULT_RESULT = "result" + System.lineSeparator() + "result";

    private final Runtime runtime;

    private final ICMPPingExecutor icmpPingExecutor;

    public ICMPPingExecutorTest() {
        this.runtime = Mockito.mock(Runtime.class);
        this.icmpPingExecutor = new ICMPPingExecutor(COMMAND, runtime);
    }

    @Test
    public void executeTest() throws IOException {
        final Process process = Mockito.mock(Process.class);
        Mockito.when(runtime.exec(COMMAND + " " + URI)).thenReturn(process);
        final ByteArrayInputStream is = new ByteArrayInputStream(StandardCharsets.UTF_8.encode(RESULT_RESULT).array());
        Mockito.when(process.getInputStream()).thenReturn(is);
        final String actual = icmpPingExecutor.execute(URI);
        Assertions.assertEquals(RESULT_RESULT, actual);
    }

    @Test
    public void executeTest_Error() throws IOException {
        final Process process = Mockito.mock(Process.class);
        Mockito.when(runtime.exec(COMMAND + " " + URI)).thenReturn(process);
        final ByteArrayInputStream is = new ByteArrayInputStream(StandardCharsets.UTF_8.encode("destination host unreachable").array());
        Mockito.when(process.getInputStream()).thenReturn(is);
        Assertions.assertThrows(IllegalStateException.class, () -> icmpPingExecutor.execute(URI));
        Mockito.when(runtime.exec(COMMAND + " " + URI)).thenThrow(RuntimeException.class);
        Assertions.assertThrows(RuntimeException.class, () -> icmpPingExecutor.execute(URI));
    }

}
