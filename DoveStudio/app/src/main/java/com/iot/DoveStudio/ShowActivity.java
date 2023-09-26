package com.iot.DoveStudio;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.text.Html;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.iot.DoveStudio.MyThread.Mythread;

public class ShowActivity extends AppCompatActivity {
    private final String TAG= "ShowActivity";
    private TextView tvMessageReceived = null;
    private Button bttRicarica= null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_show);

        tvMessageReceived = findViewById(R.id.tvMessageReceived);

        Intent _intent = getIntent();
        String _message = _intent.getStringExtra(getString(R.string.LABEL_MESSAGE));
        tvMessageReceived.setText(Html.fromHtml(_message));

        bttRicarica= findViewById(R.id.bttRicarica);

        bttRicarica.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
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

                tvMessageReceived.setText(Html.fromHtml(mythread.getMessage()));
            }
        });
    }
}