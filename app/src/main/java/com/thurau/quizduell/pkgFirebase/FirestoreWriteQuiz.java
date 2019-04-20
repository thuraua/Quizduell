package com.thurau.quizduell.pkgFirebase;

import android.os.AsyncTask;
import android.support.annotation.NonNull;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.gson.Gson;
import com.thurau.quizduell.pkgData.Quiz;

import java.util.ArrayList;
import java.util.List;

@SuppressWarnings("unused")
public class FirestoreWriteQuiz extends AsyncTask<List<Quiz>, Void, String>
        implements OnSuccessListener<DocumentReference>, OnFailureListener, OnCompleteListener<DocumentReference> {
    private FirebaseFirestore db;
    private int cnt = 0, cntMax = 0;
    private ArrayList<Quiz> collQuizs = new ArrayList<>();
    private FirebaseActionListener listener;

    public FirestoreWriteQuiz(FirebaseActionListener listener) throws Exception {
        this.listener = listener;
        initFirebase();
    }

    private void initFirebase() throws Exception {
        db = FirebaseFirestore.getInstance();
    }

    @Override
    protected String doInBackground(List<Quiz>... paraQuizs) {
        cntMax = paraQuizs[0].size();
        FirestoreQuiz quizfire;
        Gson gson = new Gson();
        try {
            for (Quiz Quiz : paraQuizs[0]) {
                quizfire = new FirestoreQuiz(gson.toJson(Quiz));
                db.collection("quizzes")
                        .add(quizfire)
                        .addOnSuccessListener(this)
                        .addOnFailureListener(this)
                        .addOnCompleteListener(this);
            }
        } catch (Exception e) {
            System.out.println("**********" + e.getMessage());
            listener.alertListener(e.getMessage());
            e.printStackTrace();
        }
        return "save completed";
    }

    @Override
    protected void onPostExecute(String message) {
        System.out.println("********onPost() write: " + message); //too early
    }

    @Override
    public void onSuccess(DocumentReference documentReference) {
        System.out.println("********** on Succ");
        cnt++;
        listener.alertListener("saved doc " + cnt + "/" + cntMax + " : " + documentReference.getId());
    }

    @Override
    public void onFailure(@NonNull Exception e) {
        System.out.println("********** NOT Succ");
        listener.alertListener("write with problem: " + e.getMessage());
    }

    @Override
    public void onComplete(@NonNull Task<DocumentReference> task) {
        System.out.println("********** onComplete()");
    }
}

