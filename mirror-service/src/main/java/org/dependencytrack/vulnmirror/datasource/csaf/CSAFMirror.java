package org.dependencytrack.vulnmirror.datasource.csaf;

import org.dependencytrack.vulnmirror.datasource.AbstractDatasourceMirror;

import java.util.concurrent.Future;

public class CSAFMirror extends AbstractDatasourceMirror<CSAFMirrorState> {
    @Override
    public Future<?> doMirror(String ecosystem) {
        return null;
    }
}
