package com.thurau.quizduell.pkgFirebase;

public interface ReadResultListener {
    void onResultReadCompleted(GameResult result);
    void onError(String message);
}
