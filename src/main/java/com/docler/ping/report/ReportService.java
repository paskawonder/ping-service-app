package com.docler.ping.report;

import com.docler.ping.converter.ExecutionResultEntityReportConverter;
import com.docler.ping.data.DataService;
import com.docler.ping.model.ExecutionResultEntity;
import com.docler.ping.model.Operation;
import com.docler.ping.model.Report;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import javax.inject.Inject;
import java.util.Map;

public class ReportService {

    private static final Logger LOG = LogManager.getLogger(ReportService.class);

    private final DataService dataService;

    private final ReportClient reportClient;

    private final ExecutionResultEntityReportConverter executionResultEntityReportConverter;

    @Inject
    public ReportService(final DataService dataService, final ReportClient reportClient,
                         final ExecutionResultEntityReportConverter executionResultEntityReportConverter) {
        this.dataService = dataService;
        this.reportClient = reportClient;
        this.executionResultEntityReportConverter = executionResultEntityReportConverter;
    }

    public void report(final String id) {
        final Map<Operation, ExecutionResultEntity> data = dataService.get(id);
        final Report report = executionResultEntityReportConverter.convert(id, data);
        LOG.warn(report);
        reportClient.submit(report);
    }

}
