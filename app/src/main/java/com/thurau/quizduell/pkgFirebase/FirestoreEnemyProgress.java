package com.thurau.quizduell.pkgFirebase;

import android.os.AsyncTask;
import android.support.annotation.NonNull;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.DocumentChange;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;
import com.google.firebase.firestore.QuerySnapshot;
import com.thurau.quizduell.pkgData.GivenAnswer;

import java.util.ArrayList;

import javax.annotation.Nullable;

public class FirestoreEnemyProgress extends AsyncTask<Void, Void, Void> implements OnCompleteListener<QuerySnapshot>, OnSuccessListener<QuerySnapshot>, OnFailureListener, /*EventListener<DocumentSnapshot>,*/ EventListener<QuerySnapshot> {
    private FirebaseFirestore db;
    private EnemyListener listener;
    private int enemyId;

    public FirestoreEnemyProgress(EnemyListener listener, int enemyId) {
        this.listener = listener;
        this.enemyId = enemyId;
        initFirebase();
    }

    private void initFirebase() {
        db = FirebaseFirestore.getInstance();
    }

    @Override
    protected Void doInBackground(Void... params) {
        try {
            db.collection("givenanswers").whereEqualTo("playerId", enemyId).addSnapshotListener(this);
            db.collection("givenanswers").whereEqualTo("playerId", enemyId).get().addOnSuccessListener(this).addOnFailureListener(this).addOnCompleteListener(this);
        } catch (Exception ex) {
            listener.alertListener("fail" + ex.getMessage());
        }
        return null;
    }

    @Override
    public void onComplete(@NonNull Task<QuerySnapshot> task) {
    }

    @Override
    public void onSuccess(QuerySnapshot queryDocumentSnapshots) {
        ArrayList<GivenAnswer> tmp = new ArrayList<>();
        for (DocumentSnapshot doc : queryDocumentSnapshots.getDocuments()) {
            try {
                int playerId = Integer.valueOf(doc.getData().get("playerId").toString());
                boolean right = Boolean.valueOf(doc.getData().get("right").toString());
                tmp.add(new GivenAnswer(playerId, right));
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        listener.refreshEnemyProgress(tmp.size());
    }

    @Override
    public void onFailure(@NonNull Exception e) {
        listener.alertListener("Failed to read enemy progress");
    }

    @Override
    public void onEvent(@Nullable QuerySnapshot snapshots, @Nullable FirebaseFirestoreException e) {
        if (e != null) {
            System.out.println("******onChangeEvent()...Listen failed. " + e.getMessage());
            return;
        }

        for (DocumentChange dc : snapshots.getDocumentChanges()) {
            switch (dc.getType()) {
                case ADDED:
                    listener.alertListener("Info: doc added in background: " + dc.getDocument().getData());
                    break;
                //... other cases not relevant yet
            }
        }
    }
}