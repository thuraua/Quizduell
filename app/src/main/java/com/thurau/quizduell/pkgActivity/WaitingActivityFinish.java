package com.thurau.quizduell.pkgActivity;

import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.thurau.quizduell.R;
import com.thurau.quizduell.pkgData.Quiz;
import com.thurau.quizduell.pkgFirebase.EnemyListener;
import com.thurau.quizduell.pkgFirebase.FirestoreEnemyProgress;
import com.thurau.quizduell.pkgFirebase.FirestoreWriteTime;

public class WaitingActivityFinish extends AppCompatActivity implements EnemyListener {

    private TextView txtMessage;
    private ProgressBar progress;
    private Quiz quiz;
    private int myId;
    private int enemyId;
    private FirestoreEnemyProgress fs_progress;
    private long myDuration;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_waiting_finish);
        this.setTitle("Tell other player 'hurry up!'");
        quiz = (Quiz) getIntent().getExtras().getSerializable("quiz");
        myId = getIntent().getExtras().getInt("myId");
        enemyId = getIntent().getExtras().getInt("enemyId");
        myDuration = getIntent().getExtras().getLong("duration");
        getAllViews();
        txtMessage.setText("Enemy still answering ...");
        fs_progress = new FirestoreEnemyProgress(this, enemyId);
        fs_progress.execute();
        try {
            new FirestoreWriteTime().execute(enemyId, myId, Math.toIntExact(myDuration));
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void getAllViews() {
        txtMessage = this.findViewById(R.id.txtMessage);
        progress = this.findViewById(R.id.progressBar4);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            progress.setMin(0);
        }
        progress.setMax(quiz.getCollQuestions().size());
    }

    @Override //EnemyListener
    public void refreshEnemyProgress(int value) {
        progress.setProgress(value);
        if (value == quiz.getCollQuestions().size()) openFinalActivity();
    }

    @Override //EnemyListener
    public void alertListener(String message) {
        txtMessage.setText(message);
        progress.setProgress(progress.getProgress() + 1);
        if (progress.getProgress() == quiz.getCollQuestions().size()) openFinalActivity();
    }

    private void openFinalActivity() {
        Intent intent = new Intent(this, ResultActivity.class);
        intent.putExtra("quiz", quiz);
        intent.putExtra("myId", myId);
        intent.putExtra("enemyId", enemyId);
        intent.putExtra("myDuration", myDuration);
        startActivity(intent);
        this.finish();
    }
}
