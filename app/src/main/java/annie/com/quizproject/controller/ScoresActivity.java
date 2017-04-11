package annie.com.quizproject.controller;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.ListView;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;

import annie.com.quizproject.R;
import annie.com.quizproject.adapter.ListviewAdapter;
import annie.com.quizproject.model.User;

/**
 * Created by Annie on 11/04/2017.
 */

public class ScoresActivity extends AppCompatActivity {

    private ListView listView;
    private DatabaseReference databaseUsers;
    private List<User> usersList;
    private Button btnStartGame;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.scores_activity);
        databaseUsers= FirebaseDatabase.getInstance().getReference("users");
        listView=(ListView)findViewById(R.id.scoresListView);
        btnStartGame=(Button)findViewById(R.id.startGameBtn);
        usersList=new ArrayList<>();

        btnStartGame.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(ScoresActivity.this, MainActivity.class);
                startActivity(intent);
            }
        });
    }

    @Override
    protected void onStart() {
        super.onStart();

        databaseUsers.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {

                usersList.clear();
                for(DataSnapshot userSnapshot: dataSnapshot.getChildren())
                {
                  User user=userSnapshot.getValue(User.class);
                    usersList.add(user);
                }
                ListviewAdapter adapter=new ListviewAdapter(ScoresActivity.this,usersList);
                listView.setAdapter(adapter);

            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
    }
}
