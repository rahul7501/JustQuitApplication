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
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;


import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;

import edu.umich.dpm.sensorgrabber.R;
import edu.umich.dpm.sensorgrabber.adapters.VoucherAdapter;
import edu.umich.dpm.sensorgrabber.models.FetchVouchers;

public class TockonFragment extends Fragment {

    List<FetchVouchers> fetchData;
    RecyclerView recyclerView;
    VoucherAdapter helperAdapter;
    FirebaseDatabase firebaseDatabase;
    DatabaseReference databaseReferenceVouchers, databaseReferenceUser;

    TextView txtPoints;

    public static int userPoints = 0;

    public TockonFragment() {
    }

    public static TockonFragment newInstance(String param1, String param2) {
        TockonFragment fragment = new TockonFragment();
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        //FirebaseApp.initializeApp(getActivity());

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_tockon, container, false);

        txtPoints = (TextView) view.findViewById(R.id.txtPoints);

        SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(getActivity());
        String accountId = prefs.getString("accountId", "");

        //Toast.makeText(getActivity(), "" + accountId, Toast.LENGTH_SHORT).show();

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







        //VOUCHERS
        databaseReferenceVouchers = FirebaseDatabase.getInstance().getReference("vouchers");

        recyclerView = view.findViewById(R.id.recyclerView);
        recyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));
        fetchData = new ArrayList<>();

        helperAdapter = new VoucherAdapter(getActivity(), fetchData);
        recyclerView.setAdapter(helperAdapter);

        databaseReferenceVouchers.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {

                fetchData.clear();

                for (DataSnapshot dataSnapshot : snapshot.getChildren()) {
                    FetchVouchers fetchVouchers  = dataSnapshot.getValue(FetchVouchers.class);
                    fetchData.add(fetchVouchers);
                }
                helperAdapter.notifyDataSetChanged();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                Toast.makeText(getActivity(), "ERROR: " + databaseError.toString(), Toast.LENGTH_SHORT).show();
            }
        });

        return view;
    }
}