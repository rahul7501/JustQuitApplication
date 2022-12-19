package edu.umich.dpm.sensorgrabber;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.text.SimpleDateFormat;
import java.util.Calendar;

public class SetTargetsActivity extends AppCompatActivity {

    EditText editAvgCigarettes, editNoOfCigarettesInPack, editPricePerPack, editLimitPerDay, editNoOfDays, editLimitPerMonth, editNoOfMonths;

    TextView btnCancel, btnSetTarget;

    DatabaseReference databaseReference;

    String accountId = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_set_targets);

        Intent intent = getIntent();

        SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(SetTargetsActivity.this);
        accountId = prefs.getString("accountId", "");

        //Toast.makeText(this, "" + accountId, Toast.LENGTH_SHORT).show();

        databaseReference = FirebaseDatabase.getInstance().getReference().child("users").child(accountId);

        databaseReference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                if (dataSnapshot.exists()) {
                    //maxId = (dataSnapshot.getChildrenCount());
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });

        editAvgCigarettes = (EditText) findViewById(R.id.editAvgCigarettes);
        editNoOfCigarettesInPack = (EditText) findViewById(R.id.editNoOfCigarettesInPack);
        editPricePerPack = (EditText) findViewById(R.id.editPricePerPack);
        editLimitPerDay = (EditText) findViewById(R.id.editLimitPerDay);
        editNoOfDays = (EditText) findViewById(R.id.editNoOfDays);
        editLimitPerMonth = (EditText) findViewById(R.id.editLimitPerMonth);
        editNoOfMonths = (EditText) findViewById(R.id.editNoOfMonths);

        btnCancel = (TextView) findViewById(R.id.btnCancel);
        btnSetTarget = (TextView) findViewById(R.id.btnSetTarget);

        btnSetTarget.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                String avgCigarettes = editAvgCigarettes.getText().toString();
                String noOfCigarettesInPack = editNoOfCigarettesInPack.getText().toString();
                String pricePerPack = editPricePerPack.getText().toString();

                String limitPerDay = editLimitPerDay.getText().toString();
                String noOfDays = editNoOfDays.getText().toString();

                String limitPerMonth = editLimitPerMonth.getText().toString();
                String noOfMonths = editNoOfMonths.getText().toString();

//                if (avgCigarettes.equals("")) {
//                    Toast.makeText(SetTargetsActivity.this, "All fields required!", Toast.LENGTH_SHORT).show();
//                    return;
//                }
//
//                if (noOfCigarettesInPack.equals("")) {
//                    Toast.makeText(SetTargetsActivity.this, "All fields required!", Toast.LENGTH_SHORT).show();
//                    return;
//                }
//
//                if (pricePerPack.equals("")) {
//                    Toast.makeText(SetTargetsActivity.this, "All fields required!", Toast.LENGTH_SHORT).show();
//                    return;
//                }
//
//                if (limitPerDay.equals("")) {
//                    Toast.makeText(SetTargetsActivity.this, "All fields required!", Toast.LENGTH_SHORT).show();
//                    return;
//                }
//
//                if (noOfDays.equals("")) {
//                    Toast.makeText(SetTargetsActivity.this, "All fields required!", Toast.LENGTH_SHORT).show();
//                    return;
//                }

                Calendar calendar = Calendar.getInstance();
                SimpleDateFormat sdf = new SimpleDateFormat("dd-MM-yyyy");
                String setDate = "" + sdf.format(calendar.getTime());

                TargetHelperClass addTarget = new TargetHelperClass(avgCigarettes, noOfCigarettesInPack, pricePerPack, limitPerDay, noOfDays, setDate);
                databaseReference.child("target").setValue(addTarget);
                databaseReference.child("cigaretteCount").removeValue();

//                finish();

                startActivity(new Intent(SetTargetsActivity.this, DashboardActivity.class));

            }
        });


    }
    public void btnBack(View view) {
        startActivity(new Intent(SetTargetsActivity.this, DashboardActivity.class));
    }
}