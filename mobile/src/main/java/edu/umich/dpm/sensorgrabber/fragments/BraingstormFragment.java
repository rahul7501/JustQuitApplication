package edu.umich.dpm.sensorgrabber.fragments;

import android.content.Intent;
import android.content.SharedPreferences;
import android.net.Uri;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;


import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import edu.umich.dpm.sensorgrabber.ComicsActivity;
import edu.umich.dpm.sensorgrabber.ExerciseActivity;
import edu.umich.dpm.sensorgrabber.MemoryGameActivity;
import edu.umich.dpm.sensorgrabber.R;

public class BraingstormFragment extends Fragment {

    ImageView imgMemoryGame, imgComic, imgExercise;

    FirebaseDatabase firebaseDatabase;
    static DatabaseReference databaseReferenceUser;

    static TextView txtPoints;

    static int userPoints = 0;

    public BraingstormFragment() {
    }

    public static BraingstormFragment newInstance(String param1, String param2) {
        BraingstormFragment fragment = new BraingstormFragment();
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);


    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_braingstorm, container, false);

        txtPoints = (TextView) view.findViewById(R.id.txtPoints);

        imgMemoryGame = (ImageView) view.findViewById(R.id.first_item);
        imgComic = (ImageView) view.findViewById(R.id.second_item);
        imgExercise = (ImageView) view.findViewById(R.id.third_item);

        SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(getActivity());
        String accountId = prefs.getString("accountId", "");

        firebaseDatabase = FirebaseDatabase.getInstance();

        //POINTS
        databaseReferenceUser = FirebaseDatabase.getInstance().getReference("users").child(accountId).child("points");

        getPoints();

        imgMemoryGame.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                addPoints();
                getPoints();
//                Intent browserIntent = new Intent(Intent.ACTION_VIEW, Uri.parse("https://play.google.com/store/apps/details?id=com.memory.brain.training.games"));
//                startActivity(browserIntent);
                startActivity(new Intent(getActivity(), MemoryGameActivity.class));
            }
        });

        imgComic.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                addPoints();
                getPoints();
                startActivity(new Intent(getActivity(), ComicsActivity.class));
            }
        });

        imgExercise.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(getActivity(), ExerciseActivity.class));
            }
        });

        return view;
    }

    public static void addPoints() {
        databaseReferenceUser.setValue(userPoints + 10);
    }

    public static void getPoints() {
        databaseReferenceUser.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                userPoints = snapshot.getValue(Integer.class);

                txtPoints.setText("" + userPoints + " Pts");
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                //Toast.makeText(getActivity(), "ERROR: " + databaseError.toString(), Toast.LENGTH_SHORT).show();
            }
        });
    }

}