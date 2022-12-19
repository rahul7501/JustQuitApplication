package edu.umich.dpm.sensorgrabber.fragments;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.preference.PreferenceManager;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;


import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.Timer;
import java.util.TimerTask;
import java.util.concurrent.TimeUnit;

import edu.umich.dpm.sensorgrabber.R;
import edu.umich.dpm.sensorgrabber.SetTargetsActivity;

public class Home_Fragment extends Fragment {

    FirebaseDatabase firebaseDatabase;
    DatabaseReference databaseReferenceUserTarget, databaseReferenceCigaretteTrack, databaseReferenceUser,databaseReferenceMoneyTrack;
    static DatabaseReference databaseReferenceLastCigaretteTime;

    static String accountId = null;

    ImageView health_edit_iv, add_iv;

    String avgCigarettes, noOfDays, setDate, limitPerDay, noOfCigarettesInPack, pricePerPack;
    int randomIndex;

    LinearLayout linear_add_target, linear_show_progress;

    TextView txtDay, txtToGo, txtCigarettesNotSmoked, txtMoneySaved, txtMoneySavedInDays;

    ProgressBar progressBar;

    SimpleDateFormat sdf = new SimpleDateFormat("dd-MM-yyyy");

    ImageView smileyface;


    int smokedCounter = 0, smokeVerificationTimeCounter = 0, cigarettesNotSmoked = 0, totalSaved = 0, userPoints = 0;
    double moneySavedTillNow=0.0;
    static int todayCigaretteCount = 0;
    int SmokedCounterforDistraction=0;

    boolean isSmokeDetectionOn = true;

    String randomIndexValue = null;
    static String todayDate = null;

    Calendar calendar = Calendar.getInstance();
    //String mTeam;
    SharedPreferences prefs;

    public Home_Fragment() {
    }

    public static Home_Fragment newInstance(String param1, String param2) {
        Home_Fragment fragment = new Home_Fragment();
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);


    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_home, container, false);

        prefs = PreferenceManager.getDefaultSharedPreferences(getActivity());

        accountId = prefs.getString("accountId", "");
        Log.d("Home_fragment_shared","Shared Account ID is: "+ String.valueOf(accountId));
        todayDate = "" + sdf.format(calendar.getTime());


//        Bundle bundle = this.getArguments();
//        assert bundle != null;
//        String mTeam = bundle.getString("SmokingValue");
//        String mTeam = getArguments().getString("SmokingValue");
//         Log.d("MTeam",mTeam);


//         Bundle data = getArguments();
//         if(data!=null)
//         {
//             mTeam= data.getString("SmokingValue");
//             Log.d("smokindex",mTeam);
//         }

