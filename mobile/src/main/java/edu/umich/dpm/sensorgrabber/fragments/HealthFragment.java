package edu.umich.dpm.sensorgrabber.fragments;

import android.annotation.SuppressLint;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;


import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.concurrent.TimeUnit;

import edu.umich.dpm.sensorgrabber.R;

public class HealthFragment extends Fragment {

    FirebaseDatabase firebaseDatabase;
    DatabaseReference databaseReferenceUser;
    DatabaseReference databaseReferenceLastCigaretteTime;


    TextView txtPoints;
    static long lastSmokedCigMillis=0;


    TextView healthProgress;
    TextView nicotineProgress;
    TextView bpProgress;

    ProgressBar healthBar;
    ProgressBar nicoteneBar;
    ProgressBar bpBar;

    int userPoints = 0;





    public HealthFragment() {
    }

    public static HealthFragment newInstance(String param1, String param2) {
        HealthFragment fragment = new HealthFragment();
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }

    @SuppressLint("MissingInflatedId")
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_health, container, false);

        txtPoints = (TextView) view.findViewById(R.id.txtPoints);

        healthProgress = (TextView) view.findViewById(R.id.healthProgressNumber);
        nicotineProgress = (TextView) view.findViewById(R.id.nicotineProgressNumber);
        bpProgress = (TextView) view.findViewById(R.id.bpProgressNumber);

        healthBar = (ProgressBar) view.findViewById(R.id.healthProgressBar);
        nicoteneBar = (ProgressBar) view.findViewById(R.id.nicotineProgressBar);
        bpBar = (ProgressBar) view.findViewById(R.id.bpProgressBar);

        SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(getActivity());
        String accountId = prefs.getString("accountId", "");

        firebaseDatabase = FirebaseDatabase.getInstance();

        //POINTS
        databaseReferenceUser = FirebaseDatabase.getInstance().getReference("users").child(accountId).child("points");

        databaseReferenceUser.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                userPoints = snapshot.getValue(Integer.class);

                txtPoints.setText("" + userPoints + " Pts");
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                Toast.makeText(getActivity(), "ERROR: " + databaseError.toString(), Toast.LENGTH_SHORT).show();
            }
        });

        databaseReferenceLastCigaretteTime = FirebaseDatabase.getInstance().getReference("users").child(accountId).child("lastCigaretteTime");
        databaseReferenceLastCigaretteTime.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                lastSmokedCigMillis = snapshot.getValue(Long.class);
                updateHealthStatus(lastSmokedCigMillis);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });

        //Updating health cards
        //updateHealthStatus();

        return view;
    }

    public void updateHealthStatus(long lastSmokedCigMillisDatabase )
    {

        long currentTimeMillis = System.currentTimeMillis();
        long currentTimeMinutes = currentTimeMillis/60000;
        long lastSmokedCigMinutesDatabase = lastSmokedCigMillisDatabase/60000;
        long timeDifferenceMinutes = currentTimeMinutes - lastSmokedCigMinutesDatabase;

        Log.d("currenttime",String.valueOf(currentTimeMinutes));
        Log.d("databaseTime",String.valueOf(lastSmokedCigMinutesDatabase));
        Log.d("time differ",String.valueOf(timeDifferenceMinutes));

        if(timeDifferenceMinutes>20)
        {
            healthBar.setProgress(100);
            bpBar.setProgress(100);
            healthProgress.setText("100%");
            bpProgress.setText("100%");
        }
        else
        {
            double percentageProgressMinutes = (timeDifferenceMinutes/20.0)*100.0;
            Log.d("percentage",percentageProgressMinutes+"");

            healthBar.setProgress((int)percentageProgressMinutes);
            bpBar.setProgress((int)percentageProgressMinutes);

            healthProgress.setText(""+(int)percentageProgressMinutes+"%");
            bpProgress.setText(""+(int)percentageProgressMinutes+"%");
        }

        if(timeDifferenceMinutes>(3*24*60))
        {
            nicoteneBar.setProgress(100);
            nicotineProgress.setText("100%");
        }
        else
        {
            double percentageProgressDays = (timeDifferenceMinutes/(3.0*24.0*60.0))*100.0;
            nicoteneBar.setProgress((int)percentageProgressDays);
            nicotineProgress.setText(""+(int)percentageProgressDays+"%");
        }


    }

//    public String MillitoDate(long lastSmokedCig)
//    {
//        DateFormat formatter = new SimpleDateFormat("dd/MM/yyyy HH:mm:ss");
//        Date date = new Date(lastSmokedCig);
//        String dateAndTime=formatter.format(date);
//        return dateAndTime;
//    }


}