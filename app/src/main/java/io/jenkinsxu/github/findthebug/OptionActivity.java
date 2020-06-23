package io.jenkinsxu.github.findthebug;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.RadioButton;
import android.widget.RadioGroup;

public class OptionActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_option);

//        int savedValue = getGridSize(this);

        createGridSizeOptions();
        createBugsNumberOptions();
    }

    private void createBugsNumberOptions() {
        RadioGroup group = (RadioGroup) findViewById(R.id.bugsOptions);

        int[] bugsNumber = getResources().getIntArray(R.array.bugs_number);

        // Create the button
        for (int i = 0; i < bugsNumber.length; i++) {
            final int SIZE = bugsNumber[i];
            RadioButton button = new RadioButton(this);
            button.setText("Number of bugs: " + SIZE);

            // Set on-click callbacks
            button.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    saveBugsNumber(SIZE);
                }
            });

            // Add to radio group
            group.addView(button);

            // Select default button
            if (SIZE == getBugsNumber(this)) {
                button.setChecked(true);
            }
        }
    }

    private void createGridSizeOptions() {
        RadioGroup group = (RadioGroup) findViewById(R.id.gridSizeOptions);

        int[] gridSize = getResources().getIntArray(R.array.grid_size);

        // Create the button
        for (int i = 0; i < gridSize.length; i++) {
            final int SIZE = gridSize[i];
            RadioButton button = new RadioButton(this);
            button.setText("Number of cells: " + SIZE);

            // Set on-click callbacks
            button.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    saveGripSize(SIZE);
                }
            });

            // Add to radio group
            group.addView(button);

            // Select default button
            if (SIZE == getGridSize(this)) {
                button.setChecked(true);
            }
        }
    }

    // TODO: Cleanup repeated code
    private void saveGripSize(int size) {
        SharedPreferences preferences =
                this.getSharedPreferences("AppPreference", MODE_PRIVATE);
        SharedPreferences.Editor editor = preferences.edit();
        editor.putInt("Size of grid", size);
        editor.apply();
    }

    private void saveBugsNumber(int size) {
        SharedPreferences preferences =
                this.getSharedPreferences("AppPreference", MODE_PRIVATE);
        SharedPreferences.Editor editor = preferences.edit();
        editor.putInt("Number of bugs", size);
        editor.apply();
    }

    static public int getGridSize(Context context) {
        SharedPreferences preferences =
                context.getSharedPreferences("AppPreference", MODE_PRIVATE);
        int defaultSize = context.getResources().getInteger(R.integer.default_grid_size);
        return preferences.getInt("Size of grid", defaultSize);
    }

    static public int getBugsNumber(Context context) {
        SharedPreferences preferences =
                context.getSharedPreferences("AppPreference", MODE_PRIVATE);
        int defaultSize = context.getResources().getInteger(R.integer.default_bug_number);
        return preferences.getInt("Number of bugs", defaultSize);
    }

    public static Intent makeIntent(Context context) {
        return new Intent(context, OptionActivity.class);
    }
}