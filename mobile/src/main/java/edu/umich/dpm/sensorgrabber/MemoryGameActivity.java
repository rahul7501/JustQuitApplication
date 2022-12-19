package edu.umich.dpm.sensorgrabber;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.view.View;
import android.widget.AdapterView;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.Toast;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import edu.umich.dpm.sensorgrabber.adapters.ImageAdapter;

public class MemoryGameActivity extends AppCompatActivity {

    FirebaseDatabase firebaseDatabase;
    static DatabaseReference databaseReferenceUser;
    static String accountId = null;
    SharedPreferences prefs;
    static int userPoints = 0;
    ImageView curView = null;
    private int countPair = 0;
    final int[] drawable = new int[] {R.drawable.gamepic1, R.drawable.gamepic2,
            R.drawable.gamepic3, R.drawable.gamepic4, R.drawable.gamepic5, R.drawable.gamepic6,
            R.drawable.gamepic7, R.drawable.gamepic8};
    int[] pos = {0,1,2,3,4,5,6,7,0,1,2,3,4,5,6,7};
    int currentPos = -1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        prefs = PreferenceManager.getDefaultSharedPreferences(this);

        accountId = prefs.getString("accountId", "");
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_memory_game);

        GridView gridView = (GridView) findViewById(R.id.gridView);
        ImageAdapter imageAdapter = new ImageAdapter(this);
        gridView.setAdapter(imageAdapter);

        gridView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                if(currentPos<0)
                {
                    currentPos=i;
                    curView=(ImageView) view;
                    ((ImageView)view).setImageResource(drawable[pos[i]]);
                }
                else
                {
                    if(currentPos==i)
                    {
                        ((ImageView)view).setImageResource(R.drawable.hidden);
                    }
                    else if(pos[currentPos]!=pos[i])
                    {
                        curView.setImageResource(R.drawable.hidden);
                        Toast.makeText(getApplicationContext(),"Not Match", Toast.LENGTH_SHORT).show();
                    }
                    else
                    {
                        ((ImageView)view).setImageResource(drawable[pos[i]]);
                        countPair++;

                        if(countPair==0){
                            Toast.makeText(getApplicationContext(),"Not Match", Toast.LENGTH_SHORT).show();
                        }
                    }
                    currentPos=-1;
                }
            }
        });
        databaseReferenceUser = FirebaseDatabase.getInstance().getReference("users").child(accountId).child("points");

    }
    public void btnBack(View view) {
        getPoints();
        addPoints();
        startActivity(new Intent(MemoryGameActivity.this, DashboardActivity.class));
        //Toast.makeText(this, "make functional", Toast.LENGTH_SHORT).show();

    }

    public static void addPoints() {
        databaseReferenceUser.setValue(userPoints + 10);
    }

    public static void getPoints() {
        databaseReferenceUser.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                userPoints = snapshot.getValue(Integer.class);


            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                //Toast.makeText(getActivity(), "ERROR: " + databaseError.toString(), Toast.LENGTH_SHORT).show();
            }
        });
    }
}