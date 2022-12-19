package edu.umich.dpm.sensorgrabber.data;


import static java.lang.Math.round;


import android.content.Context;
import android.content.SharedPreferences;
import android.content.res.AssetFileDescriptor;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.preference.PreferenceManager;
import android.util.Log;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.NotificationCompat;
import androidx.fragment.app.FragmentTransaction;

import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import org.tensorflow.lite.Interpreter;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileWriter;
import java.io.IOException;
import java.nio.MappedByteBuffer;
import java.nio.channels.FileChannel;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashSet;
import java.util.LinkedHashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Queue;

import edu.umich.dpm.sensorgrabber.ActivityClassifier;
import edu.umich.dpm.sensorgrabber.DashboardActivity;
import edu.umich.dpm.sensorgrabber.Data;
import edu.umich.dpm.sensorgrabber.MobileActivity;
import edu.umich.dpm.sensorgrabber.R;
import edu.umich.dpm.sensorgrabber.fragments.Home_Fragment;
import edu.umich.dpm.sensorgrabber.sensor.SensorQueue;
import edu.umich.dpm.sensorgrabber.sensor.SensorReading;

public class FileHandler {
    private static final String TAG = "FileHandler";

    private static final String SUB_DIRECTORY = "SensorGrabber";
    private static final String EXTENSION = "csv";

//    private static final double minAX= -30.767884999999996;
//    private static final double maxAX= 23.997084;
//    private static final double minAY= -43.030987;
//    private static final double maxAY= 15.298949;
//    private static final double minAZ= -23.130383;
//    private static final double maxAZ= 46.33498400000001;


//    private static final double minGX= -12.767083;
//    private static final double maxGX= 15.234979999999998;
//    private static final double minGY= -5.004208;
//    private static final double maxGY= 5.5356607;
//    private static final double minGZ= -7.325496;
//    private static final double maxGZ= 7.7433276;

    private static final int windowSize=400;
    private static int step=200;

//    private boolean enteredAccel=false;
//    private boolean enteredGyro=false;

    //private LinkedHashMap<Long,Data> hashMap = new LinkedHashMap<Long,Data>();

    private LinkedHashMap<Long,Data> hashMap = new LinkedHashMap<Long,Data>();
    LinkedList<Long> timeSet = new LinkedList<>();

    String channelId = "CHANNEL_1";



    private boolean visitedAccelFirst=false;
    private boolean visitedGyroFirst=false;
    private boolean visitedAccelSecond=false;
    private boolean visitedGyroSecond=false;

    //ML
    private List<Float> ax= new ArrayList<>();
    private List<Float> ay= new ArrayList<>();
    private List<Float> az= new ArrayList<>();
    private List<Float> gy= new ArrayList<>();
    private List<String> timesList= new ArrayList<>();
    private float[] results;

    private static final String STORAGE_DIRECTORY =
            (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT ?
                    Environment.DIRECTORY_DOCUMENTS : Environment.DIRECTORY_DCIM);

    private static final SimpleDateFormat DATE_FORMAT = new SimpleDateFormat("h:mm:ss.SSS a");

    private final BufferedWriter mWriter;

    private final Date mStartTime;
    private long mFirstSensorTimestamp = -1;

    private int timer30=0;
    private int timer60=0;
    private int smokeCounterForDistraction30=0;
    private int smokeCounterForDistraction60=0;



    public FileHandler(String filename, Date startTime) throws IOException {
        String state = Environment.getExternalStorageState();
        if (!state.equals(Environment.MEDIA_MOUNTED)) {
            throw new IOException("External storage is not mounted");
        }

        File root = new File(Environment.getExternalStoragePublicDirectory(STORAGE_DIRECTORY), SUB_DIRECTORY);
        if (!root.exists()) {
            if (!root.mkdirs()) {
                throw new FileNotFoundException("Unable to create directory \"" + SUB_DIRECTORY + "\" in \"" + STORAGE_DIRECTORY + "\"");
            }
        }

        File file = new File(root, filename + "." + EXTENSION);
        mWriter = new BufferedWriter(new FileWriter(file, true));
        mStartTime = startTime;
    }

    public void writeQueue(SensorQueue queue, int sensorType) throws IOException {
        SensorReading[] readings = queue.getReadings();
        Log.d("WriteQueue","In Write Queue "+ sensorType);

        if (mFirstSensorTimestamp < 0 && readings.length > 0) {
            mFirstSensorTimestamp = readings[0].getTimestamp();
        }



        // loop to iterate through all the values

        for (SensorReading r : readings) {
            writeLine(r,sensorType);

        }
//        if(visitedAccel && visitedGyro && hashMap.size()>windowSize)
//        {
//            Log.d("GyroAccel","Data filled for both accel and gyro and size of hashmap is multiples of 400");
//
//            visitedAccel=visitedGyro=false;
//
//        }

        Log.d("hashMapSize: ", String.valueOf(hashMap.size()));
//        Log.d("KeySet: ",hashMap.keySet().toString());
    }

