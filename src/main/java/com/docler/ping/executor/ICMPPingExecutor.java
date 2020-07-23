package com.docler.ping.executor;

import com.docler.ping.utils.ExternalExecutionHelper;
import java.util.stream.Stream;
import javax.inject.Inject;
import javax.inject.Named;

public class ICMPPingExecutor implements Executor {

    private static final String[] ERROR_MSGS = {"destination host unreachable", "request timed out", "ping request could not find host"};

    private final String command;

    private final ExternalExecutionHelper externalExecutionHelper;

    @Inject
    public ICMPPingExecutor(@Named("ping.icmp.command") final String command,
                            final ExternalExecutionHelper externalExecutionHelper) {
        this.command = command + " ";
        this.externalExecutionHelper = externalExecutionHelper;
    }

    @Override
    public String execute(final String uri) {
        final String result = externalExecutionHelper.exec(command + uri);
        final String lowerCaseResult = result.toLowerCase();
        if (Stream.of(ERROR_MSGS).anyMatch(lowerCaseResult::contains)) {
            throw new IllegalStateException();
        }
        return result;
    }

}
