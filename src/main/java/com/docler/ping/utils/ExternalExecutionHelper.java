package com.docler.ping.utils;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import javax.inject.Inject;
import lombok.SneakyThrows;

public class ExternalExecutionHelper {

    private static final String LINE_SEPARATOR = System.lineSeparator();

    private final Runtime runtime;

    @Inject
    public ExternalExecutionHelper(final Runtime runtime) {
        this.runtime = runtime;
    }

    @SneakyThrows
    public String exec(final String command) {
        final Process process = runtime.exec(command);
        final StringBuilder result = new StringBuilder();
        try (final BufferedReader in = new BufferedReader(new InputStreamReader(process.getInputStream()))) {
            String inputLine;
            while ((inputLine = in.readLine()) != null) {
                result.append(inputLine).append(LINE_SEPARATOR);
            }
        }
        result.delete(result.lastIndexOf(LINE_SEPARATOR), result.length());
        return result.toString().trim();
    }

}
