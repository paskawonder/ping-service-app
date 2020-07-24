package ping.utils;

import com.docler.ping.utils.ExternalExecutionHelper;
import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;

final class ExternalExecutionHelperTest {

    private static final String RESULT_RESULT = "result" + System.lineSeparator() + "result";

    private static final String COMMAND = "command";

    private final Runtime runtime;

    private final ExternalExecutionHelper externalExecutionHelper;

    public ExternalExecutionHelperTest() {
        this.runtime = Mockito.mock(Runtime.class);
        this.externalExecutionHelper = new ExternalExecutionHelper(runtime);
    }

    @Test
    void executeTest() throws IOException {
        final Process process = Mockito.mock(Process.class);
        Mockito.when(runtime.exec(COMMAND)).thenReturn(process);
        final ByteArrayInputStream is = new ByteArrayInputStream(StandardCharsets.UTF_8.encode(RESULT_RESULT).array());
        Mockito.when(process.getInputStream()).thenReturn(is);
        final String actual = externalExecutionHelper.exec(COMMAND);
        Assertions.assertEquals(RESULT_RESULT, actual);
    }

    @Test
    void executeTest_Error() throws IOException {
        Mockito.when(runtime.exec(COMMAND)).thenThrow(RuntimeException.class);
        Assertions.assertThrows(RuntimeException.class, () -> externalExecutionHelper.exec(COMMAND));
    }

}
