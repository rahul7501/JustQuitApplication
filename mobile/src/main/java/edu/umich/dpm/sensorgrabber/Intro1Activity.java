package edu.umich.dpm.sensorgrabber;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.util.Log;
import android.view.View;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;
import androidx.viewpager.widget.ViewPager;

import com.google.android.gms.auth.api.signin.GoogleSignIn;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.android.gms.auth.api.signin.GoogleSignInClient;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.gms.common.api.ApiException;
import com.google.firebase.FirebaseApp;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;
import com.tbuonomo.viewpagerdotsindicator.SpringDotsIndicator;

import edu.umich.dpm.sensorgrabber.adapters.ViewPagerAdapter;


public class Intro1Activity extends AppCompatActivity {

    private static int RC_SIGN_IN = 100;
    GoogleSignInClient mGoogleSignInClient;

    DatabaseReference databaseReference;

    long maxId = 0;

    FragmentManager fragmentManager = getSupportFragmentManager();
    final FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();

    ViewPager mViewPager;

    int[] images = {R.drawable.a_first_intro, R.drawable.a_two_intro, R.drawable.a_three_intro, R.drawable.a_four_intro,
            R.drawable.a_five_intro};

    ViewPagerAdapter mViewPagerAdapter;

    SpringDotsIndicator dot1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_intro1);
        FirebaseApp.initializeApp(Intro1Activity.this);

        mViewPager = (ViewPager)findViewById(R.id.viewPagerIntro);

        dot1 = findViewById(R.id.spring_dots_indicator);

        mViewPagerAdapter = new ViewPagerAdapter(Intro1Activity.this, images);

        mViewPager.setAdapter(mViewPagerAdapter);
        dot1.setViewPager(mViewPager);

        databaseReference = FirebaseDatabase.getInstance().getReference().child("users");

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

        GoogleSignInOptions gso = new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
                .requestEmail()
                .build();

        mGoogleSignInClient = GoogleSignIn.getClient(this, gso);

        GoogleSignInAccount account = GoogleSignIn.getLastSignedInAccount(this);
        if (account != null) {
            SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(Intro1Activity.this);
            SharedPreferences.Editor editor = prefs.edit();

            editor.putString("accountId", account.getId());
            editor.apply();

            Intent intent = new Intent(Intro1Activity.this, MobileActivity.class);
            intent.putExtra("accountId", account.getId());
            startActivity(intent);
        }
    }

    public void btnContinueWithGoogle(View view) {
        signIn();
    }

    private void signIn() {
        Intent signInIntent = mGoogleSignInClient.getSignInIntent();
        startActivityForResult(signInIntent, RC_SIGN_IN);
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == RC_SIGN_IN) {
            com.google.android.gms.tasks.Task<GoogleSignInAccount> task = GoogleSignIn.getSignedInAccountFromIntent(data);
            handleSignInResult(task);
        }

    }

    private void handleSignInResult(com.google.android.gms.tasks.Task<GoogleSignInAccount> completedTask) {
        try {
            GoogleSignInAccount account = completedTask.getResult(ApiException.class);

            if (account != null) {

                final Query checkUser = FirebaseDatabase.getInstance().getReference("users").orderByChild("email").equalTo(account.getEmail());

                checkUser.addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                        if (!dataSnapshot.exists()) {
                            createUser(account.getId(), account.getEmail());
                        }
                    }
                    @Override
                    public void onCancelled(@NonNull DatabaseError databaseError) {
                    }
                });

                SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(Intro1Activity.this);
                SharedPreferences.Editor editor = prefs.edit();

                editor.putString("accountId", account.getId());
                editor.apply();

                Intent intent = new Intent(Intro1Activity.this, MobileActivity.class);
                intent.putExtra("accountId", account.getId());
                startActivity(intent);
            }
        } catch (ApiException e) {
            Log.w("signInResult_failed", "signInResult:failed code=" + e.getStatusCode());
        }
    }

    private void createUser(String id, String email) {
        UserHelperClass addNewUser = new UserHelperClass(email, 0);
        databaseReference.child(id).setValue(addNewUser);
    }

}