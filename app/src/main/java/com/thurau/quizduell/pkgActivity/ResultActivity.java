package com.thurau.quizduell.pkgActivity;

import android.content.Intent;
import android.graphics.Color;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.github.mikephil.charting.charts.BarChart;
import com.github.mikephil.charting.charts.HorizontalBarChart;
import com.github.mikephil.charting.data.BarData;
import com.github.mikephil.charting.data.BarDataSet;
import com.github.mikephil.charting.data.BarEntry;
import com.thurau.quizduell.R;
import com.thurau.quizduell.pkgData.Quiz;
import com.thurau.quizduell.pkgFirebase.FirestoreReadResult;
import com.thurau.quizduell.pkgFirebase.GameResult;
import com.thurau.quizduell.pkgFirebase.ReadResultListener;

import java.util.ArrayList;
import java.util.List;

public class ResultActivity extends AppCompatActivity implements View.OnClickListener, ReadResultListener {

    private TextView txtResult;
    private BarChart rightAnswersChart;
    private HorizontalBarChart timeChart;
    private Button btnNewGame;
    private Button btnQuit;
    private int myId;
    private int enemyId;
    private long myDuration;
    private GameResult gameResult;
    private Quiz quiz;
    private TextView txtMessage;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_result);
        this.setTitle("Result");
        quiz = (Quiz) getIntent().getExtras().getSerializable("quiz");
        myId = getIntent().getExtras().getInt("myId");
        enemyId = getIntent().getExtras().getInt("enemyId");
        myDuration = getIntent().getExtras().getLong("myDuration");
        getAllViews();
        registerEventHandlers();
        new FirestoreReadResult(this, myId, enemyId).execute();
    }

    private void registerEventHandlers() {
        btnNewGame.setOnClickListener(this);
        btnQuit.setOnClickListener(this);
    }

    private void getAllViews() {
        txtResult = findViewById(R.id.txtResult);
        txtMessage = findViewById(R.id.txtMessage);
        rightAnswersChart = findViewById(R.id.chart);
        timeChart = findViewById(R.id.chart2);
        btnNewGame = findViewById(R.id.btnNewGame);
        btnQuit = findViewById(R.id.btnQuit);
    }

    private void initRightAnswersChart() {
        BarData barData = new BarData();

        List<BarEntry> entries1 = new ArrayList<>();
        entries1.add(new BarEntry(0, gameResult.getMyCntRightAnswers()));
        BarDataSet dataSet1 = new BarDataSet(entries1, "You");
        dataSet1.setColor(Color.parseColor("green"));
        dataSet1.setValueTextColor(Color.parseColor("black"));
        barData.addDataSet(dataSet1);

        List<BarEntry> entries2 = new ArrayList<>();
        entries2.add(new BarEntry(1, gameResult.getEnemyCntRightAnswers()));
        BarDataSet dataSet2 = new BarDataSet(entries2, "Enemy");
        dataSet2.setColor(Color.parseColor("red"));
        dataSet2.setValueTextColor(Color.parseColor("black"));
        barData.addDataSet(dataSet2);

        rightAnswersChart.setData(barData);
        rightAnswersChart.getAxisRight().setAxisMinimum(0);
        rightAnswersChart.getAxisRight().setAxisMaximum(quiz.getCollQuestions().size());
        rightAnswersChart.getAxisLeft().setAxisMinimum(0);
        rightAnswersChart.getAxisLeft().setAxisMaximum(quiz.getCollQuestions().size());
        rightAnswersChart.setDrawGridBackground(false);
        rightAnswersChart.getXAxis().setEnabled(false);
        rightAnswersChart.getDescription().setEnabled(false);
        rightAnswersChart.invalidate(); // refresh
    }

    private void initTimeChart() {
        BarData barData = new BarData();

        List<BarEntry> entries1 = new ArrayList<>();
        entries1.add(new BarEntry(0, myDuration));
        BarDataSet dataSet1 = new BarDataSet(entries1, "You");
        dataSet1.setColor(Color.parseColor("green"));
        dataSet1.setValueTextColor(Color.parseColor("black"));
        barData.addDataSet(dataSet1);

        List<BarEntry> entries2 = new ArrayList<>();
        entries2.add(new BarEntry(1, gameResult.getEnemyDuration()));
        BarDataSet dataSet2 = new BarDataSet(entries2, "Enemy"); // add entries to dataset
        dataSet2.setColor(Color.parseColor("red"));
        dataSet2.setValueTextColor(Color.parseColor("black")); // styling, ...
        barData.addDataSet(dataSet2);

        timeChart.setData(barData);
        timeChart.getXAxis().setEnabled(false);
        timeChart.getAxisRight().setAxisMinimum(0);
        timeChart.getAxisRight().setAxisMaximum((float) (myDuration > gameResult.getEnemyDuration() ? myDuration * 1.2 : gameResult.getEnemyDuration() * 1.2));
        timeChart.getAxisLeft().setAxisMinimum(0);
        timeChart.getAxisLeft().setAxisMaximum((float) (myDuration > gameResult.getEnemyDuration() ? myDuration * 1.2 : gameResult.getEnemyDuration() * 1.2));
        timeChart.getAxisLeft().setDrawAxisLine(false);
        timeChart.getAxisLeft().setDrawLabels(false);
        timeChart.setDrawGridBackground(false);
        timeChart.getDescription().setEnabled(false);
        timeChart.invalidate(); // refresh
    }

    @Override
    public void onClick(View v) {
        if (v.getId() == R.id.btnNewGame) {
            Intent intent = new Intent(this, MainActivity.class);
            startActivity(intent);
        }
        this.finish();
    }

    @Override
    public void onResultReadCompleted(GameResult result) {
        this.gameResult = result;
        boolean win;
        if (gameResult.getMyCntRightAnswers() > gameResult.getEnemyCntRightAnswers())
            win = true;
        else if (gameResult.getMyCntRightAnswers() < gameResult.getEnemyCntRightAnswers())
            win = false;
        else win = myDuration < gameResult.getEnemyDuration();
        if (win) txtResult.setText("You won!");
        else {
            txtResult.setText("You lost");
            ((ImageView) findViewById(R.id.imageView1)).setImageResource(R.mipmap.lose);
            ((ImageView) findViewById(R.id.imageView2)).setImageResource(R.mipmap.lose);
        }
        initRightAnswersChart();
        initTimeChart();
    }

    @Override
    public void onError(String message) {
        txtMessage.setText(message);
    }
}
