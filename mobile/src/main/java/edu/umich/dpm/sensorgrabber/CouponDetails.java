package edu.umich.dpm.sensorgrabber;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.squareup.picasso.Picasso;

public class CouponDetails extends AppCompatActivity {

    ImageView imgVoucher, imgBack;

    TextView txtHeading, txtPointOne;

    Button btnGetCode;

    FirebaseDatabase firebaseDatabase;
    DatabaseReference databaseReferenceUser;

    int userPoints = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_coupon_details);

        imgVoucher = (ImageView) findViewById(R.id.imgVoucher);
        imgBack = (ImageView) findViewById(R.id.imgBack);

        txtHeading = (TextView) findViewById(R.id.txtHeading);
        txtPointOne = (TextView) findViewById(R.id.txtPointOne);

        btnGetCode = (Button) findViewById(R.id.btnGetCode);

        SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(CouponDetails.this);
        String accountId = prefs.getString("accountId", "");

        //Toast.makeText(getActivity(), "" + accountId, Toast.LENGTH_SHORT).show();

        firebaseDatabase = FirebaseDatabase.getInstance();




        //POINTS
        databaseReferenceUser = FirebaseDatabase.getInstance().getReference("users").child(accountId).child("points");

        databaseReferenceUser.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                userPoints = snapshot.getValue(Integer.class);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                Toast.makeText(CouponDetails.this, "ERROR: " + databaseError.toString(), Toast.LENGTH_SHORT).show();
            }
        });

        Intent intent = getIntent();

        String imgUrl = intent.getStringExtra("imgUrl");
        String points = intent.getStringExtra("points");
        String aed = intent.getStringExtra("aed");
        String brandName = intent.getStringExtra("brandName");
        int userPoints = intent.getIntExtra("userPoints", 0);

        Picasso.with(CouponDetails.this)
                .load(imgUrl)
                .into(imgVoucher);

        txtHeading.setText("Enjoy an " + aed + " AED voucher on " + brandName);
        txtPointOne.setText("1. This voucher is worth " + points + " points.");

        btnGetCode.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (userPoints >= Integer.parseInt(points)) {
                    CouponCodeGenerator object = new CouponCodeGenerator();
                    String code = object.getAlphaNumericString(6);
                    btnGetCode.setBackgroundTintList(getColorStateList(R.color.colorGreen));
                    btnGetCode.setText("" + code);
                    btnGetCode.setEnabled(false);

                    databaseReferenceUser.setValue(userPoints - Integer.parseInt(points));
                } else {
                    btnGetCode.setBackgroundTintList(getColorStateList(R.color.colorRed));
                    btnGetCode.setText("NOT ENOUGH POINTS");
                    btnGetCode.setEnabled(false);
                }
            }

        });

    }

    public void btnBack(View view) {
        startActivity(new Intent(CouponDetails.this, DashboardActivity.class));
    }

}