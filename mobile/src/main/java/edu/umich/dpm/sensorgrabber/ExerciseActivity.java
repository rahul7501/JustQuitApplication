package edu.umich.dpm.sensorgrabber;

import androidx.appcompat.app.AppCompatActivity;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.res.ColorStateList;
import android.graphics.Color;
import android.media.Ringtone;
import android.media.RingtoneManager;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.text.InputType;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;



import org.w3c.dom.Text;

import java.util.Date;
import java.util.Locale;

import edu.umich.dpm.sensorgrabber.fragments.BraingstormFragment;

public class ExerciseActivity extends AppCompatActivity {

    TextView txtCountdownTimer;

    Button btnStart;

    private long mStartTimeInMillis;
    private long mTimeLeftInMillis;
    private long newTime;

    private CountDownTimer mCountDownTimer;

    private boolean mTimerRunning;

    Ringtone ringAlarm;
    Uri alarm = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_ALARM);

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_exercise);

        txtCountdownTimer = (TextView) findViewById(R.id.txtCountdownTimer);

        btnStart = (Button) findViewById(R.id.btnStart);

        ringAlarm = RingtoneManager.getRingtone(ExerciseActivity.this, alarm);

        btnStart.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String btnText = btnStart.getText().toString();

                if (btnText.equals("START")) {
                    setTime(10000);
                    btnStart.setText("STOP");
                    btnStart.setBackgroundTintList(getColorStateList(R.color.colorRed));
                } else {
                    mCountDownTimer.cancel();
                    ringAlarm.stop();
                    resetTimer();
                    btnStart.setText("START");
                    btnStart.setBackgroundTintList(getColorStateList(R.color.colorGreen));
                }
            }
        });

    }

    private void setTime(long minutesTime) {
        mStartTimeInMillis = minutesTime;
        resetTimer();

        startTimer();
    }

    private void startTimer() {

        mCountDownTimer = new CountDownTimer(mTimeLeftInMillis, 1000) {
            @Override
            public void onTick(long millisUntilFinished) {
                mTimeLeftInMillis = millisUntilFinished;
                updateCountDownText();
            }

            @Override
            public void onFinish() {
                mTimerRunning = false;
                ringAlarm.play();
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.P) {
                    if (ringAlarm.isLooping()) {
                        ringAlarm.stop();
                    }
                }
                alertCompleted();
            }
        }.start();

        mTimerRunning = true;
    }

    private void alertCompleted() {
        AlertDialog.Builder builder = new AlertDialog.Builder(ExerciseActivity.this);
        builder.setTitle("Congratulations!");
        builder.setMessage("You are one step closer to Quit Smoking");

        builder.setPositiveButton("Exit", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                BraingstormFragment.addPoints();
                BraingstormFragment.getPoints();
                mCountDownTimer.cancel();
                ringAlarm.stop();
                startActivity(new Intent(ExerciseActivity.this, DashboardActivity.class));
            }
        });

        builder.show();
    }

    private void resetTimer() {
        mTimeLeftInMillis = mStartTimeInMillis;
        updateCountDownText();
    }

    private void updateCountDownText() {
        int hours = (int) (mTimeLeftInMillis / 1000) / 3600;
        int minutes = (int) ((mTimeLeftInMillis / 1000) % 3600) / 60;
        int seconds = (int) (mTimeLeftInMillis / 1000) % 60;

        String timeLeftFormatted;

        if (hours > 0) {
            timeLeftFormatted = String.format(Locale.getDefault(),
                    "%d:%02d:%02d", hours, minutes, seconds);
        } else {
            timeLeftFormatted = String.format(Locale.getDefault(),
                    "%02d:%02d", minutes, seconds);
        }

        txtCountdownTimer.setText(timeLeftFormatted);
    }

    public void btnBack(View view) {
       startActivity(new Intent(ExerciseActivity.this, DashboardActivity.class));
       //Toast.makeText(this, "make functional", Toast.LENGTH_SHORT).show();
    }
}