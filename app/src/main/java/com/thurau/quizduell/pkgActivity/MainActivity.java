package com.thurau.quizduell.pkgActivity;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.FirebaseApp;
import com.thurau.quizduell.R;
import com.thurau.quizduell.pkgData.Quiz;
import com.thurau.quizduell.pkgFirebase.FirebaseActionListener;
import com.thurau.quizduell.pkgFirebase.FirestoreReadQuiz;

import java.util.ArrayList;

public class MainActivity extends AppCompatActivity implements View.OnClickListener, FirebaseActionListener {
    private TextView txtWelcome;
    private Spinner spQuiz;
    private Button btnStartQuiz;
    private ArrayAdapter<Quiz> adapterQuiz;
    private TextView txtMessage;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        try {
            FirebaseApp.initializeApp(this);
            getAllViews();
            initOtherThings();
            fillSpinnerQuiz();
            registerEventHandlers();
        } catch (Exception e) {
            Toast.makeText(this, "error..." + e.getMessage(), Toast.LENGTH_LONG).show();
        }
    }

    private void initOtherThings() {
        adapterQuiz = new ArrayAdapter<>(this, android.R.layout.simple_list_item_1);
        spQuiz.setAdapter(adapterQuiz);
    }

    private void fillSpinnerQuiz() {
        adapterQuiz.clear();
        txtMessage.setText("Loading quizzes from firestore...");
        FirestoreReadQuiz fs_read = new FirestoreReadQuiz(this);
        fs_read.execute();
    }

    private void registerEventHandlers() {
        btnStartQuiz.setOnClickListener(this);
    }

    private void getAllViews() {
        txtWelcome = this.findViewById(R.id.txtWelcome);
        spQuiz = this.findViewById(R.id.spQuiz);
        btnStartQuiz = this.findViewById(R.id.btnStartQuiz);
        txtMessage = this.findViewById(R.id.txtMessages);
    }

    @Override
    public void onClick(View v) {
        Intent intent = new Intent(this, WaitingActivityStart.class);
        intent.putExtra("quiz", (Quiz)spQuiz.getSelectedItem());
        startActivity(intent);
        this.finish();
    }

    @Override
    public void onReadCompleted(ArrayList<Quiz> message) {
        adapterQuiz.addAll(message);
        txtMessage.setText("Quizzes loaded successfully!");
    }

    @Override
    public void alertListener(String message) {
        txtMessage.setText(message);
    }
}