package com.example.demoproject;

import android.app.Activity;
import android.content.Intent;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;
import androidx.appcompat.app.AppCompatActivity;
import android.os.Bundle;

public class MainActivity2 extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main2);

        Button btn = (Button) findViewById(R.id.button);
        btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                goBack();
            }
        });

        Intent intent = getIntent();
        Toast.makeText(MainActivity2.this, intent.getStringExtra("request"), Toast.LENGTH_SHORT).show();
    }
    private void goBack() {
        this.onBackPressed();
        finish();
    }

    @Override
    public void finish() {
        Intent intent = new Intent();
        intent.putExtra("result", "Back to main screen");

        this.setResult(Activity.RESULT_OK, intent);
        super.finish();
    }
}