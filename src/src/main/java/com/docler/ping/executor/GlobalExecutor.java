package com.docler.ping.executor;

import com.docler.ping.data.DataService;
import com.docler.ping.model.ExecutionResultEntity;
import com.docler.ping.model.Operation;
import com.docler.ping.model.Task;
import com.docler.ping.report.ReportService;
import com.docler.ping.utils.Clock;
import com.google.common.util.concurrent.Striped;

import javax.inject.Inject;
import java.util.List;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.locks.Lock;

public final class GlobalExecutor {

    private final ExecutorFactory executorFactory;

    private final DataService dataService;

    private final ReportService reportService;

    private final Clock clock;

    @Inject
    public GlobalExecutor(final ExecutorFactory executorFactory, final DataService dataService,
                          final ReportService reportService, final Clock clock) {
        this.executorFactory = executorFactory;
        this.dataService = dataService;
        this.reportService = reportService;
        this.clock = clock;
    }

    public void execute(final List<Task> tasks) {
        final Striped<Lock> locks = Striped.lock(tasks.size());
        final ScheduledExecutorService executorService = Executors.newScheduledThreadPool(tasks.size());
        tasks.forEach(task -> {
            final Operation operation = task.getOperation();
            final Executor executor = executorFactory.getExecutor(operation);
            final Runnable executionTask = () -> {
                final boolean isGreenLight = locks.get(task).tryLock();
                if (isGreenLight) {
                    try {
                        final String executionResult = executor.execute(task.getUri());
                        final long timestamp = clock.timestamp();
                        dataService.persist(task.getId(), operation, new ExecutionResultEntity(timestamp, executionResult));
                    } catch (final Exception ex) {
                        reportService.report(task.getId());
                    } finally {
                        locks.get(task).unlock();
                    }
                }
            };
            executorService.scheduleAtFixedRate(executionTask, 0, task.getPeriod(), TimeUnit.MILLISECONDS);
        });
    }

}
