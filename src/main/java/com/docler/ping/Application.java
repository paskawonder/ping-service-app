package com.docler.ping;

import com.docler.ping.config.ApplicationConfig;
import com.docler.ping.executor.GlobalExecutor;
import com.docler.ping.model.Task;
import com.google.inject.Guice;
import com.google.inject.Injector;
import com.google.inject.Key;
import com.google.inject.TypeLiteral;

import java.util.List;

public final class Application {

    public static void main(final String[] args) {
        final Injector injector = Guice.createInjector(new ApplicationConfig());
        final List<Task> tasks = injector.getInstance(Key.get(new TypeLiteral<>() {}));
        final GlobalExecutor globalExecutor = injector.getInstance(GlobalExecutor.class);
        globalExecutor.execute(tasks);
    }

}
