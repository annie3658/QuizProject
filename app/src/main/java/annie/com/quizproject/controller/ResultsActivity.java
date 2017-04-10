package annie.com.quizproject.controller;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.RatingBar;
import android.widget.TextView;

import com.facebook.CallbackManager;
import com.facebook.FacebookSdk;
import com.facebook.login.LoginManager;
import com.facebook.share.ShareApi;
import com.facebook.share.model.ShareLinkContent;
import com.facebook.share.model.SharePhoto;
import com.facebook.share.model.SharePhotoContent;
import com.facebook.share.widget.ShareButton;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import annie.com.quizproject.R;
/**
 * Created by Annie on 30/03/2017.
 */

public class ResultsActivity extends AppCompatActivity {

    private RatingBar ratingBar;
    private TextView numberOfQuestionsAnswered;
    private TextView scoreRating;
    private Button btnRestartGame,btnViewScores, btnShare;
    private int score;
    private String caption;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.results_activity);

        FacebookSdk.sdkInitialize(getApplicationContext());

        ShareButton fbShareButton = (ShareButton) findViewById(R.id.btnShare);

        init();

        ratingBar.setNumStars(5);
        ratingBar.setStepSize(0.5f);
        Bundle b = getIntent().getExtras();
        score= b.getInt("score");
        int totalQs= b.getInt("totalQs");
        final String language=b.getString("Language");
        String username=b.getString("Username");


        //Bitmap image = BitmapFactory.decodeResource(getResources(), R.drawable.stars_0);
        Bitmap image=sharePhotoToFacebook(score);
        SharePhoto photo = new SharePhoto.Builder()
                .setBitmap(image)
                .build();
        SharePhotoContent content = new SharePhotoContent.Builder()
                .addPhoto(photo)
                .build();
        fbShareButton.setShareContent(content);


        ratingBar.setRating((float)(score*5)/totalQs);

        switch (language)
        {
            case "English":
                setEnglish(username,score,totalQs);
                caption="These are my results:";
                break;
            case "Romanian":
                caption="Acestea sunt rezultatele mele:";
                setRomanian(username,score,totalQs);
                break;
            default:
                caption="These are my results:";
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
        //btnShare=(Button)findViewById(R.id.shareBtn);
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



    public Bitmap sharePhotoToFacebook(int sco){
        Bitmap image=null;
        switch(sco)
        {
            case 0:
                image=BitmapFactory.decodeResource(getResources(), R.drawable.stars_0);
               break;
            case 1:
                image=BitmapFactory.decodeResource(getResources(), R.drawable.stars_1);

                break;
            case 2:
                image=BitmapFactory.decodeResource(getResources(), R.drawable.stars_2);

                break;
            case 3:
                image=BitmapFactory.decodeResource(getResources(), R.drawable.stars_3);

                break;
            case 4:
                image=BitmapFactory.decodeResource(getResources(), R.drawable.stars_4);

                break;
            case 5:
                image=BitmapFactory.decodeResource(getResources(), R.drawable.stars_5);

                break;
            case 6:
                image=BitmapFactory.decodeResource(getResources(), R.drawable.stars_6);

                break;
            case 7:
                image=BitmapFactory.decodeResource(getResources(), R.drawable.stars_7);

                break;
            case 8:
                image=BitmapFactory.decodeResource(getResources(), R.drawable.stars_8);

                break;
            case 9:
                image=BitmapFactory.decodeResource(getResources(), R.drawable.stars_9);

                break;
            case 10:
                image=BitmapFactory.decodeResource(getResources(), R.drawable.stars_10);

                break;
           /* default:
                image=BitmapFactory.decodeResource(getResources(), R.drawable.stars_0);*/


        }

        return image;

    }


}
