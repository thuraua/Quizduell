package com.thurau.quizduell.pkgFirebase;

import com.thurau.quizduell.pkgData.Quiz;

import java.util.ArrayList;

public interface FirebaseActionListener {
    void onReadCompleted(ArrayList<Quiz> message);

    void alertListener(String message);

}
