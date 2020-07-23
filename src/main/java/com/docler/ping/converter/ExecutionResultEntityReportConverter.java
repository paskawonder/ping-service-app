package com.docler.ping.converter;

import com.docler.ping.model.ExecutionResultEntity;
import com.docler.ping.model.Operation;
import com.docler.ping.model.Report;

import java.util.Map;
import java.util.Optional;

public class ExecutionResultEntityReportConverter {

    public Report convert(final String id, final Map<Operation, ExecutionResultEntity> e) {
        final ExecutionResultEntity icmpPingResult = e.get(Operation.ICMP_PING);
        final ExecutionResultEntity tcpPingResult = e.get(Operation.TCP_PING);
        final ExecutionResultEntity routeTraceResult = e.get(Operation.ROUTE_TRACE);
        final Report.ReportBuilder reportBuilder = Report.builder().host(id);
        Optional.ofNullable(icmpPingResult).ifPresent(r -> reportBuilder.icmpPing(r.getPayload()));
        Optional.ofNullable(tcpPingResult).ifPresent(r -> reportBuilder.tcpPing(r.getPayload()));
        Optional.ofNullable(routeTraceResult).ifPresent(r -> reportBuilder.trace(r.getPayload()));
        return reportBuilder.build();
    }

}
