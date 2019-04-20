package com.thurau.quizduell.pkgFirebase;

import android.os.AsyncTask;
import android.support.annotation.NonNull;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.ListenerRegistration;
import com.thurau.quizduell.pkgData.GivenAnswer;

public class FirestoreWriteGivenAnswer extends AsyncTask<GivenAnswer, Void, Void> implements OnSuccessListener<DocumentReference>, OnFailureListener, OnCompleteListener<DocumentReference> {
    private FirebaseFirestore db;
    private int cnt = 0;
    private GivenAnswerListener listener;
    private ListenerRegistration registration;

    public FirestoreWriteGivenAnswer(GivenAnswerListener listener) {
        this.listener = listener;
        initFirebase();
    }

    private void initFirebase() {
        db = FirebaseFirestore.getInstance();
    }

    @Override
    protected Void doInBackground(GivenAnswer... params) {
        db.collection("givenanswers").add(params[0]).addOnSuccessListener(this).addOnFailureListener(this).addOnCompleteListener(this);
        return null;
    }

    @Override
    public void onComplete(@NonNull Task<DocumentReference> task) {
    }

    @Override
    public void onSuccess(DocumentReference queryDocumentSnapshots) {
        listener.alert("Successfully stored given answer");
    }

    @Override
    public void onFailure(@NonNull Exception e) {
        listener.alert("Failed to write given answer");
    }
}