package edu.umich.dpm.sensorgrabber.fragments;

import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;


import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import edu.umich.dpm.sensorgrabber.R;

public class AwardsFragment extends Fragment {

    FirebaseDatabase firebaseDatabase;
    DatabaseReference databaseReferenceUser;

    TextView txtPoints;

    int userPoints = 0;

    public AwardsFragment() {
    }

    public static AwardsFragment newInstance(String param1, String param2) {
        AwardsFragment fragment = new AwardsFragment();
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_awards, container, false);

        txtPoints = (TextView) view.findViewById(R.id.txtPoints);

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

        return view;
    }
}