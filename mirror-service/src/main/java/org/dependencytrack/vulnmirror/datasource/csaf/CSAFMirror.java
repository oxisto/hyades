package org.dependencytrack.vulnmirror.datasource.csaf;

import org.dependencytrack.vulnmirror.datasource.AbstractDatasourceMirror;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.concurrent.ExecutionException;
import java.util.concurrent.Future;
import java.util.concurrent.ExecutorService;

public class CSAFMirror extends AbstractDatasourceMirror<CSAFMirrorState> {

    private static final Logger LOGGER = LoggerFactory.getLogger(CSAFMirror.class);

    private final ExecutorService executorService;
    private final CSAFConfig config;

    public CSAFMirror(ExecutorService executorService, CSAFConfig config) {
        this.executorService = executorService;
        this.config = config;
    }

    @Override
    public Future<?> doMirror(String ecosystem) {
        return executorService.submit(() -> {
        // do the actual import based on the config
            try {
                doImport(config);
            } catch (Exception e) {
                LOGGER.error("Error occured while importing CSAF", e);
            }
        });
    }

    private void doImport(CSAFConfig config) throws ExecutionException, InterruptedException {
        // fetch CSAF form URL, parse it to a Bom
        Bom bom = CSAFImporter.parse(config.url);
        publishIfChanged(bom);
    }
}
