package annie.com.quizproject.controller;

import android.content.Intent;
import android.content.res.Configuration;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.Spinner;

import java.util.Locale;

import annie.com.quizproject.R;
import annie.com.quizproject.adapter.SpinnerAdapter;

public class MainActivity extends AppCompatActivity {

    private String[] languages = {"English", "Romanian"};
    private int[] images = {R.drawable.uk_flag, R.drawable.ro_flag};
    private Button mStartButton;
    private Spinner mSpinner;
    private String selectedLanguage = "English";
    Configuration config = new Configuration();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        mStartButton = (Button) findViewById(R.id.startBtn);
        mStartButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Intent intent = new Intent(MainActivity.this, QuizActivity.class);
                intent.putExtra("Language", selectedLanguage);
                startActivity(intent);

            }
        });
        mSpinner = (Spinner) findViewById(R.id.spinner);

        SpinnerAdapter adapter = new SpinnerAdapter(this, languages, images);
        mSpinner.setAdapter(adapter);


        mSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> arg0, View arg1,
                                       int arg2, long arg3) {

                {
                    switch (arg2) {
                        case 0:
                            config.locale = Locale.ENGLISH;
                            selectedLanguage = "English";
                            break;
                        case 1:

                            Locale locale = new Locale("ro");
                            Locale.setDefault(locale);
                            config.locale = locale;
                            selectedLanguage = "Romanian";
                            break;
                        default:
                            config.locale = Locale.ENGLISH;
                            selectedLanguage = "English";
                            break;
                    }
                    getResources().updateConfiguration(config, null);
                }

            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });
    }

}
