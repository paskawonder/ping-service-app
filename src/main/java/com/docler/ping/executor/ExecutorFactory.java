package com.docler.ping.executor;

import com.docler.ping.model.Operation;

import javax.inject.Inject;

public class ExecutorFactory {

    private final ICMPPingExecutor icmpPingExecutor;

    private final TCPPingExecutor tcpPingExecutor;

    private final RouteTraceExecutor routeTraceExecutor;

    @Inject
    public ExecutorFactory(final ICMPPingExecutor icmpPingExecutor, final TCPPingExecutor tcpPingExecutor,
                           final RouteTraceExecutor routeTraceExecutor) {
        this.icmpPingExecutor = icmpPingExecutor;
        this.tcpPingExecutor = tcpPingExecutor;
        this.routeTraceExecutor = routeTraceExecutor;
    }

    public Executor getExecutor(final Operation operation) {
        final Executor executor;
        if (Operation.ICMP_PING.equals(operation)) {
            executor = icmpPingExecutor;
        } else if (Operation.TCP_PING.equals(operation)) {
            executor = tcpPingExecutor;
        } else if (Operation.ROUTE_TRACE.equals(operation)) {
            executor = routeTraceExecutor;
        } else  {
            throw new UnsupportedOperationException();
        }
        return executor;
    }

}
