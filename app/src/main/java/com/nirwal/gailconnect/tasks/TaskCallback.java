package com.nirwal.gailconnect.tasks;

public interface TaskCallback<T> {
    void onComplete(Result result);
}
