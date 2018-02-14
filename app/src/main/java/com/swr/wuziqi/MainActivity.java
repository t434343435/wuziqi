package com.swr.wuziqi;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

public class MainActivity extends AppCompatActivity {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Button start = findViewById(R.id.button_start);
        start.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                ((MainView)findViewById(R.id.game_view)).reset();
                findViewById(R.id.game_view).invalidate();
            }
        });
        Button undo = findViewById(R.id.button_undo);
        undo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                ((MainView)findViewById(R.id.game_view)).undo();
                findViewById(R.id.game_view).invalidate();
            }
        });
        Button end = findViewById(R.id.button_exit);
        end.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });
    }
}
