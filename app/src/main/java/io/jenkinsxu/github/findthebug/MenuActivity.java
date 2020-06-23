package io.jenkinsxu.github.findthebug;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

public class MenuActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_menu);

        setupStartButton();
        setupOptionsButton();
    }

    private void setupStartButton() {
        Button button = (Button) findViewById(R.id.startButton);
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = GameActivity.makeIntent(MenuActivity.this);
                startActivity(intent);
            }
        });
    }

    private void setupOptionsButton() {
        Button button = (Button) findViewById(R.id.optionButton);
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = OptionActivity.makeIntent(MenuActivity.this);
                startActivity(intent);
            }
        });
    }
}