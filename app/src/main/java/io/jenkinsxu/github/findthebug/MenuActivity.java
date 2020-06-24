package io.jenkinsxu.github.findthebug;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

/**
 * HelpActivity is for the menu screen. Other
 * Activities can be launched through this class.
 */
public class MenuActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_menu);

        Intent intent = GameActivity.makeIntent(MenuActivity.this);
        setupButtonForLaunch(R.id.start_button, intent);

        intent = OptionActivity.makeIntent(MenuActivity.this);
        setupButtonForLaunch(R.id.option_button, intent);

        intent = HelpActivity.makeIntent(MenuActivity.this);
        setupButtonForLaunch(R.id.help_button, intent);

    }

    private void setupButtonForLaunch(int id, final Intent intent) {
        Button button = (Button) findViewById(id);
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(intent);
            }
        });
    }

    public static Intent makeIntent(Context context) {
        return new Intent(context, MenuActivity.class);
    }
}