    // this runs 128 times before going to writeQueue function

    private void writeLine(SensorReading reading, int sensorType) throws IOException {
        //Log.d("WriteLineStarted: ", "Write Line Started");
        long timeFromStart = (reading.getTimestamp() - mFirstSensorTimestamp) / 1000000;


        //10 20 30
        // mWriter.write(String.valueOf(timeFromStart));
        // mWriter.write(44);//shifting right to cells
        //String time = DATE_FORMAT.format(mStartTime.getTime() + timeFromStart);
        SimpleDateFormat sdf = new SimpleDateFormat("'Date\n'dd-MM-yyyy '\n\nand\n\nTime\n'HH:mm:ss z");
        String time = sdf.format(new Date());
        //mWriter.write(DATE_FORMAT.format(mStartTime.getTime() + timeFromStart));

        if((hashMap.containsKey(timeFromStart)) && (sensorType==4))         //Accelerometer data is already fed gyroscope remaining
        {
            //Log.d("Insert","----------Accelerometer data is already fed gyroscope remaining");
            visitedAccelSecond=true;
            Data toInsert = hashMap.get(timeFromStart);
            timeSet.add(timeFromStart);
            int counter=0;
            for (float value : reading.getValues()) {
                if(counter==0)
                {
//                    value -= minGX;
//                    value /= (minGX - maxGX);
                    toInsert.setGX(value);
                }

                if(counter==1)
                {
//                    value -= minGY;
//                    value /= (minGY - maxGY);
                    toInsert.setGY(value);
                }

                if(counter==2)
                {
//                    value -= minGZ;
//                    value /= (minGZ - maxGZ);
                    toInsert.setGZ(value);
                }

                counter++;
            }
            //Log.d("InsertSensor", "Time: "+ timeFromStart+ hashMap.get(timeFromStart));
            return;
            // Log.d("InsertSensor", "Time: "+ timeFromStart+ hashMap.get(timeFromStart+""));
        }


        if((hashMap.containsKey(timeFromStart))  && (sensorType==1))         //Gyrosccpe data is fed. Accelerometer remaining
        {
            visitedAccelFirst=true;
            //Log.d("Insert","----------Gyrosccpe data is fed. Accelerometer remaining");
            Data toInsert = hashMap.get(timeFromStart);
            timeSet.add(timeFromStart);
            int counter=0;
            for (float value : reading.getValues()) {
                if(counter==0)
                {
//                    value-=minAX;
//                    value/= (maxAX-minAX);
                    toInsert.setAX(value);
                }

                if(counter==1)
                {
//                    value-=minAY;
//                    value/= (maxAY-minAY);
                    toInsert.setAY(value);
                }

                if(counter==2)
                {
//                    value -= minAZ;
//                    value /= (maxAZ - minAZ);
                    toInsert.setAZ(value);
                }

                counter++;
            }
            //Log.d("InsertSensor", "Time: "+ timeFromStart+ hashMap.get(timeFromStart));
            return;

        }

        if((!hashMap.containsKey(timeFromStart)) && (sensorType==1))    //No records added for accelerometer or gyroscope
        {
//            Log.d("ContainsKey:",String.valueOf(hashMap.containsKey(dateClass)));
            //Log.d("Insert","---------No records added for accelerometer or gyroscope. Acceloermeter values would get fed");
            //First Acceloermeter values would get fed
            // Removing Outliers is Left
            visitedGyroSecond=true;
            Data d= new Data();
            d.setTime(time);
            int counter=0; // tells me which axis value is being added
            for (float value : reading.getValues()) {
                if(counter==0)
                {
//                    value-=minAX;
//                    value/= (maxAX-minAX);
                    d.setAX(value);
                }

                if(counter==1)
                {
//                    value-=minAY;
//                    value/= (maxAY-minAY);
                    d.setAY(value);
                }

                if(counter==2) {
//                    value -= minAZ;
//                    value /= (maxAZ - minAZ);
                    d.setAZ(value);
                }
                counter++;

            }
            hashMap.put(timeFromStart, d);

            //Log.d("InsertSensor", "Time: " + timeFromStart + hashMap.get(timeFromStart));
            return;
        }


        if((!hashMap.containsKey(timeFromStart) && (sensorType==4)))    //No records added for accelerometer or gyroscope
        {
//            Log.d("ContainsKey:",String.valueOf(hashMap.containsKey(dateClass)));
            //Log.d("Insert","----------No records added for accelerometer or gyroscope. Gyroscope values would get fed ");
            //First Gyroscope values would get fed
            visitedGyroFirst=true;
            Data d= new Data();
            d.setTime(time);
            int counter=0; // tells me which axis value is being added
            for (float value : reading.getValues()) {
                if(counter==0)
                {
//                    value -= minGX;
//                    value /= (minGX - maxGX);
                    d.setGX(value);
                }

                if(counter==1)
                {
//                    value -= minGY;
//                    value /= (minGY - maxGY);
                    d.setGY(value);
                }

                if(counter==2)
                {
//                    value -= minGZ;
//                    value /= (minGZ - maxGZ);
                    d.setGZ(value);
                }

                counter++;
            }
            hashMap.put(timeFromStart,d);

            //Log.d("InsertSensor", "Time: "+ timeFromStart+ hashMap.get(timeFromStart));
//            Log.d("ConstainsKeyToday: ", String.valueOf(hashMap.containsKey(dateClass)));

        }

        if(((visitedAccelFirst && visitedGyroFirst) || (visitedGyroSecond && visitedAccelSecond)) && (hashMap.size()>=1000))
        {
            Log.d("GyroAccel","Data filled for both accel and gyro and size of hashmap is more than 400");
            //send for prediction
            String currentTime = hashMap.get(timeSet.get(0)).getTime();
            for(int i=0;i<400;i++){
                ax.add(hashMap.get(timeSet.get(i)).getAX());
                ay.add(hashMap.get(timeSet.get(i)).getAY());
                az.add(hashMap.get(timeSet.get(i)).getAZ());
                gy.add(hashMap.get(timeSet.get(i)).getGY());
            }
            predict(currentTime);

            //remove first 200 tuples
//            Log.d("TimeSetSize",String.valueOf(timeSet.size()));
//            Log.d("TimeSet: ",String.valueOf(timeSet));
            for(int i=0;i<200;i++)
            {
                long timeAti = timeSet.get(0);
                hashMap.remove(timeAti);
                timeSet.remove(0);

            }

//            Log.d("TimeSet: ",String.valueOf(timeSet));
//            Log.d("TimeSetSize",String.valueOf(timeSet.size()));
            //print the hashmap and check
//            for( Map.Entry<Long, Data> entry : hashMap.entrySet() ){
//                System.out.println( entry.getKey() + " = " + entry.getValue() );
//            }
            visitedGyroSecond=visitedAccelSecond=visitedAccelFirst=visitedGyroFirst=false;
        }


    }

