package com.docler.ping.executor;

import lombok.SneakyThrows;

import java.io.BufferedReader;
import java.io.InputStreamReader;

public class RouteTraceExecutor implements Executor {

    private static final String LINE_SEPARATOR = System.lineSeparator();

    private final String command;

    private final Runtime runtime;

    public RouteTraceExecutor(final String command, final Runtime runtime) {
        this.command = command + " ";
        this.runtime = runtime;
    }

    @SneakyThrows
    @Override
    public String execute(final String uri) {
        final String pingCmd = command + uri;
        final Process process = runtime.exec(pingCmd);
        final StringBuilder pingResult = new StringBuilder();
        try (final BufferedReader in = new BufferedReader(new InputStreamReader(process.getInputStream()))) {
            String inputLine;
            while ((inputLine = in.readLine()) != null) {
                pingResult.append(inputLine).append(LINE_SEPARATOR);
            }
        }
        pingResult.delete(pingResult.lastIndexOf(LINE_SEPARATOR), pingResult.length());
        return pingResult.toString().trim();
    }

}
