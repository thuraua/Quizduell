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
import com.google.firebase.firestore.ListenerRegistration;
import com.google.firebase.firestore.QuerySnapshot;
import com.google.gson.Gson;
import com.thurau.quizduell.pkgData.Quiz;

import java.util.ArrayList;
import java.util.HashMap;

import javax.annotation.Nullable;

public class IdManager extends AsyncTask<Void, Void, Void> implements OnCompleteListener<QuerySnapshot>, OnSuccessListener<QuerySnapshot>, OnFailureListener, EventListener<QuerySnapshot> {
    private Quiz quiz;
    private FirebaseFirestore db;
    private IdManagerListener listener;
    private int myId, enemyId;
    private ListenerRegistration registration;
    private String enemyInternalId;
    private String myInternalId;

    public IdManager(IdManagerListener listener, Quiz quiz) {
        this.listener = listener;
        this.quiz = quiz;
        initFirebase();
    }

    private void initFirebase() {
        db = FirebaseFirestore.getInstance();
    }

    @Override
    protected Void doInBackground(Void... params) {
        try {
            db.collection("ids").get().addOnSuccessListener(this).addOnFailureListener(this);
        } catch (Exception ex) {
            listener.err("fail" + ex.getMessage());
        }
        return null;
    }

    @Override
    public void onComplete(@NonNull Task<QuerySnapshot> task) {
        ArrayList<Integer> tmp = new ArrayList<>();
        ArrayList<Quiz> quizzes = new ArrayList<>();
        for (DocumentSnapshot doc : task.getResult().getDocuments()) {
            try {
                int id = Integer.valueOf(String.valueOf(doc.get("id")));
                Quiz c = new Gson().fromJson(String.valueOf(doc.get("quiz")), Quiz.class);
                if (id == myId) myInternalId = doc.getId();
                else if (enemyInternalId == null) enemyInternalId = doc.getId();
                tmp.add(id);
                quizzes.add(c);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        if (tmp.size() > 1) {       //Gegner ist schon da, wir müssen das Quiz nehmen das er gewählt hat
            enemyId = myId == tmp.get(0) ? tmp.get(1) : tmp.get(0);
            quiz = myId == tmp.get(0) ? quizzes.get(1) : quizzes.get(0);
            listener.assignEnemyId(enemyId, quiz);
            setIdsReadyToFalse();
        } else {      //wir warten auf einen Gegner
            registration = db.collection("ids").whereEqualTo("ready", true).addSnapshotListener(this);
        }
    }

    @Override
    public void onSuccess(QuerySnapshot queryDocumentSnapshots) {
        ArrayList<Integer> tmp = new ArrayList<>();
        for (DocumentSnapshot doc : queryDocumentSnapshots.getDocuments()) {
            try {
                int id = Integer.valueOf(String.valueOf(doc.get("id")));
                tmp.add(id);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        myId = tmp.size() + 1;
        HashMap<String, Object> map = new HashMap<String, Object>();
        map.put("id", myId);
        map.put("ready", true);
        map.put("quiz", new Gson().toJson(quiz));
        map.put("duration", 0);
        db.collection("ids").add(map);
        listener.assignCurrentPlayerId(myId);
        db.collection("ids").whereEqualTo("ready", true).get().addOnCompleteListener(this);
    }

    @Override
    public void onFailure(@NonNull Exception e) {
        listener.err("Failed to read ids");
    }

    @Override
    public void onEvent(@Nullable QuerySnapshot queryDocumentSnapshots, @Nullable FirebaseFirestoreException e) {
        if (e != null) {
            System.out.println("******onChangeEvent()...Listen failed. " + e.getMessage());
            return;
        }
        for (DocumentChange dc : queryDocumentSnapshots.getDocumentChanges()) {
            switch (dc.getType()) {
                case ADDED:     //someone else has signed up and is now my enemy
                    if (Integer.valueOf(String.valueOf(dc.getDocument().get("id"))) != myId) {
                        enemyId = Integer.valueOf(String.valueOf(dc.getDocument().get("id")));
                        enemyInternalId = dc.getDocument().getId();
                        registration.remove();
                        listener.assignEnemyId(enemyId, quiz);
                        setIdsReadyToFalse();
                    }
                    break;
                //other cases not relevant yet...
            }
        }
    }

    private void setIdsReadyToFalse() {
        db.collection("ids").document(myInternalId).update("ready", false);
        db.collection("ids").document(enemyInternalId).update("ready", false);
    }
}