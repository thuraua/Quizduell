package com.thurau.quizduell.pkgActivity;

import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.thurau.quizduell.R;
import com.thurau.quizduell.pkgData.GivenAnswer;
import com.thurau.quizduell.pkgData.Quiz;
import com.thurau.quizduell.pkgFirebase.EnemyListener;
import com.thurau.quizduell.pkgFirebase.FirestoreEnemyProgress;
import com.thurau.quizduell.pkgFirebase.FirestoreWriteGivenAnswer;
import com.thurau.quizduell.pkgFirebase.GivenAnswerListener;

import java.util.Date;

public class QuestionActivity extends AppCompatActivity implements View.OnClickListener, EnemyListener, GivenAnswerListener {

    private TextView txtQuestionText;
    private Button buttonAnswer0;
    private Button buttonAnswer1;
    private Button buttonAnswer2;
    private Button buttonAnswer3;
    private Quiz quiz;
    private int idx = 0;
    private ProgressBar enemyProgress;
    private FirestoreEnemyProgress fs_progress;
    private int myId;
    private int enemyId;
    private long startTime;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_question);
        startTime = new Date().getTime();
        quiz = (Quiz) getIntent().getExtras().getSerializable("quiz");
        myId = getIntent().getExtras().getInt("myId");
        enemyId = getIntent().getExtras().getInt("enemyId");
        getAllViews();
        initGUIThings();
        fs_progress = new FirestoreEnemyProgress(this, enemyId);
        fs_progress.execute();
        registerEventHandlers();
    }

    private void getAllViews() {
        txtQuestionText = this.findViewById(R.id.txtQuestionText);
        buttonAnswer0 = this.findViewById(R.id.button0);
        buttonAnswer1 = this.findViewById(R.id.button1);
        buttonAnswer2 = this.findViewById(R.id.button2);
        buttonAnswer3 = this.findViewById(R.id.button3);
        enemyProgress = this.findViewById(R.id.progressBar);
    }

    private void initGUIThings() {
        this.setTitle("Question " + (idx + 1) + " of " + quiz.getCollQuestions().size());
        txtQuestionText.setText(quiz.getCollQuestions().get(idx).getText());
        buttonAnswer0.setText(quiz.getCollQuestions().get(idx).getPossibleAnswers().get(0));
        buttonAnswer1.setText(quiz.getCollQuestions().get(idx).getPossibleAnswers().get(1));
        buttonAnswer2.setText(quiz.getCollQuestions().get(idx).getPossibleAnswers().get(2));
        buttonAnswer3.setText(quiz.getCollQuestions().get(idx).getPossibleAnswers().get(3));
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            enemyProgress.setMin(0);
        }
        enemyProgress.setMax(quiz.getCollQuestions().size());
    }

    private void registerEventHandlers() {
        buttonAnswer0.setOnClickListener(this);
        buttonAnswer1.setOnClickListener(this);
        buttonAnswer2.setOnClickListener(this);
        buttonAnswer3.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        int givenAnswerIdx = 3;
        if (v.getId() == R.id.button0) givenAnswerIdx = 0;
        else if (v.getId() == R.id.button1) givenAnswerIdx = 1;
        else if (v.getId() == R.id.button2) givenAnswerIdx = 2;
        FirestoreWriteGivenAnswer fs_write = new FirestoreWriteGivenAnswer(this);
        fs_write.execute(new GivenAnswer(myId, quiz.getCollQuestions().get(idx).getRightAnwersIdx() == givenAnswerIdx));
        //next question will be displayed as soon as given answer is stored successfully
    }

    @Override //EnemyListener
    public void refreshEnemyProgress(int value) {
        enemyProgress.setProgress(value);
    }

    @Override //EnemyListener
    public void alertListener(String message) {
        if (message.startsWith("Info"))
            enemyProgress.setProgress(enemyProgress.getProgress() + 1);
    }

    @Override //GivenAnswerListener
    public void alert(String message) {
        if (message.startsWith("Successfully")) {
            idx++;
            if (idx >= quiz.getCollQuestions().size()) {    //no question left
                //quiz finished, open waiting intent
                Intent intent = new Intent(this, WaitingActivityFinish.class);
                intent.putExtra("quiz", quiz);
                intent.putExtra("myId", myId);
                intent.putExtra("enemyId", enemyId);
                long runningTime = new Date().getTime() - startTime;
                intent.putExtra("duration", runningTime);
                startActivity(intent);
                this.finish();
            } else {        //next question
                initGUIThings();
            }
        }
    }
}