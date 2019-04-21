package com.thurau.quizduell.pkgData;

import android.support.v7.util.SortedList;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Objects;
import java.util.Set;

public class Quiz implements Serializable, Comparable<Quiz> {
    private String name;
    private List<Question> collQuestions=new ArrayList<>();

    public Quiz(String name) {
        this.name = name;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public List<Question> getCollQuestions() {
        return collQuestions;
    }

    public void setCollQuestions(List<Question> collQuestions) {
        this.collQuestions = collQuestions;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Quiz quiz = (Quiz) o;
        return Objects.equals(name, quiz.name) &&
                Objects.equals(collQuestions, quiz.collQuestions);
    }

    @Override
    public int hashCode() {
        return Objects.hash(name, collQuestions);
    }

    @Override
    public String toString() {
        return name;
    }

    @Override
    public int compareTo(Quiz o) {
        return this.name.compareTo(o.getName());
    }
}