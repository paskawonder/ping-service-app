package ping.executor;

import com.docler.ping.executor.ICMPPingExecutor;
import com.docler.ping.utils.ExternalExecutionHelper;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;

final class ICMPPingExecutorTest {

    private static final String URI = "http://www.google.com/";

    private static final String COMMAND = "command";

    private static final String RESULT_RESULT = "result" + System.lineSeparator() + "result";

    private final ExternalExecutionHelper externalExecutionHelper;

    private final ICMPPingExecutor icmpPingExecutor;

    ICMPPingExecutorTest() {
        this.externalExecutionHelper = Mockito.mock(ExternalExecutionHelper.class);
        this.icmpPingExecutor = new ICMPPingExecutor(COMMAND, externalExecutionHelper);
    }

    @Test
    void executeTest() {
        Mockito.when(externalExecutionHelper.exec(COMMAND + " " + URI)).thenReturn(RESULT_RESULT);
        final String actual = icmpPingExecutor.execute(URI);
        Assertions.assertEquals(RESULT_RESULT, actual);
    }

    @Test
    void executeTest_Error() {
        Mockito.when(externalExecutionHelper.exec(COMMAND + " " + URI)).thenReturn("destination host unreachable");
        Assertions.assertThrows(IllegalStateException.class, () -> icmpPingExecutor.execute(URI));
        Mockito.when(externalExecutionHelper.exec(COMMAND + " " + URI)).thenThrow(RuntimeException.class);
        Assertions.assertThrows(RuntimeException.class, () -> icmpPingExecutor.execute(URI));
    }

}
