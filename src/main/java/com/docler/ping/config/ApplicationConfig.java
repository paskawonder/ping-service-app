package com.docler.ping.config;

import com.docler.ping.executor.ICMPPingExecutor;
import com.docler.ping.executor.RouteTraceExecutor;
import com.docler.ping.executor.TCPPingExecutor;
import com.docler.ping.model.Task;
import com.docler.ping.report.ReportClient;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.google.inject.AbstractModule;
import com.google.inject.TypeLiteral;
import lombok.SneakyThrows;
import org.apache.commons.configuration2.Configuration;
import org.apache.commons.configuration2.builder.fluent.Configurations;

import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.List;

public final class ApplicationConfig extends AbstractModule {

    private static final String TASK_FILE_LOCATION = "task.file.location";

    private static final String PING_ICMP_COMMAND = "ping.icmp.command";

    private static final String PING_TCP_TIMEOUT = "ping.tcp.timeout";

    private static final String ROUTE_TRACE_COMMAND = "route.trace.command";

    private static final String REPORT_TIMEOUT = "report.timeout";

    private static final String REPORT_URI = "report.uri";

    private static final ObjectMapper OBJECT_MAPPER = new ObjectMapper();

    private static final TypeReference<List<Task>> LIST_TASK_TYPE_REFERENCE = new TypeReference<>() {};

    @SneakyThrows
    @Override
    protected void configure() {
        final Runtime runtime = Runtime.getRuntime();
        final Configuration properties = new Configurations().properties("application.properties");
        bind(ICMPPingExecutor.class).toInstance(new ICMPPingExecutor(properties.getString(PING_ICMP_COMMAND), runtime));
        bind(TCPPingExecutor.class).toInstance(new TCPPingExecutor(properties.getLong(PING_TCP_TIMEOUT)));
        bind(RouteTraceExecutor.class).toInstance(new RouteTraceExecutor(properties.getString(ROUTE_TRACE_COMMAND), runtime));
        bind(ReportClient.class).toInstance(new ReportClient(properties.getString(REPORT_URI), properties.getLong(REPORT_TIMEOUT)));
        final String tasksJson = Files.readString(Paths.get(properties.getString(TASK_FILE_LOCATION)));
        final List<Task> tasks = OBJECT_MAPPER.readValue(tasksJson, LIST_TASK_TYPE_REFERENCE);
        bind(new TypeLiteral<List<Task>>() {}).toInstance(tasks);
    }

}