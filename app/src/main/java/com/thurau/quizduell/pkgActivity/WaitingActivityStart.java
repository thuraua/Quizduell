package com.thurau.quizduell.pkgActivity;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.thurau.quizduell.R;
import com.thurau.quizduell.pkgData.Quiz;
import com.thurau.quizduell.pkgFirebase.IdManager;
import com.thurau.quizduell.pkgFirebase.IdManagerListener;

public class WaitingActivityStart extends AppCompatActivity implements IdManagerListener {

    private TextView txtMessage;
    private ProgressBar progress;
    private Quiz quiz;
    private int myId;
    private int enemyId;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_waiting_start);
        quiz = (Quiz) getIntent().getExtras().getSerializable("quiz");
        new IdManager(this, quiz).execute();
        getAllViews();
        this.setTitle("Please wait");
    }

    private void getAllViews() {
        txtMessage = this.findViewById(R.id.txtMessage);
        progress = this.findViewById(R.id.progressBar2);
    }

    @Override
    public void assignCurrentPlayerId(int id) {
        myId = id;
        txtMessage.setText("Signed up successfully with temp id " + myId + ".\nWaiting for enemy...");
    }

    @Override
    public void assignEnemyId(int id, Quiz q) {
        enemyId = id;
        Intent intent = new Intent(this, QuestionActivity.class);
        intent.putExtra("quiz", q);
        intent.putExtra("myId", myId);
        intent.putExtra("enemyId", enemyId);
        startActivity(intent);
        this.finish();
    }

    @Override
    public void err(String message) {
        txtMessage.setText("Error: " + message);
    }
}
