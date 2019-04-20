package com.thurau.quizduell.pkgFirebase;

import android.os.AsyncTask;
import android.support.annotation.NonNull;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QuerySnapshot;

public class FirestoreWriteTime extends AsyncTask<Integer, Void, Void>
        implements OnFailureListener, OnSuccessListener<QuerySnapshot> {
    private FirebaseFirestore db;
    private int myDuration;
    private int enemyDuration;

    public FirestoreWriteTime() throws Exception {
        initFirebase();
    }

    private void initFirebase() throws Exception {
        db = FirebaseFirestore.getInstance();
    }

    @Override
    protected Void doInBackground(Integer... params) {
        int enemyId = params[0];
        int myId = params[1];
        myDuration = params[2];
        try {
            db.collection("ids")
                    .whereEqualTo("id", myId).get()
                    .addOnSuccessListener(this)
                    .addOnFailureListener(this);
        } catch (Exception e) {
            System.out.println("**********" + e.getMessage());
            e.printStackTrace();
        }
        return null;
    }

    @Override
    public void onFailure(@NonNull Exception e) {
        System.out.println("********** NOT Succ");
    }

    @Override
    public void onSuccess(QuerySnapshot queryDocumentSnapshots) {
        db.collection("ids").document(queryDocumentSnapshots.getDocuments().get(0).getId()).update("duration", myDuration);
    }
}

