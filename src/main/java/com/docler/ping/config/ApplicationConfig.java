package com.docler.ping.config;

import com.docler.ping.model.Task;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.google.inject.AbstractModule;
import com.google.inject.TypeLiteral;
import com.google.inject.name.Names;
import java.io.FileReader;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.List;
import java.util.Properties;
import lombok.SneakyThrows;

public final class ApplicationConfig extends AbstractModule {

    private static final String TASK_FILE_LOCATION = "task.file.location";

    private static final ObjectMapper OBJECT_MAPPER = new ObjectMapper();

    private static final TypeReference<List<Task>> LIST_TASK_TYPE_REFERENCE = new TypeReference<>() {};

    @SneakyThrows
    @Override
    protected void configure() {
        final Properties properties = new Properties();
        properties.load(new FileReader("application.properties"));
        Names.bindProperties(binder(), properties);
        bind(Runtime.class).toInstance(Runtime.getRuntime());
        final String tasksJson = Files.readString(Paths.get(properties.getProperty(TASK_FILE_LOCATION)));
        final List<Task> tasks = OBJECT_MAPPER.readValue(tasksJson, LIST_TASK_TYPE_REFERENCE);
        bind(new TypeLiteral<List<Task>>() {}).toInstance(tasks);
    }

}