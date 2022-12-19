package edu.umich.dpm.sensorgrabber;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.google.android.material.bottomnavigation.BottomNavigationView;

import edu.umich.dpm.sensorgrabber.fragments.BraingstormFragment;
import edu.umich.dpm.sensorgrabber.fragments.Home_Fragment;

public class ComicsActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_comics);
    }

    public void btnBack(View view) {
        startActivity(new Intent(ComicsActivity.this, DashboardActivity.class));
    }
}