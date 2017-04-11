package annie.com.quizproject.controller;

import android.app.Dialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Random;
import java.util.concurrent.TimeUnit;

import android.os.Handler;

import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import annie.com.quizproject.database.DatabaseEnAccess;
import annie.com.quizproject.database.DatabaseRoAccess;
import annie.com.quizproject.model.Question;
import annie.com.quizproject.R;
import annie.com.quizproject.model.User;

/**
 * Created by Annie on 30/03/2017.
 */
public class QuizActivity extends AppCompatActivity {

    private List<Question> allQuestions = null, questionsList;
    private Question currentQuestion;
    private TextView tvQuestion, tvNoOfQuestions, tvShowAnswer, timer;
    private RadioButton rbtnA, rbtnB, rbtnC, rbtnD, answer;
    private RadioGroup grp;
    private Button btnNext, btnShowAnswer, btnFifty, btnCancel;
    private int obtainedScore = 0, questionId = 0, answeredQsNo;
    private boolean defaultLanguage = false, pressedFiftyButton, pressedShowAnswerButton;
    private DatabaseEnAccess databaseEnAccess;
    private DatabaseRoAccess databaseRoAccess;
    private String correctToast, incorrectToast, selectABtnToast, enterNameToast, usernameInput,selectedLanguage;
    private CountDownTimer countDownTimer;
    private long millisUntilFinished = 15000, interval = 1000;
    private final Bundle b = new Bundle();
    private DatabaseReference databaseUsers;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.quiz_activity);

        databaseUsers= FirebaseDatabase.getInstance().getReference("users");

        Bundle extras = getIntent().getExtras();
        final String language = extras.getString("Language");
        selectedLanguage=language;
        databaseEnAccess = DatabaseEnAccess.getInstance(this);
        databaseRoAccess = DatabaseRoAccess.getInstance(this);

        switch (language) {
            case "English":
                setEnglish();
                break;
            case "Romanian":
                setRomanian();
                break;
            default:
                setEnglish();

        }
        btnNext.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                answer = (RadioButton) findViewById(grp.getCheckedRadioButtonId());
                if (answer != null) {
                    if (currentQuestion.getAnswer().equals(answer.getText())) {
                        obtainedScore++;
                        Toast.makeText(QuizActivity.this, correctToast, Toast.LENGTH_SHORT).show();
                        answer.setTextColor(Color.parseColor("#ff99cc00"));

                    } else {
                        answer.setTextColor(Color.parseColor("#ff0000"));
                        Toast.makeText(QuizActivity.this, incorrectToast, Toast.LENGTH_SHORT).show();
                    }
                    if (questionId < questionsList.size()) {
                        currentQuestion = questionsList.get(questionId);
                        disableButtons();
                        delaySetQuestion();
                        delayTimer();

                    } else {
                        countDownTimer.cancel();
                        createBundle(b, language);
                        delayAlertDialog();
                    }

                } else {
                    Toast.makeText(QuizActivity.this, selectABtnToast, Toast.LENGTH_SHORT).show();
                }
                grp.clearCheck();
            }
        });

        btnShowAnswer.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                tvShowAnswer.setText(currentQuestion.getAnswer());
                pressedShowAnswerButton = true;
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

                grp.clearCheck();
                Random random = new Random();
                pressedFiftyButton = true;
                int i = 0, usedNumber = 0;
                int correctAnswer = getCorrectAnswerButton();
                while (i < 2) {
                    int number = random.nextInt(4 - 1 + 1) + 1;

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

        countDownTimer = new CountDownTimer(millisUntilFinished, interval) {
            @Override
            public void onTick(long millisUntilFinished) {

                long millis = millisUntilFinished;
                String time = String.format("%02d:%02d", TimeUnit.MILLISECONDS.toMinutes(millis),
                        TimeUnit.MILLISECONDS.toSeconds(millis) - TimeUnit.MINUTES.toSeconds(TimeUnit.MILLISECONDS.toMinutes(millis)));
                timer.setText(time);
            }

            @Override
            public void onFinish() {
                Log.i("onFinish", "entered onFinish()\n\n");
                answer = (RadioButton) findViewById(grp.getCheckedRadioButtonId());
                if (answer != null) {
                    if (currentQuestion.getAnswer().equals(answer.getText())) {
                        obtainedScore++;
                        Log.i("onFinish -> answer!=null", "correct answer\n\n");
                        Toast.makeText(QuizActivity.this, correctToast, Toast.LENGTH_SHORT).show();
                        answer.setTextColor(Color.parseColor("#ff99cc00"));

                    } else {
                        Log.i("onFinish -> answer!=null", "incorrect answer\n\n");
                        answer.setTextColor(Color.parseColor("#ff0000"));
                        Toast.makeText(QuizActivity.this, incorrectToast, Toast.LENGTH_SHORT).show();
                    }
                    if (questionId < questionsList.size()) {
                        Log.i("questionID", String.valueOf(questionId) + "\n");
                        currentQuestion = questionsList.get(questionId);
                        delaySetQuestion();
                        delayTimer();
                        Log.i("questionID", String.valueOf(questionId) + "\n");

                    } else {

                        createBundle(b, language);
                        delayAlertDialog();

                    }

                } else {
                    Log.i("onFinish -> answer==null", "null answer\n\n");
                    if (questionId < questionsList.size()) {
                        currentQuestion = questionsList.get(questionId);
                        setQuestionsView();
                        countDownTimer.start();
                        Log.i("onFinish()", "restart time\n\n");
                    } else {
                        countDownTimer.cancel();
                        createBundle(b, language);
                        disableButtons();
                        alertDialog();
                    }
                }
                grp.clearCheck();

            }
        };
        countDownTimer.start();
        Log.i("startTime_1st", "start initial time\n\n");
    }


    public void delaySetQuestion() {
        Log.i("delaySetQuestion", "entered delaySetQuestion()\n\n");
        final Handler handler = new Handler();
        handler.postDelayed(new Runnable() {
            @Override
            public void run() {
                countDownTimer.cancel();
                resetRadioButtons();
                setQuestionsView();
                enableButtons();

            }
        }, 2000);

    }

    public void init() {
        grp = (RadioGroup) findViewById(R.id.radioGroup);
        tvShowAnswer = (TextView) findViewById(R.id.textViewAnsweredShown);
        tvNoOfQuestions = (TextView) findViewById(R.id.tvNoOfQuestions);
        tvQuestion = (TextView) findViewById(R.id.tvQuestion);
        timer = (TextView) findViewById(R.id.textViewTimer);
        rbtnA = (RadioButton) findViewById(R.id.optionARadioBtn);
        rbtnB = (RadioButton) findViewById(R.id.optionBRadioBtn);
        rbtnC = (RadioButton) findViewById(R.id.optionCRadioBtn);
        rbtnD = (RadioButton) findViewById(R.id.optionDRadioBtn);
        btnNext = (Button) findViewById(R.id.nextBtn);
        btnFifty = (Button) findViewById(R.id.fiftyBtn);
        btnShowAnswer = (Button) findViewById(R.id.showAnswerBtn);
        btnCancel = (Button) findViewById(R.id.cancelButton);

    }

    private void setQuestionsView() {
        Log.i("setQuestion", "entered setQuestion()\n\n");
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
        Log.i("questionID", String.valueOf(questionId) + "\n");
        tvQuestion.setText(currentQuestion.getQuestion());
        rbtnA.setText(currentQuestion.getOptionA());
        rbtnB.setText(currentQuestion.getOptionB());
        rbtnC.setText(currentQuestion.getOptionC());
        rbtnD.setText(currentQuestion.getOptionD());
        questionId++;
        Log.i("questionID++", String.valueOf(questionId) + "\n");
    }


    public void delayAlertDialog() {
        final Handler handler = new Handler();
        handler.postDelayed(new Runnable() {
            @Override
            public void run() {
                alertDialog();
            }
        }, 2000);
    }

    public void delayTimer() {
        Log.i("delayTimer", "entered delayTimer()\n\n");
        final Handler handler = new Handler();
        handler.postDelayed(new Runnable() {
            @Override
            public void run() {
                countDownTimer.start();

            }
        }, 2000);
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

    public void alertDialog() {

        LinearLayout layout = new LinearLayout(this);
        LayoutInflater.from(this).inflate(R.layout.user_alert_dialog_layout, layout);

        final EditText etUserName = (EditText) layout.findViewById(R.id.editTextUserName);
        final AlertDialog alertDialog = new AlertDialog.Builder(this)
                .setPositiveButton("Ok", null)
                .setView(layout)
                .create();

        alertDialog.getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_ADJUST_RESIZE);

        alertDialog.show();

        alertDialog.getButton(AlertDialog.BUTTON_POSITIVE).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                usernameInput = etUserName.getEditableText().toString().trim();

                if (usernameInput.length() == 0) {
                    Toast.makeText(QuizActivity.this, enterNameToast, Toast.LENGTH_SHORT).show();
                    return;
                }
                addUser(usernameInput,String.valueOf(obtainedScore));
                startFinalActivity(etUserName.getText().toString(), b);
                alertDialog.dismiss();
            }
        });


    }

    public void startFinalActivity(String username, Bundle b) {
        b.putString("Username", username);
        Intent intent = new Intent(QuizActivity.this, ResultsActivity.class);
        intent.putExtras(b);
        startActivity(intent);
        finish();
    }

    public void createBundle(Bundle bundle, String language) {
        bundle.putInt("score", obtainedScore);
        bundle.putInt("totalQs", questionsList.size());
        bundle.putString("Language", language);
    }

    public void setEnglish() {
        correctToast = "Correct";
        incorrectToast = "Incorrect";
        selectABtnToast = "Please select an answer";
        enterNameToast = "Please enter a name";
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

    public void setRomanian() {
        correctToast = "Corect";
        incorrectToast = "Greșit";
        selectABtnToast = "Selectați un răspuns";
        enterNameToast = "Introduceti un nume vă rog";
        defaultLanguage = true;
        databaseRoAccess.open();
        allQuestions = databaseRoAccess.getRomanianQuestions();
        Collections.shuffle(allQuestions);
        questionsList = new ArrayList<Question>(allQuestions.subList(0, 10));
        init();
        currentQuestion = questionsList.get(questionId);
        setQuestionsView();
        databaseRoAccess.close();
    }

    public void resetRadioButtons() {
        tvShowAnswer.setText("");
        rbtnA.setTextColor(Color.parseColor("#000000"));
        rbtnB.setTextColor(Color.parseColor("#000000"));
        rbtnC.setTextColor(Color.parseColor("#000000"));
        rbtnD.setTextColor(Color.parseColor("#000000"));
        if (pressedFiftyButton == true) {
            if (rbtnA.isEnabled() == false) rbtnA.setEnabled(true);
            if (rbtnB.isEnabled() == false) rbtnB.setEnabled(true);
            if (rbtnC.isEnabled() == false) rbtnC.setEnabled(true);
            if (rbtnD.isEnabled() == false) rbtnD.setEnabled(true);
        }
    }

    public void disableButtons() {
        btnNext.setEnabled(false);
        btnFifty.setEnabled(false);
        btnShowAnswer.setEnabled(false);
        rbtnA.setEnabled(false);
        rbtnB.setEnabled(false);
        rbtnC.setEnabled(false);
        rbtnD.setEnabled(false);
    }

    public void enableButtons() {
        btnNext.setEnabled(true);
        if (pressedFiftyButton == false) {
            btnFifty.setEnabled(true);
        }
        if (pressedShowAnswerButton == false) {
            btnShowAnswer.setEnabled(true);
        }
        rbtnA.setEnabled(true);
        rbtnB.setEnabled(true);
        rbtnC.setEnabled(true);
        rbtnD.setEnabled(true);
    }

    private void addUser(String username, String userScore)
    {
        String id=databaseUsers.push().getKey();
        User user=new User(id,username,selectedLanguage,userScore);
        databaseUsers.child(id).setValue(user);
    }
}
