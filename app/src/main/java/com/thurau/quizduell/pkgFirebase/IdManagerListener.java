package com.thurau.quizduell.pkgFirebase;

import com.thurau.quizduell.pkgData.Quiz;

public interface IdManagerListener {
    void assignCurrentPlayerId(int id);
    void assignEnemyId(int id, Quiz q);
    void err(String message);
}
