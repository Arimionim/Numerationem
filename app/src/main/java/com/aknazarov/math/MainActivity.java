package com.aknazarov.math;

import android.content.SharedPreferences;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import static com.aknazarov.math.Task.ADD_SUB_POINT;
import static java.lang.Math.max;

public class MainActivity extends AppCompatActivity {


    public static final int LOOSE_STREAK_MULTIPLIER = 3;
    SharedPreferences sharedPreferences;
    final String POINTS = "POINTS";
    final String STREAK = "STREAK";
    final String LOOSE_STREAK = "LOOSE_STREAK";
    final int START_POINTS = ADD_SUB_POINT; // if set it under ADD_SUB_POINT add task will be just a number
    int points;
    int streak;
    int looseStreak;
    private static Task task;

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
                    answerView.setHint(R.string.empty_input_hint);
                    return;
                }
                try {
                    if (Integer.parseInt(answerView.getText().toString()) == task.getAnswer()) {
                        answerView.setHint(R.string.success_hint);
                        points += streak++;
                        looseStreak = 0;
                        answerView.setBackgroundResource(R.color.goodAnswerColor);
                        answerView.setText("");
                        nextTask();
                        updateView(taskView, levelView, streakView);
                    } else {
                        points = max(points - LOOSE_STREAK_MULTIPLIER * ++looseStreak, START_POINTS);
                        streak = 1;
                        updateView(taskView, levelView, streakView);
                        answerView.setBackgroundResource(R.color.badAnswerColor);
                        answerView.setHint(R.string.mistake_hint);
                    }
                    save();
                }
                catch (NumberFormatException e){
                    answerView.setText("");
                    answerView.setHint("Only numbers!");
                }
            }
        });

        final TextView skip = findViewById(R.id.skip_button);
        skip.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                nextTask();
                answerView.setBackgroundResource(R.color.goodAnswerColor);
                answerView.setText("");
                answerView.setHint(R.string.default_hint);
                updateView(taskView, levelView, streakView);
            }
        });

        if (task == null) {
            task = new Task(points);
            nextTask();
        }
        updateView(taskView, levelView, streakView);
    }



    void save() {
        sharedPreferences = getPreferences(MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putInt(POINTS, points);
        editor.putInt(STREAK, streak);
        editor.putInt(LOOSE_STREAK, looseStreak);
        editor.apply();
    }

    void load() {
        sharedPreferences = getPreferences(MODE_PRIVATE);
        points = sharedPreferences.getInt(POINTS, START_POINTS);
        streak = sharedPreferences.getInt(STREAK, 1);
        looseStreak = sharedPreferences.getInt(LOOSE_STREAK, 0);
    }

    private void nextTask(){
        task.setPoint(points);
        do {
            task.genTask();
        } while (task.getAnswer() >= 1e9);
    }

    private void updateView(TextView textView, TextView levelView, TextView streakView){
        levelView.setText(points + "");
        streakView.setText("+" + streak);
        textView.setText(task.getTask());
    }
}
