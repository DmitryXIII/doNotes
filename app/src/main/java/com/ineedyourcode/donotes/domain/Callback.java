package com.ineedyourcode.donotes.domain;

public interface Callback<T> {

    void onSuccess(T result);

    void onError(Throwable error);
}
