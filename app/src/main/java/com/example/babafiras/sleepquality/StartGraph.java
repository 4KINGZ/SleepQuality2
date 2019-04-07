package com.example.babafiras.sleepquality;

import android.app.AlarmManager;
import android.app.PendingIntent;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.TimePicker;
import android.widget.Toast;
import android.widget.ToggleButton;

import com.jjoe64.graphview.DefaultLabelFormatter;
import com.jjoe64.graphview.GraphView;
import com.jjoe64.graphview.series.DataPoint;
import com.jjoe64.graphview.series.LineGraphSeries;

import java.io.Serializable;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;

import static java.lang.Math.pow;
import static java.lang.Math.sqrt;

public class StartGraph extends AppCompatActivity implements SensorEventListener,
        Serializable {

    TimePicker alarmTimePicker;
    PendingIntent pendingIntent;
    AlarmManager alarmManager;
    private SensorManager sensorManager;
    private boolean started = false;
    private  ArrayList<AccelData> sensorData;

    //GRAPH
    GraphView graphView;
    SimpleDateFormat sdf = new SimpleDateFormat("H:mm");

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_start_graph);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        Button btn_start ;
        btn_start = (Button)findViewById(R.id.btn_start);

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
                    SensorManager.SENSOR_DELAY_NORMAL);



        }
        else
        {

            setContentView(R.layout.activity_graph);
            graphView = (GraphView) findViewById(R.id.graphid);
            double[] Z_Sensor = new double[sensorData.size()];
            if (sensorData.size() > 0) {
                LineGraphSeries<DataPoint> seriesX = new LineGraphSeries<>();
                LineGraphSeries<DataPoint> seriesY = new LineGraphSeries<>();
                LineGraphSeries<DataPoint> seriesZ = new LineGraphSeries<>();
               /* for (int i =0;i<sensorData.size();i++)
                {
                    Z_Sensor[i]= sensorData.get(i).getZ();
                }
                Arrays.sort(Z_Sensor);
                double m = Z_Sensor[(int) sensorData.size()/2];*/
                for (int j = 0; j < sensorData.size(); j++) {
                    DataPoint pointx = new DataPoint(sensorData.get(j).getDate(),
                            sqrt(pow(sensorData.get(j).getX(),2)+pow(sensorData.get(j).getY(),2)));
                    seriesX.appendData(pointx, true, sensorData.size());

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
            }
        }


    }

    @Override
    public void onSensorChanged(SensorEvent event) {

    }

    @Override
    public void onAccuracyChanged(Sensor sensor, int accuracy) {

    }
}
