package com.aknazarov.math;

import android.content.SharedPreferences;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import static java.lang.Math.max;

public class MainActivity extends AppCompatActivity {


    SharedPreferences sharedPreferences;
    final String POINTS = "POINTS";
    final String STREAK = "STREAK";
    final int START_POINTS = 30; // if set it under ADD_SUB_POINT add tasks will be just a number
    int points;
    int streak;
    Task task;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        load();

        final TextView taskView = findViewById(R.id.task);
        final TextView answerView = findViewById(R.id.answer);
        final TextView levelView = findViewById(R.id.level_view);
        final TextView streakView = findViewById(R.id.streak_view);
        final Button submit = findViewById(R.id.submit);

        submit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (answerView.getText().toString().length() == 0){
                    answerView.setHint("Type something!");
                    return;
                }
                try {
                    if (Integer.parseInt(answerView.getText().toString()) == task.getAnswer()) {
                        answerView.setHint("Right");
                        points += streak++;
                        answerView.setText("");
                        nextTask();
                        updateView(taskView, levelView, streakView);
                    } else {
                        if (streak != 1) {
                            points -= 5;
                            points = max(points, START_POINTS);
                        }
                        streak = 1;
                        updateView(taskView, levelView, streakView);
                        answerView.setHint("Mistake");
                    }
                }
                catch (NumberFormatException e){
                    answerView.setText("");
                    answerView.setHint("only numbers!");
                }
            }
        });

        task = new Task(points);
        nextTask();
        updateView(taskView, levelView, streakView);
    }



    void save() {
        sharedPreferences = getPreferences(MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putInt(POINTS, points);
        editor.putInt(STREAK, streak);
        editor.apply();
    }

    void load() {
        sharedPreferences = getPreferences(MODE_PRIVATE);
        points = sharedPreferences.getInt(POINTS, START_POINTS);
        streak = sharedPreferences.getInt(STREAK, 1);
    }

    private void nextTask(){
        save();
        task.setPoint(points);
        task.genTask();
    }

    private void updateView(TextView textView, TextView levelView, TextView streakView){
        levelView.setText(points + "");
        streakView.setText("+" + streak);
        textView.setText(task.getTask());
    }
}
