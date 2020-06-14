package io.jenkinsxu.github.findthebug;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;

import android.content.res.ColorStateList;
import android.graphics.Color;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.TableLayout;
import android.widget.TableRow;
import android.widget.Toast;

import com.google.android.material.floatingactionbutton.FloatingActionButton;

import io.jenkinsxu.github.findthebug.model.BugNameGenerator;
import io.jenkinsxu.github.findthebug.model.FileManager;

public class GameActivity extends AppCompatActivity {

    private static final int NUM_ROWS = 4;
    private static final int NUM_COLS = 8;
    private static final int NUM_BUGS = 4; // for testing only
    private FileManager fileManager = new FileManager(NUM_COLS, NUM_ROWS, NUM_BUGS);

    FloatingActionButton buttons[][] = new FloatingActionButton[NUM_ROWS][NUM_COLS];

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        this.getWindow().setFlags(
                WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setContentView(R.layout.activity_game);

        populateButtons();
    }

    private void populateButtons() {
        TableLayout table = (TableLayout) findViewById(R.id.tableForButtons);
        for (int row = 0; row < NUM_ROWS; row++) {
            TableRow tableRow = new TableRow(this);
            tableRow.setLayoutParams(new TableLayout.LayoutParams(
                    TableLayout.LayoutParams.MATCH_PARENT,
                    TableLayout.LayoutParams.MATCH_PARENT,
                    1.0f
            ));
            table.addView(tableRow);

            for (int col = 0; col < NUM_COLS; col++) {
                final int FINAL_ROW = row;
                final int FINAL_COL = col;

                FloatingActionButton button = new FloatingActionButton(this);
                TableRow.LayoutParams buttonLayoutParams = new TableRow.LayoutParams(
                        TableRow.LayoutParams.MATCH_PARENT,
                        TableRow.LayoutParams.MATCH_PARENT,
                        1.0f);
                buttonLayoutParams.setMargins(30, 30, 30, 35);
                button.setLayoutParams(buttonLayoutParams);
                button.setCustomSize(120);
                button.setScaleType(ImageView.ScaleType.CENTER);
                button.setImageDrawable(
                        ContextCompat.getDrawable(this, R.drawable.ic_folder));

                button.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        gridButtonClicked(FINAL_ROW, FINAL_COL);
                    }
                });

                tableRow.addView(button);
                buttons[row][col] = button;
            }
        }
    }

    private void gridButtonClicked(int row, int col) {
        FloatingActionButton button = buttons[row][col];

        if (fileManager.containsBugAt(row, col)) {
            button.setBackgroundTintList(ColorStateList.valueOf(Color.parseColor(
                    "#f0518e")));
            button.setImageDrawable(
                    ContextCompat.getDrawable(this, R.drawable.ic_adb));
            Toast.makeText(
                    this,
                    "You handled a " + BugNameGenerator.getRandomBugName() + "!",
                    Toast.LENGTH_SHORT).show();
        } else {
            button.setBackgroundTintList(ColorStateList.valueOf(Color.parseColor(
                    "#7a41fa")));
        }
    }
}