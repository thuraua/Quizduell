package com.thurau.quizduell.pkgFirebase;

public class FirestoreQuiz {
    private String info;

    public FirestoreQuiz(String info) {
        this.info = info;
    }

    public FirestoreQuiz() {
        this("nix");
    }

    public String getInfo() {
        return this.info;
    }

    public void setInfo(String info) {
        this.info = info;
    }

    @Override
    public String toString() {
        return "FirestoreQuiz{" +
                "info='" + info + '\'' +
                '}';
    }
}
