package com.workmanager.view;

public interface ProgressListener {

        void onProgress(long progress, long total, boolean done);
}
