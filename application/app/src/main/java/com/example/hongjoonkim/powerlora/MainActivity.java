package com.example.hongjoonkim.powerlora;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;


public class MainActivity extends AppCompatActivity {

    Button btn1,btn2,btn3,btn4;
    Intent intent = null;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Intent intent = new Intent(MainActivity.this,MyService.class);
        startService(intent);
    }
    public void onClick(View v)
    {
        Intent intent = null;

        switch (v.getId())
        {
            case R.id.btn1:
                intent = new Intent(this,setLocation3.class);
                break;
            case R.id.setlocation:
                intent = new Intent(this,setLocation3.class);
                break;
            case R.id.btn2:
                intent = new Intent(this,MapsActivity.class);
                break;
            case R.id.location:
                intent = new Intent(this,MapsActivity.class);
                break;
            case R.id.btn3:
                intent = new Intent(this,HeartBeatActivity.class);
                break;
            case R.id.heart:
                intent = new Intent(this,HeartBeatActivity.class);
                break;
            case R.id.btn4:
                intent = new Intent(this,moveActivity.class);
                break;
            case R.id.summove:
                intent = new Intent(this,moveActivity.class);
                break;
        }
        startActivity(intent);
    }
}

