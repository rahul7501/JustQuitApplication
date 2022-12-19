package edu.umich.dpm.sensorgrabber;

import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Build;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.NotificationCompat;
import androidx.core.app.NotificationManagerCompat;


import com.google.android.gms.auth.api.signin.GoogleSignIn;
import com.google.android.gms.auth.api.signin.GoogleSignInClient;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.android.material.navigation.NavigationBarView;

import edu.umich.dpm.sensorgrabber.fragments.AwardsFragment;
import edu.umich.dpm.sensorgrabber.fragments.BraingstormFragment;
import edu.umich.dpm.sensorgrabber.fragments.HealthFragment;
import edu.umich.dpm.sensorgrabber.fragments.Home_Fragment;
import edu.umich.dpm.sensorgrabber.fragments.TockonFragment;

public class DashboardActivity extends AppCompatActivity {

    public static DashboardActivity dashboardActContext;
    BottomNavigationView bottomNavigationView;

    BraingstormFragment braingstormFragment = new BraingstormFragment();
    HealthFragment healthFragment = new HealthFragment();
    Home_Fragment homeFragment = new Home_Fragment();
    TockonFragment tockonFragment = new TockonFragment();
    AwardsFragment awardsFragment = new AwardsFragment();

    String accountId = null;

    GoogleSignInClient mGoogleSignInClient;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_dashboard);

        dashboardActContext=DashboardActivity.this;
        if(Build.VERSION.SDK_INT>=Build.VERSION_CODES.O)
        {
            NotificationChannel channel = new NotificationChannel("hehe","hehe", NotificationManager.IMPORTANCE_DEFAULT);
            NotificationManager manager = getSystemService(NotificationManager.class);
            manager.createNotificationChannel(channel);
        }

        GoogleSignInOptions gso = new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
                .requestEmail()
                .build();

        mGoogleSignInClient = GoogleSignIn.getClient(DashboardActivity.this, gso);

        //Toast.makeText(this, "" + mGoogleSignInClient, Toast.LENGTH_SHORT).show();

        Bundle b = new Bundle();
        b.putString("accountId", accountId);
        homeFragment.setArguments(b);
        getSupportFragmentManager().beginTransaction().add(R.id.fragment_container, homeFragment).commit();

        bottomNavigationView = findViewById(R.id.navigation);

        getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container, homeFragment).commit();

        bottomNavigationView.setOnItemSelectedListener(new NavigationBarView.OnItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                switch (item.getItemId()) {
                    case R.id.nav_brainstrim_btn:
                        getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container, braingstormFragment).commit();
                        return true;
                    case R.id.nav_health_btn:
                        getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container, healthFragment).commit();
                        return true;
                    case R.id.nav_home_btn:
                        getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container, homeFragment).commit();
                        return true;
                    case R.id.nav_tocken_btn:
                        getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container, tockonFragment).commit();
                        return true;
                    case R.id.nav_badge_btn:
                        getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container, awardsFragment).commit();
                        return true;
                }
                return false;
            }
        });

    }


    public void logout() {

        mGoogleSignInClient.signOut()
                .addOnCompleteListener(this, new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {
                        Intent intent = new Intent(DashboardActivity.this, Intro1Activity.class);
                        startActivity(intent);

                        SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(DashboardActivity.this);
                        SharedPreferences.Editor editor = prefs.edit();
                        editor.remove("accountId");
                        editor.apply();

                        finish();
                    }
                });
    }

    public void btnLogout(View view) {
        logout();
    }

    public static void notifyMessage(){
        Log.d("Notification","Notification called");
        Intent intent = new Intent(dashboardActContext, MemoryGameActivity.class);
        PendingIntent pendingIntent = PendingIntent.getActivity(
                dashboardActContext,
                100,
                intent,
                PendingIntent.FLAG_CANCEL_CURRENT
        );
        NotificationCompat.Builder
                builder = new NotificationCompat.Builder(dashboardActContext, "hehe")
                .setSmallIcon(R.drawable.app_logo)
                .setContentTitle("Smoking Activity Detected")
                .setContentText("Click here to distract yourself!")
                .setContentIntent(pendingIntent)
                .setAutoCancel(true)
                .setPriority(NotificationCompat.PRIORITY_DEFAULT);

        NotificationManagerCompat notificationManage = NotificationManagerCompat.from(dashboardActContext);
        notificationManage.notify(100, builder.build());
        Log.d("Notification","Notification generated");
    }
}