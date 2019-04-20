package com.thurau.quizduell.pkgData;

import java.util.Objects;

public class GivenAnswer {
    private int playerId;
    private boolean right;

    public GivenAnswer(int playerId, boolean right) {
        this.playerId = playerId;
        this.right = right;
    }

    public GivenAnswer() {
    }

    public int getPlayerId() {
        return playerId;
    }

    public void setPlayerId(int playerId) {
        this.playerId = playerId;
    }

    public boolean isRight() {
        return right;
    }

    public void setRight(boolean right) {
        this.right = right;
    }

    @Override
    public String toString() {
        return "GivenAnswer{" +
                "playerId=" + playerId +
                ", right=" + right +
                '}';
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        GivenAnswer that = (GivenAnswer) o;
        return playerId == that.playerId &&
                right == that.right;
    }

    @Override
    public int hashCode() {
        return Objects.hash(playerId, right);
    }
}
