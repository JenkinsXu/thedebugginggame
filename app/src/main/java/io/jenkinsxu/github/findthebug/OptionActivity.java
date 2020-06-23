package io.jenkinsxu.github.findthebug;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.RadioButton;
import android.widget.RadioGroup;

public class OptionActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_option);

        int savedValue = getGridSize(this);

        createGridSizeOptions();
        setupSaveButton();
    }

    private void setupSaveButton() {
        Button button = (Button) findViewById(R.id.saveButton);
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                RadioGroup group = (RadioGroup) findViewById(R.id.gridSizeOptions);
                int idOfSelected = group.getCheckedRadioButtonId();
//                RadioButton radioButton = (RadioButton) findViewById(idOfSelected);

            }
        });
    }

    private void createGridSizeOptions() {
        RadioGroup group = (RadioGroup) findViewById(R.id.gridSizeOptions);

        int[] gridSize = getResources().getIntArray(R.array.grid_size);

        // Create the button
        for (int i = 0; i < gridSize.length; i++) {
            final int size = gridSize[i];
            RadioButton button = new RadioButton(this);
            button.setText("Number of rows: " + size);

            // Set on-click callbacks
            button.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    saveGripSize(size);
                }
            });

            // Add to radio group
            group.addView(button);

            // Select default button
            if (size == getGridSize(this)) {
                button.setChecked(true);
            }
        }

    }

    private void saveGripSize(int size) {
        SharedPreferences preferences =
                this.getSharedPreferences("AppPreference", MODE_PRIVATE);
        SharedPreferences.Editor editor = preferences.edit();
        editor.putInt("Size of grid", size);
        editor.apply();
    }

    static public int getGridSize(Context context) {
        SharedPreferences preferences =
                context.getSharedPreferences("AppPreference", MODE_PRIVATE);
        int defaultSize = context.getResources().getInteger(R.integer.default_size);
        return preferences.getInt("Size of grid", defaultSize);
    }

    public static Intent makeIntent(Context context) {
        return new Intent(context, OptionActivity.class);
    }
}