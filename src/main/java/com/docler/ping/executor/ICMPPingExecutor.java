package com.docler.ping.executor;

import lombok.SneakyThrows;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.util.stream.Stream;

public class ICMPPingExecutor implements Executor {

    private static final String LINE_SEPARATOR = System.lineSeparator();

    private static final String[] ERROR_MSGS = {"destination host unreachable", "request timed out", "ping request could not find host"};

    private final String command;

    private final Runtime runtime;

    public ICMPPingExecutor(final String command, final Runtime runtime) {
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
        final String result = pingResult.toString().trim();
        final String lowerCaseResult = result.toLowerCase();
        if (Stream.of(ERROR_MSGS).anyMatch(lowerCaseResult::contains)) {
            throw new IllegalStateException();
        }
        return result;
    }

}
