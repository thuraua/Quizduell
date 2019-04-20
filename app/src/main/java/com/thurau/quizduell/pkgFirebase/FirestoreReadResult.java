package com.thurau.quizduell.pkgFirebase;

import android.os.AsyncTask;
import android.support.annotation.NonNull;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QuerySnapshot;

public class FirestoreReadResult extends AsyncTask<Void, Void, Void> implements OnCompleteListener<QuerySnapshot>, OnSuccessListener<QuerySnapshot>, OnFailureListener {
    private FirebaseFirestore db;
    private ReadResultListener listener;
    private int myId, enemyId;
    private GameResult gameResult = new GameResult();

    public FirestoreReadResult(ReadResultListener listener, int myId, int enemyId) {
        this.listener = listener;
        this.myId = myId;
        this.enemyId = enemyId;
        initFirebase();
    }

    private void initFirebase() {
        db = FirebaseFirestore.getInstance();
    }

    @Override
    protected Void doInBackground(Void... params) {
        try {
            Thread.sleep(1000);
            db.collection("givenanswers").whereEqualTo("playerId", myId).get().addOnSuccessListener(this).addOnFailureListener(this);
            db.collection("givenanswers").whereEqualTo("playerId", enemyId).get().addOnSuccessListener(this).addOnFailureListener(this);
            db.collection("ids").whereEqualTo("id", enemyId).get().addOnCompleteListener(this).addOnFailureListener(this);
        } catch (Exception ex) {
            listener.onError("fail" + ex.getMessage());
        }
        return null;
    }

    @Override
    public void onComplete(@NonNull Task<QuerySnapshot> task) {
        int enemyDuration = 0;
        for (DocumentSnapshot doc : task.getResult().getDocuments())
            enemyDuration = Integer.valueOf(String.valueOf(doc.get("duration")));
        if (enemyDuration == 0) {
            db.collection("ids").whereEqualTo("id", enemyId).get().addOnCompleteListener(this).addOnFailureListener(this);
            return;
        }
        gameResult.setEnemyDuration(enemyDuration);
        if (gameResult.getEnemyCntRightAnswers() != -1 && gameResult.getMyCntRightAnswers() != -1)
            listener.onResultReadCompleted(gameResult);
    }

    @Override   //executed 2 times
    public void onSuccess(QuerySnapshot queryDocumentSnapshots) {
        int id = 0, cnt = 0;
        for (DocumentSnapshot doc : queryDocumentSnapshots.getDocuments()) {
            id = Integer.valueOf(String.valueOf(doc.get("playerId")));
            if (doc.getBoolean("right") == true)
                cnt++;
        }
        if (id == myId) gameResult.setMyCntRightAnswers(cnt);
        if (id == enemyId) gameResult.setEnemyCntRightAnswers(cnt);
        if (gameResult.getEnemyDuration() != -1 && gameResult.getEnemyCntRightAnswers() != -1 && gameResult.getMyCntRightAnswers() != -1)
            listener.onResultReadCompleted(gameResult);
    }

    @Override
    public void onFailure(@NonNull Exception e) {
        listener.onError("Failed to read game results: " + e.getMessage());
    }
}