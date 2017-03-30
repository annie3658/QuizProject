package annie.com.quizproject.controller;

import android.app.Dialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Random;

import annie.com.quizproject.database.DatabaseEnAccess;
import annie.com.quizproject.database.DatabaseRoAccess;
import annie.com.quizproject.model.Question;
import annie.com.quizproject.R;

/**
 * Created by Annie on 30/03/2017.
 */

public class QuizActivity extends AppCompatActivity {

    private List<Question> allQuestions = null, questionsList;
    private Question currentQuestion;

    private TextView tvQuestion, tvNoOfQuestions, tvShowAnswer, timer;
    private RadioButton rbtnA, rbtnB, rbtnC, rbtnD;
    private Button btnNext, btnShowAnswer, btnFifty, btnCancel;

    private int obtainedScore = 0, questionId = 0, answeredQsNo;
    private ArrayList<String> userAnswersList;
    private boolean defaultLanguage = false, pressedFiftyButton = false, isFinished=false;
    private DatabaseEnAccess databaseEnAccess;
    private DatabaseRoAccess databaseRoAccess;
    private String correctToast, incorrectToast,selectABtnToast;
    private CountDownTimer countDownTimer;
    long millisUntilFinished=15000, interval=1000;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.quiz_activity);

        Bundle extras = getIntent().getExtras();
        final String language = extras.getString("Language");
        databaseEnAccess = DatabaseEnAccess.getInstance(this);
        databaseRoAccess = DatabaseRoAccess.getInstance(this);

        switch (language) {
            case "English":
                correctToast = "Correct";
                incorrectToast = "Incorrect";
                selectABtnToast="Please select an answer";
                defaultLanguage = false;
                databaseEnAccess.open();
                allQuestions = databaseEnAccess.getEnglishQuestions();
                Collections.shuffle(allQuestions);
                questionsList = new ArrayList<Question>(allQuestions.subList(0, 10));
                init();
                currentQuestion = questionsList.get(questionId);
                setQuestionsView();
                databaseEnAccess.close();

                break;
            case "Romanian":
                correctToast = "Corect";
                incorrectToast = "Greșit";
                selectABtnToast="Selectați un răspuns";
                defaultLanguage = true;
                databaseRoAccess.open();
                allQuestions = databaseRoAccess.getRomanianQuestions();
                Collections.shuffle(allQuestions);
                questionsList = new ArrayList<Question>(allQuestions.subList(0, 10));
                init();
                currentQuestion = questionsList.get(questionId);
                setQuestionsView();
                databaseRoAccess.close();

                break;
            default:
                correctToast = "Correct";
                incorrectToast = "Incorrect";
                selectABtnToast="Please select an answer";
                defaultLanguage = false;
                databaseEnAccess.open();
                allQuestions = databaseEnAccess.getEnglishQuestions();
                Collections.shuffle(allQuestions);
                questionsList = new ArrayList<Question>(allQuestions.subList(0, 10));
                init();
                currentQuestion = questionsList.get(questionId);
                setQuestionsView();
                databaseEnAccess.close();

        }


        btnNext.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Log.i("nextBtn","Entered next button");
                RadioGroup grp = (RadioGroup) findViewById(R.id.radioGroup);
                RadioButton answer = (RadioButton) findViewById(grp.getCheckedRadioButtonId());


                    Log.e("Answer ID", "Selected Positioned  value - " + grp.getCheckedRadioButtonId());

                    if (answer != null) {

                        tvShowAnswer.setText("");
                        if (pressedFiftyButton == true) {
                            if (rbtnA.isEnabled() == false) rbtnA.setEnabled(true);
                            if (rbtnB.isEnabled() == false) rbtnB.setEnabled(true);
                            if (rbtnC.isEnabled() == false) rbtnC.setEnabled(true);
                            if (rbtnD.isEnabled() == false) rbtnD.setEnabled(true);
                        }

                        Log.e("Answer", currentQuestion.getAnswer() + " -- " + answer.getText());

                        //userAnswersList.add("" + answer.getText());

                        if (currentQuestion.getAnswer().equals(answer.getText())) {
                            obtainedScore++;
                            Toast.makeText(QuizActivity.this, correctToast, Toast.LENGTH_SHORT).show();
                            Log.e("comments", "Correct Answer");
                            Log.d("score", "Obtained score " + obtainedScore);
                        } else {
                            Log.e("comments", "Wrong Answer");
                            Toast.makeText(QuizActivity.this, incorrectToast, Toast.LENGTH_SHORT).show();
                        }
                        if (questionId < questionsList.size()) {
                            currentQuestion = questionsList.get(questionId);
                            setQuestionsView();
                            countDownTimer.start();
                        } else {

                            final Bundle b = new Bundle();
                            b.putInt("score", obtainedScore);
                            b.putInt("totalQs", questionsList.size());
                            b.putString("Language", language);

                            View view = (LayoutInflater.from(QuizActivity.this)).inflate(R.layout.user_alert_dialog_layout, null);
                            AlertDialog.Builder alertBuilder = new AlertDialog.Builder(QuizActivity.this);
                            alertBuilder.setView(view);
                            final EditText etUserName = (EditText) view.findViewById(R.id.editTextUserName);
                            alertBuilder.setCancelable(true).setPositiveButton("Ok", new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialog, int which) {
                                    if (etUserName.getText().toString().isEmpty() == false) {
                                        String username = etUserName.getText().toString();
                                        b.putString("Username", username);
                                        Intent intent = new Intent(QuizActivity.this, ResultsActivity.class);
                                        intent.putExtras(b);
                                        startActivity(intent);
                                        finish();
                                    } else {
                                        Toast.makeText(QuizActivity.this, "Please enter your name", Toast.LENGTH_SHORT).show();
                                    }
                                }
                            });
                            Dialog dialog = alertBuilder.create();
                            dialog.show();


                        }

                    } else {
                        Log.e("comments", "No Answer");
                        Toast.makeText(QuizActivity.this, selectABtnToast, Toast.LENGTH_SHORT).show();
                    }

                grp.clearCheck();

            }
        });

        btnShowAnswer.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                tvShowAnswer.setText(currentQuestion.getAnswer());
                btnShowAnswer.setEnabled(false);


            }
        });

        btnCancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Intent intent = new Intent(QuizActivity.this, MainActivity.class);
                startActivity(intent);
            }
        });

        btnFifty.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {


                Random random = new Random();
                pressedFiftyButton = true;
                int i = 0, usedNumber = 0;
                int correctAnswer = getCorrectAnswerButton();
                while (i < 2) {
                    int number = random.nextInt(4 - 1 + 1) + 1;
                    Log.i("50btn", "correct answer:" + String.valueOf(correctAnswer) + " random=" + String.valueOf(number) + " i=" + String.valueOf(i));

                    if (number != correctAnswer && number != usedNumber) {

                        switch (number) {
                            case 1:
                                rbtnA.setEnabled(false);
                                usedNumber = number;
                                break;
                            case 2:
                                rbtnB.setEnabled(false);
                                usedNumber = number;
                                break;
                            case 3:
                                rbtnC.setEnabled(false);
                                usedNumber = number;
                                break;
                            case 4:
                                rbtnD.setEnabled(false);
                                usedNumber = number;
                                break;
                        }

                        i++;
                    }

                }
                btnFifty.setEnabled(false);

            }
        });


        countDownTimer=new CountDownTimer(millisUntilFinished,interval) {
            @Override
            public void onTick(long millisUntilFinished) {

                timer.setText("00:"+millisUntilFinished/interval);


            }

            @Override
            public void onFinish() {

                if (questionId < questionsList.size()) {
                    currentQuestion = questionsList.get(questionId);
                    setQuestionsView();
                } else {

                    final Bundle b = new Bundle();
                    b.putInt("score", obtainedScore);
                    b.putInt("totalQs", questionsList.size());
                    b.putString("Language", language);

                    View view = (LayoutInflater.from(QuizActivity.this)).inflate(R.layout.user_alert_dialog_layout, null);
                    AlertDialog.Builder alertBuilder = new AlertDialog.Builder(QuizActivity.this);
                    alertBuilder.setView(view);
                    final EditText etUserName = (EditText) view.findViewById(R.id.editTextUserName);
                    alertBuilder.setCancelable(true).setPositiveButton("Ok", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            if (etUserName.getText().toString().isEmpty() == false) {
                                String username = etUserName.getText().toString();
                                b.putString("Username", username);
                                Intent intent = new Intent(QuizActivity.this, ResultsActivity.class);
                                intent.putExtras(b);
                                startActivity(intent);
                                finish();
                            } else {
                                Toast.makeText(QuizActivity.this, "Please enter your name", Toast.LENGTH_SHORT).show();
                            }
                        }
                    });
                    Dialog dialog = alertBuilder.create();
                    dialog.show();


                }

             countDownTimer.start();
            }
        }.start();


    }



    public int getCorrectAnswerButton() {

        if (currentQuestion.getAnswer().equals(rbtnA.getText().toString()))
            return 1;
        else if (currentQuestion.getAnswer().equals(rbtnB.getText().toString()))
            return 2;
        else if (currentQuestion.getAnswer().equals(rbtnC.getText().toString()))
            return 3;
        else if (currentQuestion.getAnswer().equals(rbtnD.getText().toString()))
            return 4;
        else return 0;
    }

    public void init() {
        tvShowAnswer = (TextView) findViewById(R.id.textViewAnsweredShown);
        tvNoOfQuestions = (TextView) findViewById(R.id.tvNoOfQuestions);
        tvQuestion = (TextView) findViewById(R.id.tvQuestion);
        timer=(TextView) findViewById(R.id.textViewTimer);
        rbtnA = (RadioButton) findViewById(R.id.optionARadioBtn);
        rbtnB = (RadioButton) findViewById(R.id.optionBRadioBtn);
        rbtnC = (RadioButton) findViewById(R.id.optionCRadioBtn);
        rbtnD = (RadioButton) findViewById(R.id.optionDRadioBtn);
        btnNext = (Button) findViewById(R.id.nextBtn);
        btnFifty = (Button) findViewById(R.id.fiftyBtn);
        btnShowAnswer = (Button) findViewById(R.id.showAnswerBtn);
        btnCancel = (Button) findViewById(R.id.cancelButton);

        userAnswersList = new ArrayList<String>();
    }


    private void setQuestionsView() {
        rbtnA.setChecked(false);
        rbtnB.setChecked(false);
        rbtnC.setChecked(false);
        rbtnD.setChecked(false);
        timer.setText("00:15");
        answeredQsNo = questionId + 1;
        if (defaultLanguage == false) {
            tvNoOfQuestions.setText("Questions " + answeredQsNo + " of " + questionsList.size());
        } else {
            tvNoOfQuestions.setText("Întrebarea " + answeredQsNo + " din " + questionsList.size());
        }

        tvQuestion.setText(currentQuestion.getQuestion());
        rbtnA.setText(currentQuestion.getOptionA());
        rbtnB.setText(currentQuestion.getOptionB());
        rbtnC.setText(currentQuestion.getOptionC());
        rbtnD.setText(currentQuestion.getOptionD());

        questionId++;
    }

}
