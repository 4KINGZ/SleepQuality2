package com.example.babafiras.sleepquality;

import android.content.Intent;
import android.net.Uri;
import android.os.Environment;
import android.os.Handler;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import java.io.File;

public class Menu extends AppCompatActivity {
    Button instruction;
    Button goAlarm;
    Button goTest,check;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_menu);

        instruction=(Button)findViewById(R.id.instructions);
        instruction.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                openInstractions();
            }
        });

        goAlarm=(Button)findViewById(R.id.begin);
        goAlarm.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                openAlarm();
            }
        });

        goTest=(Button)findViewById(R.id.test_your_phone);
        goTest.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                openTest();
            }
        });

        check=(Button)findViewById(R.id.hist_stats);
        check.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Toast.makeText(Menu.this, "Accessing graphs folder"
                        , Toast.LENGTH_LONG).show();openFolder();
            }
        });

    }

    public void openFolder(){
        Intent intent = new Intent(Intent.ACTION_GET_CONTENT);
        //File imageRoot = new File(Environment.getExternalStoragePublicDirectory(
              //  Environment.DIRECTORY_PICTURES), "Sleep Quality Graphs");

        //Uri uri = Uri.parse(Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_PICTURES).getPath());
        //intent.setDataAndType(uri, "*/*");
        //startActivity(Intent.createChooser(intent, "Open folder"));
        //Intent intent = new Intent(Intent.ACTION_GET_CONTENT);
        Uri uri = Uri.parse(Environment.getExternalStorageDirectory().getPath()
                +  File.separator + "Sleep Quality Graphs" + File.separator);
        intent.setDataAndType(uri, "*/*");
        startActivity(Intent.createChooser(intent, "Open folder"));

    }











    public void openInstractions()
    {
        Intent intent = new Intent(this, Instructions.class);
        startActivity(intent);

    }
    public void openAlarm()
    {
        Intent intent = new Intent(this, SetAlarm.class);
        startActivity(intent);

    }
    public void openTest()
    {
        Intent intent = new Intent(this, testGraph.class);
        startActivity(intent);

    }
}

