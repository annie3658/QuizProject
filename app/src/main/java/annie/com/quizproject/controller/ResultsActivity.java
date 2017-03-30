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
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.results_activity);

        ratingBar=(RatingBar)findViewById(R.id.ratingBar);
        ratingBar.setNumStars(5);
        ratingBar.setStepSize(0.5f);


        numberOfQuestionsAnswered =(TextView)findViewById(R.id.tvNumberOfQuestionsAnswered);
        scoreRating=(TextView)findViewById(R.id.tvScoreRating);

        Bundle b = getIntent().getExtras();
        int score= b.getInt("score");
        int totalQs= b.getInt("totalQs");
        final String language=b.getString("Language");
        String username=b.getString("Username");
        float percentage=0;
        ratingBar.setRating((float)(score*5)/totalQs);

        switch (language)
        {
            case "English":
                numberOfQuestionsAnswered.setText(username+" has answered "+score+" of "+totalQs+" questions  correctly!");

                percentage=(score*100)/totalQs;
                //ratingBar.setRating(percentage);

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

                break;
            case "Romanian":

                numberOfQuestionsAnswered.setText(username+" ați răspuns corect la "+score+" întrebări din "+totalQs+" !");

                percentage=(score*100)/totalQs;
                //ratingBar.setRating(percentage);

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

                break;
            default:
                numberOfQuestionsAnswered.setText(username+" has answered "+score+" of "+totalQs+" questions  correctly!");

                percentage=(score*100)/totalQs;

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



    }
}
