package com.example.babafiras.sleepquality;

import android.app.AlarmManager;
import android.app.AlertDialog;
import android.app.PendingIntent;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Environment;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RatingBar;
import android.widget.TextView;
import android.widget.Toast;

import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TimePicker;
import android.widget.Toast;
import android.widget.ToggleButton;

import com.jjoe64.graphview.DefaultLabelFormatter;
import com.jjoe64.graphview.GraphView;
import com.jjoe64.graphview.series.DataPoint;
import com.jjoe64.graphview.series.LineGraphSeries;

import java.io.File;
import java.io.FileOutputStream;
import java.io.Serializable;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.concurrent.TimeUnit;

import static java.lang.Math.abs;
import static java.lang.Math.exp;
import static java.lang.Math.pow;
import static java.lang.Math.sqrt;


public class SetAlarm extends AppCompatActivity implements SensorEventListener,
        Serializable {

    TimePicker alarmTimePicker;
    PendingIntent pendingIntent;
    AlarmManager alarmManager;
    private SensorManager sensorManager;
    private boolean started = false;
    private  ArrayList<AccelData> sensorData;
    private Button btnSave, btnAssess;
    private Button btnEval;
    private RatingBar mRatingBar;
    private int small=0;
    private int big=0;
    final private double seuilMin = 2.9;
    final private double seuilMax = 6.8;
    private double qual=0;
    private double qualFeed=0;
    long diffh=0;long diffm=0;long diffs=0;
    long alarmTime;
    boolean wu=false;


    //GRAPH
    GraphView graphView;
    SimpleDateFormat sdf = new SimpleDateFormat("H:mm");




    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        //SENSORS + ALARM
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_set_alarm);
        alarmTimePicker = (TimePicker) findViewById(R.id.timePicker);
        alarmManager = (AlarmManager) getSystemService(ALARM_SERVICE);
        sensorManager = (SensorManager) getSystemService(Context.SENSOR_SERVICE);
        sensorData=new ArrayList<AccelData>();
        //btnSave = (Button)findViewById(R.id.btnSave);

    }


    private void configureButton2() {
        Button button = (Button) findViewById(R.id.btnSave);
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //setContentView(R.layout.activity_test);
                Date date = Calendar.getInstance().getTime();
                DateFormat dateFormat = new SimpleDateFormat("yyyy-mm-dd hh:mm:ss");
                String strDate = dateFormat.format(date);

                Bitmap bitmap = graphView.takeSnapshot();
                final String appDirectoryName = "Sleep Quality Graphs";
                final File imageRoot = new File(Environment.getExternalStoragePublicDirectory(
                        Environment.DIRECTORY_PICTURES), appDirectoryName);
                imageRoot.mkdir();
                try {
                    final File image = new File(imageRoot, strDate +".JPEG");
                    FileOutputStream ostream = new FileOutputStream(image);
                    bitmap.compress(Bitmap.CompressFormat.JPEG, 10, ostream);
                    ostream.close();
                }catch (Exception e){
                    e.printStackTrace();
                }





            }


        });


        //btnSave.setOnClickListener(graph.takeSnapshotAndShare(MainActivity, "exampleGraph", "GraphViewSnapshot"););
    }


    public void configureButtonA() {
        btnAssess = (Button)findViewById(R.id.btnAssess);
        //final TextView mRatingScale = (TextView) findViewById(R.id.ratingscale);
        //final RatingBar mRatingBar = (RatingBar) findViewById(R.id.ratingBar);
        btnAssess.setOnClickListener(
                new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        setContentView(R.layout.activity_quality);
                        RatingBar mRatingBar = (RatingBar) findViewById(R.id.ratingBar);
                        final TextView mRatingScale = (TextView) findViewById(R.id.ratingscale);
                        mRatingBar.setOnRatingBarChangeListener(new RatingBar.OnRatingBarChangeListener() {
                                                                    @Override
                                                                    public void onRatingChanged(RatingBar ratingBar, float v, boolean b) {
                                                                        mRatingScale.setText(String.valueOf(v));
                                                                        switch ((int) ratingBar.getRating()) {
                                                                            case 1:
                                                                                mRatingScale.setText("It was awful!!");
                                                                                qual-=qualFeed;
                                                                                qualFeed = 5;
                                                                                qual += qualFeed;
                                                                                break;
                                                                            case 2:
                                                                                mRatingScale.setText("I could use more sleep");
                                                                                qual-=qualFeed;
                                                                                qualFeed = 10;
                                                                                qual += qualFeed;
                                                                                break;
                                                                            case 3:
                                                                                mRatingScale.setText("I feel well rested");
                                                                                qual-=qualFeed;
                                                                                qualFeed = 15;
                                                                                qual += qualFeed;
                                                                                break;
                                                                            case 4:
                                                                                mRatingScale.setText("I'm waking up full of neregy");
                                                                                qual-=qualFeed;
                                                                                qualFeed = 20;
                                                                                qual += qualFeed;
                                                                                break;
                                                                            case 5:
                                                                                mRatingScale.setText("I loved it");
                                                                                qual-=qualFeed;
                                                                                qualFeed = 25;
                                                                                qual += qualFeed;
                                                                                break;
                                                                            default:
                                                                                mRatingScale.setText("");
                                                                        }

                                                                    }

                                                                }

                        );

                        btnEval = (Button) findViewById(R.id.btnEval);

                        btnEval.setOnClickListener(
                                new View.OnClickListener() {
                                    @Override
                                    public void onClick(View v) {
                                        AlertDialog.Builder a_builder = new AlertDialog.Builder(SetAlarm.this);
                                        a_builder.setMessage("Sleep quality Assessment complete! Your sleep rating was "
                                                + Double.toString(qual) + "  %")
                                                .setCancelable(false)
                                                .setPositiveButton("Close", new DialogInterface.OnClickListener() {
                                                    @Override
                                                    public void onClick(DialogInterface dialog, int which) {
                                                        finish();
                                                    }
                                                });

                                        AlertDialog alert = a_builder.create();
                                        alert.setTitle("Finished!");
                                        alert.show();
                                    }
                                }
                        );

                        TextView rolls = (TextView) findViewById(R.id.rolls);
                        TextView wake = (TextView) findViewById(R.id.wake);
                        TextView dura = (TextView) findViewById(R.id.hrs);


                        //QUALITY ASSESSMENT MODEL

                        long timestamp = System.currentTimeMillis();
                        long diff = (timestamp - alarmTime) / 1000;
                        long diffh = diff / 3600;


                        long diffm = ((diff) % 3600) / 60;
                        long diffs = ((diff) % 3600) % 60;
                        dura.setText("You slept for" + " " + Long.toString(diffh) + " hours and "
                                + Long.toString(diffm) + " minutes and " + Long.toString(diffs)+ " seconds.");
                        rolls.setText("You rolled in your sleep" + " " + Integer.toString(small) + " times.");
                        wake.setText("You woke up in your sleep " + Integer.toString(big) + " times.");
                        qual += 50 * (1 - exp(-diffm / 96));












                    }
                }


        );

    }



    public void onButtonEval() {
        //setContentView(R.layout.activity_quality);


    }


    public void OnToggleClicked(View view)
    {
        long time;

        if (((ToggleButton) view).isChecked())
        {
            started=true;

            Sensor accel = sensorManager
                    .getDefaultSensor(Sensor.TYPE_ACCELEROMETER);
            sensorManager.registerListener(this, accel,
                    500000);



            Toast.makeText(SetAlarm.this, "ALARM ON - SleepQuality is recording your sleep motion..."
                    , Toast.LENGTH_LONG).show();
            Calendar calendar = Calendar.getInstance();
            calendar.set(Calendar.HOUR_OF_DAY, alarmTimePicker.getCurrentHour());
            calendar.set(Calendar.MINUTE, alarmTimePicker.getCurrentMinute());
            Intent intent = new Intent(this, Alarm.class);
            pendingIntent = PendingIntent.getBroadcast(this, 0, intent, 0);

            time=(calendar.getTimeInMillis()-(calendar.getTimeInMillis()%60000));

            if(System.currentTimeMillis()>time)
            {
                if (calendar.AM_PM == 0)
                    time = time + (1000*60*60*12);
                else
                    time = time + (1000*60*60*24);
            }
            alarmTime=System.currentTimeMillis();
            alarmManager.setRepeating(AlarmManager.RTC_WAKEUP, time, 10000, pendingIntent);
        }
        else
        {
            alarmManager.cancel(pendingIntent);
            Toast.makeText(SetAlarm.this, "ALARM OFF - Displaying graph...", Toast.LENGTH_SHORT).show();

            setContentView(R.layout.activity_graph);
            btnSave =(Button) findViewById(R.id.btnSave);
            configureButton2();
            long endDate = System.currentTimeMillis();


            graphView = (GraphView) findViewById(R.id.graphid);

            if (sensorData.size() > 0) {
                LineGraphSeries<DataPoint> seriesX = new LineGraphSeries<>();
                LineGraphSeries<DataPoint> seriesY = new LineGraphSeries<>();
                LineGraphSeries<DataPoint> seriesZ = new LineGraphSeries<>();
                for (int j = 0; j < sensorData.size(); j++) {
                    DataPoint pointx = new DataPoint(sensorData.get(j).getDate(),
                            sqrt(pow(sensorData.get(j).getX(),2)+pow(sensorData.get(j).getY(),2)));
                    //DataPoint pointy = new DataPoint(sensorData.get(j).getDate(), sensorData.get(j).getY());
                    //DataPoint pointz = new DataPoint(sensorData.get(j).getDate(), sensorData.get(j).getZ());
                    seriesX.appendData(pointx, true, sensorData.size());
                    //seriesY.appendData(pointy, true, sensorData.size());
                    //seriesZ.appendData(pointz, true, sensorData.size());
                }
                graphView.getGridLabelRenderer().setLabelFormatter(new DefaultLabelFormatter(){
                    @Override
                    public String formatLabel(double value, boolean isval){
                        if(isval){
                            return sdf.format(new Date((long) value));
                        }else{
                            return super.formatLabel(value,isval);

                        }
                    }
                });

                graphView.addSeries(seriesX);
                //graphView.addSeries(seriesY);
                //graphView.addSeries(seriesZ);
            }
            for(int i=0;i<sensorData.size();i++){
                double comp =abs(sensorData.get(i).getX())+abs(sensorData.get(i).getY());
                if((comp>seuilMin)&&(comp<seuilMax)){
                    i+=10;

                    small++;
                }
                if(comp>seuilMax) {

                    wu=true;
                    big =1;
                }


                }





            configureButtonA();
            onButtonEval();
            //TextView dura= (TextView)findViewById(R.id.hrs);

            //dura.setText("You slept for"+" " +Long.toString(diffh)+" and "+Long.toString(diffm));

            //

            switch (small) {
                case 0:
                    qual += 21.32;
                case 1:
                    qual += 19.22;
                case 2:
                    qual += 12.55;
                case 3:
                    qual += 8.02;
                case 4:
                    qual += 4.77;
                default:
                    qual += 2.523;
            }




            //Intent intent = new Intent(this, test.class);
            //intent.putExtra("data", new DataWrapper(sensorData));
        }
        //graphView = (GraphView) findViewById(R.id.graphid);

    }
    @Override
    protected void onResume () {
        super.onResume();

    }

    @Override
    protected void onPause () {
        super.onPause();
        //if (started == true) {
        //  sensorManager.unregisterListener(this);
        //}
    }

    @Override
    public void onAccuracyChanged (Sensor sensor,int accuracy){

    }

    @Override
    public void onSensorChanged (SensorEvent event){
        if (started) {
            double x = event.values[0];
            double y = event.values[1];
            double z = event.values[2];
            long date = new Date().getTime();


            //long timestamp = System.currentTimeMillis();
            AccelData data = new AccelData(date, x, y, z);
            sensorData.add(data);
            //new DataPoint(new Date().getTime(), x);
            //new DataPoint(new Date().getTime(), y);
            //new DataPoint(new Date().getTime(), z);
        }

    }



}
