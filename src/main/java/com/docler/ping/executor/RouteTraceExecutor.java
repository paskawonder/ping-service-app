package com.docler.ping.executor;

import com.docler.ping.utils.ExternalExecutionHelper;
import javax.inject.Inject;
import javax.inject.Named;
import javax.inject.Singleton;

@Singleton
public class RouteTraceExecutor implements Executor {

    private final String command;

    private final ExternalExecutionHelper externalExecutionHelper;

    @Inject
    public RouteTraceExecutor(@Named("route.trace.command") final String command,
                              final ExternalExecutionHelper externalExecutionHelper) {
        this.command = command + " ";
        this.externalExecutionHelper = externalExecutionHelper;
    }

    @Override
    public String execute(final String uri) {
        return externalExecutionHelper.exec(command + uri);
    }

}
