package com.thurau.quizduell.pkgData;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

public class Question implements Serializable {
    private String text;
    private List<String> possibleAnswers=new ArrayList<>();
    private int rightAnwersIdx;

    public Question(String text, List<String> possibleAnswers, int rightAnwersIdx) {
        this.text = text;
        this.possibleAnswers = possibleAnswers;
        this.rightAnwersIdx = rightAnwersIdx;
    }

    public String getText() {
        return text;
    }

    public void setText(String text) {
        this.text = text;
    }

    public List<String> getPossibleAnswers() {
        return possibleAnswers;
    }

    public void setPossibleAnswers(List<String> possibleAnswers) {
        this.possibleAnswers = possibleAnswers;
    }

    public int getRightAnwersIdx() {
        return rightAnwersIdx;
    }

    public void setRightAnwersIdx(int rightAnwersIdx) {
        this.rightAnwersIdx = rightAnwersIdx;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Question question = (Question) o;
        return rightAnwersIdx == question.rightAnwersIdx &&
                Objects.equals(text, question.text) &&
                Objects.equals(possibleAnswers, question.possibleAnswers);
    }

    @Override
    public int hashCode() {
        return Objects.hash(text, possibleAnswers, rightAnwersIdx);
    }

    @Override
    public String toString() {
        return "Question{" +
                "text='" + text + '\'' +
                ", possibleAnswers=" + possibleAnswers +
                ", rightAnwersIdx=" + rightAnwersIdx +
                '}';
    }
}