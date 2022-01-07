package com.example.filechecker.logger;

import androidx.annotation.NonNull;

import java.io.File;

public interface DiagModule {

    /**
     * @return true if a log file has successfully been created and we are able to write to it
     */
    boolean isRecordingActive();
    void addDiagnosticsEntry(@NonNull String category, @NonNull String message);
    /**
     * Connect attempts to initialise a file and get the diagnostics module into an active state
     */
    void connect();
    /**
     * Immediately commit any outstanding buffered entries to the log file then remove access from the
     * file, to render i unlocked. This should be called prior to sending any logs to support or deleting.
     *
     * @param shouldDelete if true the written will will be deleted
     */
    void disconnect(boolean shouldDelete);
    /**
     * @return the last/current file the diag module expects to exist
     */
    @NonNull
    File getKnownFile();
}