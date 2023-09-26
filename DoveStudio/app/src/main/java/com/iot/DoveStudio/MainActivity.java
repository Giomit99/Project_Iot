package com.iot.DoveStudio;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import com.iot.DoveStudio.MyThread.Mythread;
import com.iot.DoveStudio.Service.BluetoothScan;

public class MainActivity extends AppCompatActivity {
    private final String TAG = "MainActivity";
    private final int ACTIVITY_REQUEST_CODE = 1;
    private EditText etMessage = null;
    private Button bttStartActivity = null;

    /***********************/
    private Mythread mythread;
    /***********************/

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        /*Intent serviceIntent= new Intent(this, BluetoothScan.class);
        startForegroundService(serviceIntent);*/
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            startForegroundService(new Intent(this, BluetoothScan.class));
        }
        else Log.i(TAG, "Problema con Biuld.Version");

        bttStartActivity = findViewById(R.id.bttStartActivity);

        bttStartActivity.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                /********************************/
                Log.i(TAG, "OnClick");
                Mythread mythread= new Mythread();
                mythread.setTypeMessage("SHOW");
                Thread thread= new Thread(mythread);
                thread.start();

                try {
                    thread.join();
                } catch (InterruptedException e) {
                    throw new RuntimeException(e);
                }

                Intent intent = new Intent(MainActivity.this, ShowActivity.class);
                intent.putExtra(getString(R.string.LABEL_MESSAGE), mythread.getMessage());
                startActivity(intent);
            }
        });
    }
}