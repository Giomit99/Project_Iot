package com.iot.DoveStudio;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

public class StartActivity extends AppCompatActivity {
    Button button= null;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_start);

        button= findViewById(R.id.bttStartActivity);

        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Log.i("click", "click");
                Intent intent= new Intent(StartActivity.this, InfoActivationActivity.class);
                startActivity(intent);
            }
        });
    }
}
