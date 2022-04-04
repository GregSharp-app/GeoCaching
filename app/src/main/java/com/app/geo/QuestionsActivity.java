package com.app.geo;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class QuestionsActivity extends AppCompatActivity {

    // first cache Questions
    private List<Question> questionsFirstCache = new ArrayList<Question>() {{
        add(new Question("Cache 1 Question 1 ?", "Answer 1", false));
        add(new Question("Cache 1 Question 2 ?", "Answer 2", true));
        add(new Question("Cache 1 Question 3 ?", "Answer 3", true));
    }};

    // second cache Questions
    private List<Question> questionsSecondCache = new ArrayList<Question>() {{
        add(new Question("Cache 2 Question 1 ?", "Answer 1", false));
        add(new Question("Cache 2 Question 2 ?", "Answer 2", true));
        add(new Question("Cache 2 Question 3 ?", "Answer 3", true));
    }};

    // third cache Questions
    private List<Question> questionsThirdCache = new ArrayList<Question>() {{
        add(new Question("Cache 3 Question 1 ?", "Answer 1", false));
        add(new Question("Cache 3 Question 2 ?", "Answer 2", true));
        add(new Question("Cache 3 Question 3 ?", "Answer 3", true));
    }};

    private List<Question> displayedQuestions = null;
    LinearLayout containerQuestion0;
    TextView txtQuestion0;
    EditText inputAnswer0;
    Button btn_Check0;
    LinearLayout containerQuestion1;
    TextView txtQuestion1;
    EditText inputAnswer1;
    Button btn_Check1;
    LinearLayout containerQuestion2;
    TextView txtQuestion2;
    EditText inputAnswer2;
    Button btn_Check2;

    private int id;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_questions);

        // get catch id
        id = getIntent().getIntExtra("id_catch", -1);

        // todo to test uncomment bellow
        // id = 1;

        if (id == -1) {
            startActivity(new Intent(getApplicationContext(), MapsActivity2.class));
            finish();
        }
        // check if cache already completed
        if (Utils.getPreferenceBoolean(getApplicationContext(), "id_cache_" + id)) {
            Utils.showToast(getApplicationContext(), "You have already Completed This Level");
            startActivity(new Intent(getApplicationContext(), MapsActivity2.class));
            finish();
        }

        // UI
        findViewById(R.id.btn_questions_back).setOnClickListener(e -> {
            startActivity(new Intent(getApplicationContext(), MapsActivity2.class));
            finish();
        });

        containerQuestion0 = findViewById(R.id.container_question_0);
        txtQuestion0 = findViewById(R.id.txt_question_0);
        inputAnswer0 = findViewById(R.id.input_answer_0);
        btn_Check0 = findViewById(R.id.btn_check_0);

        containerQuestion1 = findViewById(R.id.container_question_1);
        txtQuestion1 = findViewById(R.id.txt_question_1);
        inputAnswer1 = findViewById(R.id.input_answer_1);
        btn_Check1 = findViewById(R.id.btn_check_1);

        containerQuestion2 = findViewById(R.id.container_question_2);
        txtQuestion2 = findViewById(R.id.txt_question_2);
        inputAnswer2 = findViewById(R.id.input_answer_2);
        btn_Check2 = findViewById(R.id.btn_check_2);

        if (id == 0) displayedQuestions = questionsFirstCache;
        else if (id == 1) displayedQuestions = questionsSecondCache;
        else if (id == 2) displayedQuestions = questionsThirdCache;

        // fill UI
        containerQuestion0.setVisibility(displayedQuestions.get(0).isHidden() ? View.INVISIBLE : View.VISIBLE);
        txtQuestion0.setText(displayedQuestions.get(0).getText());
        btn_Check0.setOnClickListener(e -> {

            if (inputAnswer0.getText().toString().equals(displayedQuestions.get(0).getCorrectAnswer())) {
                displayedQuestions.get(1).setHidden(false);
                containerQuestion1.setVisibility(View.VISIBLE);
                Utils.showToast(getApplicationContext(), "Correct Answer !");
            } else Utils.showToast(getApplicationContext(), "Wrong Answer.");

        });

        containerQuestion1.setVisibility(displayedQuestions.get(1).isHidden() ? View.INVISIBLE : View.VISIBLE);
        txtQuestion1.setText(displayedQuestions.get(1).getText());
        btn_Check1.setOnClickListener(e -> {
            if (inputAnswer1.getText().toString().equals(displayedQuestions.get(1).getCorrectAnswer())) {
                displayedQuestions.get(2).setHidden(false);
                containerQuestion2.setVisibility(View.VISIBLE);
                Utils.showToast(getApplicationContext(), "Correct Answer !");
            } else Utils.showToast(getApplicationContext(), "Wrong Answer.");

        });

        containerQuestion2.setVisibility(displayedQuestions.get(2).isHidden() ? View.INVISIBLE : View.VISIBLE);
        txtQuestion2.setText(displayedQuestions.get(2).getText());
        btn_Check2.setOnClickListener(e -> {
            if (inputAnswer2.getText().toString().equals(displayedQuestions.get(2).getCorrectAnswer())) {
                Utils.showToast(getApplicationContext(), "Correct Answer ,Go to next location");

                // save as completed Cache
                SharedPreferences sh = getSharedPreferences(Utils.PREFERENCE_CODE, Context.MODE_PRIVATE);
                SharedPreferences.Editor editor = sh.edit();
                editor.putBoolean("id_cache_" + id, true);
                editor.apply();

                startActivity(new Intent(getApplicationContext(), MapsActivity2.class));
            } else Utils.showToast(getApplicationContext(), "Wrong Answer.");
        });

    }
}

class Question {
    private String text;
    private String correctAnswer;
    private boolean isHidden;

    public Question(String text, String correctAnswer, boolean isHidden) {
        this.text = text;
        this.correctAnswer = correctAnswer;
        this.isHidden = isHidden;
    }

    public boolean isHidden() {
        return isHidden;
    }

    public void setHidden(boolean hidden) {
        isHidden = hidden;
    }

    public String getText() {
        return text;
    }

    public void setText(String text) {
        this.text = text;
    }

    public String getCorrectAnswer() {
        return correctAnswer;
    }

    public void setCorrectAnswer(String correctAnswer) {
        this.correctAnswer = correctAnswer;
    }
}