//                = new ArrayList<String>();
//        smoked.add("no");
//        smoked.add("no");
//        smoked.add("no");
//        smoked.add("yes");
//        smoked.add("no");
//        smoked.add("no");
//        smoked.add("no");
//        smoked.add("no");
//        smoked.add("no");
//        smoked.add("no");
//        smoked.add("yes");
//        smoked.add("no");
//        smoked.add("no");
//        smoked.add("no");

        //timerDelay(mTeam);



        health_edit_iv = (ImageView) view.findViewById(R.id.health_edit_iv);
        add_iv = (ImageView) view.findViewById(R.id.add_iv);
        smileyface = (ImageView) view.findViewById(R.id.ic_face_iv);

        linear_add_target = (LinearLayout) view.findViewById(R.id.linear_add_target);
        linear_show_progress = (LinearLayout) view.findViewById(R.id.linear_show_progress);
        linear_add_target.setVisibility(View.GONE);
        linear_show_progress.setVisibility(View.GONE);

        txtDay = (TextView) view.findViewById(R.id.txtDay);
        txtToGo = (TextView) view.findViewById(R.id.txtToGo);
        txtCigarettesNotSmoked = (TextView) view.findViewById(R.id.txtCigarettesNotSmoked);
        txtMoneySaved = (TextView) view.findViewById(R.id.txtMoneySaved);
        txtMoneySavedInDays = (TextView) view.findViewById(R.id.txtMoneySavedInDays);

        progressBar = (ProgressBar) view.findViewById(R.id.TargetprogressBar);

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
                Toast.makeText(getActivity(), "ERROR: " + databaseError.toString(), Toast.LENGTH_SHORT).show();
            }
        });

        databaseReferenceUserTarget = FirebaseDatabase.getInstance().getReference("users").child(accountId).child("target");

        databaseReferenceCigaretteTrack = FirebaseDatabase.getInstance().getReference("users").child(accountId).child("cigaretteCount");
        databaseReferenceMoneyTrack = FirebaseDatabase.getInstance().getReference("users").child(accountId).child("Money");
        databaseReferenceUserTarget.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                avgCigarettes = snapshot.child("avgCigarettes").getValue(String.class);
                noOfDays = snapshot.child("noOfDays").getValue(String.class);
                setDate = snapshot.child("setDate").getValue(String.class);
                limitPerDay = snapshot.child("limitPerDay").getValue(String.class);
                noOfCigarettesInPack = snapshot.child("noOfCigarettesInPack").getValue(String.class);
                pricePerPack = snapshot.child("pricePerPack").getValue(String.class);

                if(avgCigarettes == null) {
                    linear_add_target.setVisibility(View.VISIBLE);
                    linear_show_progress.setVisibility(View.GONE);
                    return;
                } else {
                    linear_add_target.setVisibility(View.GONE);
                    linear_show_progress.setVisibility(View.VISIBLE);
                }

                String inputString1 = setDate;
                String inputString2 = sdf.format(new Date());

                try {
                    Date date1 = sdf.parse(inputString1);
                    Date date2 = sdf.parse(inputString2);
                    long diff = date2.getTime() - date1.getTime();
                    txtDay.setText("Day " + TimeUnit.DAYS.convert(diff, TimeUnit.MILLISECONDS) + "/" + noOfDays);

                    txtMoneySavedInDays.setText("In " + TimeUnit.DAYS.convert(diff, TimeUnit.MILLISECONDS) + " day(s)");

                    double noOfDaysOver = (int) TimeUnit.DAYS.convert(diff, TimeUnit.MILLISECONDS);

                    double percentageProgress = ( noOfDaysOver / Double.parseDouble(noOfDays) ) * 100;

                    int daysToGo = (int) (Integer.parseInt(noOfDays) - noOfDaysOver);

                    if (daysToGo < 0) {
                        databaseReferenceUserTarget.removeValue();
                        databaseReferenceCigaretteTrack.removeValue();
                        databaseReferenceUser.setValue(userPoints + 50);
                        databaseReferenceMoneyTrack.removeValue();
                        txtCigarettesNotSmoked.setText("0 Cigarettes");
                        txtMoneySaved.setText("AED 0");
                        txtMoneySavedInDays.setText("In 0 day(s)");
                    }

                    txtToGo.setText("" + daysToGo + " day(s) to go");

                    if(percentageProgress>50)
                    {
                        smileyface.setImageResource(R.drawable.smiley_face);
                    }

                    progressBar.setProgress((int) percentageProgress);

                } catch (ParseException e) {
                    e.printStackTrace();
                }

            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                Toast.makeText(getActivity(), "ERROR: " + databaseError.toString(), Toast.LENGTH_SHORT).show();
            }
        });

        databaseReferenceCigaretteTrack.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {

                for (DataSnapshot dataSnapshot : snapshot.getChildren()) {
                    totalSaved = totalSaved +  dataSnapshot.getValue(Integer.class);
                }

                //Toast.makeText(getActivity(), "" + totalSaved, Toast.LENGTH_SHORT).show();

                if (snapshot.child(todayDate).getValue() != null) {

                    if (snapshot.child(todayDate).getValue(Integer.class) != null) {
                        todayCigaretteCount = snapshot.child(todayDate).getValue(Integer.class);
                    }

                    final Timer timerMain = new Timer();

                    TimerTask timerTaskMain = new TimerTask() {
                        @Override
                        public void run() {

                            if (getActivity() == null)
                                return;

                            getActivity().runOnUiThread(new Runnable() {

                                @Override
                                public void run() {

                                    if (todayCigaretteCount > Integer.parseInt(limitPerDay)) {
                                        databaseReferenceUserTarget.removeValue();
                                        databaseReferenceCigaretteTrack.removeValue();
                                        databaseReferenceMoneyTrack.removeValue();
                                        txtCigarettesNotSmoked.setText("... Cigarettes");
                                        txtMoneySaved.setText("AED ...");
                                        txtMoneySavedInDays.setText("In ... day(s)");
                                    }

                                    if (limitPerDay != null) {
                                        cigarettesNotSmoked = Integer.parseInt(limitPerDay) - todayCigaretteCount;
                                        txtCigarettesNotSmoked.setText("" + cigarettesNotSmoked + " cigarette(s)");

                                        double priceOfOneCigarette = Double.parseDouble(pricePerPack) / Double.parseDouble(noOfCigarettesInPack);

                                        databaseReferenceMoneyTrack.child("moneySavedTillNow").setValue(priceOfOneCigarette * cigarettesNotSmoked );
                                        databaseReferenceMoneyTrack.addValueEventListener(new ValueEventListener() {
                                            @Override
                                            public void onDataChange(@NonNull DataSnapshot snapshot) {
                                                moneySavedTillNow = snapshot.child("moneySavedTillNow").getValue(Double.class);
                                            }

                                            @Override
                                            public void onCancelled(@NonNull DatabaseError error) {

                                            }
                                        });

                                        if(txtMoneySavedInDays.getText().toString().equals("In 0 day(s)")) {
                                            txtMoneySaved.setText("AED " + priceOfOneCigarette * cigarettesNotSmoked);
                                            Log.d("Days","0 Days Over");
                                        }
                                        if(!txtMoneySavedInDays.getText().toString().equals("In 0 day(s)")) {
                                            txtMoneySaved.setText("AED " + ((priceOfOneCigarette * cigarettesNotSmoked) + moneySavedTillNow));
                                            Log.d("Days","More Days Over");
                                        }

//                                        txtMoneySaved.setText("AED " + priceOfOneCigarette * totalSaved);

//                                        if (todayCigaretteCount > Integer.parseInt(limitPerDay)) {
//                                            databaseReferenceUserTarget.removeValue();
//                                            databaseReferenceCigaretteTrack.removeValue();
//                                            databaseReferenceMoneyTrack.removeValue();
//                                            txtCigarettesNotSmoked.setText("... Cigarettes");
//                                            txtMoneySaved.setText("AED ...");
//                                            txtMoneySavedInDays.setText("In ... day(s)");
//                                        }
                                    }
                                }
                            });

                        }

                    };

                    timerMain.schedule(timerTaskMain, 1000, 1000);

                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });

        health_edit_iv.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                setTarget();
            }
        });

        add_iv.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                setTarget();
            }
        });

        return view;
    }

    void setTarget() {
        Intent intent = new Intent(getActivity(), SetTargetsActivity.class);
        //intent.putExtra("accountId", accountId);
        startActivity(intent);
    }


    public void printMessage(){
        Log.d("Home_fragment","Last cigarette time added to database");
        Log.d("Home_fragment","The account ID is: "+ String.valueOf(accountId));
//        Log.d("Home_fragment","The account ID with passed parameter is: "+ String.valueOf(id));
//        Log.d("Home_fragment",String.valueOf(System.currentTimeMillis()));
        databaseReferenceCigaretteTrack = FirebaseDatabase.getInstance().getReference("users").child(accountId).child("cigaretteCount");
        databaseReferenceCigaretteTrack.child(todayDate).setValue(todayCigaretteCount + 1);
        databaseReferenceLastCigaretteTime = FirebaseDatabase.getInstance().getReference("users").child(accountId).child("lastCigaretteTime");
        databaseReferenceLastCigaretteTime.setValue(System.currentTimeMillis());

    }
}
