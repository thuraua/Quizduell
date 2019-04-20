package com.thurau.quizduell.pkgFirebase;

import java.util.Objects;

public class GameResult {
    private int myCntRightAnswers = -1;
    private int enemyCntRightAnswers = -1;
    private int enemyDuration = -1;

    public GameResult() {
    }

    public int getMyCntRightAnswers() {
        return myCntRightAnswers;
    }

    public void setMyCntRightAnswers(int myCntRightAnswers) {
        this.myCntRightAnswers = myCntRightAnswers;
    }

    public int getEnemyCntRightAnswers() {
        return enemyCntRightAnswers;
    }

    public void setEnemyCntRightAnswers(int enemyCntRightAnswers) {
        this.enemyCntRightAnswers = enemyCntRightAnswers;
    }

    public int getEnemyDuration() {
        return enemyDuration;
    }

    public void setEnemyDuration(int enemyDuration) {
        this.enemyDuration = enemyDuration;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        GameResult that = (GameResult) o;
        return myCntRightAnswers == that.myCntRightAnswers &&
                enemyCntRightAnswers == that.enemyCntRightAnswers &&
                enemyDuration == that.enemyDuration;
    }

    @Override
    public int hashCode() {
        return Objects.hash(myCntRightAnswers, enemyCntRightAnswers, enemyDuration);
    }

    @Override
    public String toString() {
        return "GameResult{" +
                "myCntRightAnswers=" + myCntRightAnswers +
                ", enemyCntRightAnswers=" + enemyCntRightAnswers +
                ", enemyDuration=" + enemyDuration +
                '}';
    }
}
