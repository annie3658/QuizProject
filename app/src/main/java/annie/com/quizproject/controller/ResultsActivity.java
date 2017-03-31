package annie.com.quizproject.controller;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.RatingBar;
import android.widget.TextView;

import java.util.ArrayList;
import annie.com.quizproject.R;
/**
 * Created by Annie on 30/03/2017.
 */

public class ResultsActivity extends AppCompatActivity {

    private RatingBar ratingBar;
    private TextView numberOfQuestionsAnswered;
    private TextView scoreRating;
    private Button btnRestartGame,btnViewScores;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.results_activity);

        init();

        ratingBar.setNumStars(5);
        ratingBar.setStepSize(0.5f);
        Bundle b = getIntent().getExtras();
        int score= b.getInt("score");
        int totalQs= b.getInt("totalQs");
        final String language=b.getString("Language");
        String username=b.getString("Username");

        ratingBar.setRating((float)(score*5)/totalQs);

        switch (language)
        {
            case "English":
                setEnglish(username,score,totalQs);
                break;
            case "Romanian":
                setRomanian(username,score,totalQs);
                break;
            default:
                setEnglish(username,score,totalQs);
        }

        btnRestartGame.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(ResultsActivity.this, MainActivity.class);
                startActivity(intent);
            }
        });
    }

    public void init()
    {
        ratingBar=(RatingBar)findViewById(R.id.ratingBar);
        btnRestartGame=(Button)findViewById(R.id.btnStartNewGame);
        btnViewScores=(Button)findViewById(R.id.btnViewScores);
        numberOfQuestionsAnswered =(TextView)findViewById(R.id.tvNumberOfQuestionsAnswered);
        scoreRating=(TextView)findViewById(R.id.tvScoreRating);
    }

    public void setEnglish(String username, int score, int noOfQs)
    {
        numberOfQuestionsAnswered.setText(username+" has answered "+score+" of "+noOfQs+" questions  correctly!");

        float percentage=(score*100)/noOfQs;

        if (percentage>=80 && percentage<=100){
            scoreRating.setText("Score is Excellent !");
        }else if(percentage>=70 && percentage<=79){
            scoreRating.setText("Score is Best");
        }else if(percentage>=60 && percentage<=69){
            scoreRating.setText("Score is Good");
        }else if(percentage>=50 && percentage<=59){
            scoreRating.setText("Score is Average!");
        }else if(percentage>=33 && percentage<=49){
            scoreRating.setText("Score is  Below Average!");
        }else{
            scoreRating.setText("Score is Poor! You need to practice more!");
        }

    }

    public void setRomanian(String username, int score, int noOfQs)
    {
        numberOfQuestionsAnswered.setText(username+" ați răspuns corect la "+score+" întrebări din "+noOfQs+" !");

        float percentage=(score*100)/noOfQs;

        if (percentage>=80 && percentage<=100){
            scoreRating.setText("Scorul este excellent !");
        }else if(percentage>=70 && percentage<=79){
            scoreRating.setText("Scorul este bun");
        }else if(percentage>=60 && percentage<=69){
            scoreRating.setText("Scorul este peste medie");
        }else if(percentage>=50 && percentage<=59){
            scoreRating.setText("Scorul este în medie");
        }else if(percentage>=33 && percentage<=49){
            scoreRating.setText("Scorul este sub medie");
        }else{
            scoreRating.setText("Scor mic :( ");
        }
    }
}
