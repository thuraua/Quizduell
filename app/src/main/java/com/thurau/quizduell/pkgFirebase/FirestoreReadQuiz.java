package com.thurau.quizduell.pkgFirebase;

import android.os.AsyncTask;
import android.support.annotation.NonNull;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.ListenerRegistration;
import com.google.firebase.firestore.QuerySnapshot;
import com.google.gson.Gson;
import com.thurau.quizduell.pkgData.Quiz;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.Comparator;

public class FirestoreReadQuiz extends AsyncTask<String, Void, Void> implements OnSuccessListener<QuerySnapshot>, OnFailureListener {
    private FirebaseFirestore db;
    private ArrayList<Quiz> quizzes = new ArrayList<>();
    private FirebaseActionListener listener;
    private ListenerRegistration registration;

    public FirestoreReadQuiz(FirebaseActionListener listener) {
        this.listener = listener;
        initFirebase();
    }

    private void initFirebase() {
        db = FirebaseFirestore.getInstance();
    }

    @Override
    protected Void doInBackground(String... params) {
        try {
            quizzes.clear();
            db.collection("quizzes").get().addOnSuccessListener(this).addOnFailureListener(this);
        } catch (Exception ex) {
            listener.alertListener("fail" + ex.getMessage());
        }
        return null;
    }

    @Override
    public void onSuccess(QuerySnapshot queryDocumentSnapshots) {
        ArrayList<Quiz> tmp = new ArrayList<>();
        Gson gson = new Gson();
        for (DocumentSnapshot doc : queryDocumentSnapshots.getDocuments()) {
            try {
                FirestoreQuiz quizfire = doc.toObject(FirestoreQuiz.class);
                Quiz c = gson.fromJson(quizfire.getInfo(), Quiz.class);
                tmp.add(c);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        Collections.sort(tmp);
        listener.onReadCompleted(tmp);
    }

    @Override
    public void onFailure(@NonNull Exception e) {
        listener.alertListener("Failed to Read Cars");
    }
}