    public void predict(String time){
        Home_Fragment hf = new Home_Fragment();
        timer30+=2;
        timer60+=2;
        List<Float> combinedData = new ArrayList<>();
        combinedData.addAll(ax);
        combinedData.addAll(ay);
        combinedData.addAll(az);
        combinedData.addAll(gy);


        results = MobileActivity.classifier.predictProbabilities(toFloatArray(combinedData));
        Log.d("Time: ", time);
        Log.d("Smoking: ", Double.toString(round(results[0])));
        Log.d("Non-Smoking: ", Double.toString(round(results[1])));

        if(Double.toString(round(results[0])).equals("1.0")){
            smokeCounterForDistraction30+=1;
            smokeCounterForDistraction60+=1;
        }


        if(timer30 == 30)
        {
            if(smokeCounterForDistraction30>2){
                Log.d("PossibleSmoking","The user is smoking ..... Distract!!!");
//                hf.printMessage();
                //hf.printMessage("117979763903338415383");
                DashboardActivity.notifyMessage();
                timer30=0;
                smokeCounterForDistraction30=0;

            }
            else{
                Log.d("PossibleSmoking","The user is not smoking ..... DONT Distract!!!");
                //COMMENT THIS PART
                //DashboardActivity.notifyMessage();
//                hf.printMessage();
                timer30=0;
                smokeCounterForDistraction30=0;
            }

        }

        if(timer60 == 60)
        {
            if(smokeCounterForDistraction60>4){
                Log.d("ConfirmedSmoking","The user is smoking ..... Increase Cigarette Counter!!!");
                hf.printMessage();
                timer60=0;
                smokeCounterForDistraction60=0;

            }
            else{
                Log.d("ConfirmedSmoking","The user is not smoking ..... DONT INCREASE Cigarette Counter");
                timer60=0;
                smokeCounterForDistraction60=0;
            }

        }

        ax.clear();
        ay.clear();
        az.clear();
        gy.clear();

    }


    private float[] toFloatArray(List<Float> list) {
        int i = 0;
        float[] array = new float[list.size()];

        for (Float f : list) {
            array[i++] = (f != null ? f : Float.NaN);
        }
        return array;
    }

//    private int useModel(double[][] input)
//    {
//        int output[][] = new int[1][1];
//        tflite.run(input,output);
//        return output[0][0];
//    }

    //ML

    public void close() {
        try {
            mWriter.close();
        }
        catch (IOException e) {
            e.printStackTrace();
        }
    }
}
