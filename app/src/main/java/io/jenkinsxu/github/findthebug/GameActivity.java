package io.jenkinsxu.github.findthebug;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;

import android.animation.ArgbEvaluator;
import android.animation.ObjectAnimator;
import android.animation.ValueAnimator;
import android.content.Context;
import android.content.Intent;
import android.content.res.ColorStateList;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.WindowManager;
import android.view.animation.DecelerateInterpolator;
import android.widget.ImageView;
import android.widget.TableLayout;
import android.widget.TableRow;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.material.floatingactionbutton.FloatingActionButton;

import java.text.DecimalFormat;

import io.jenkinsxu.github.findthebug.model.BugNameGenerator;
import io.jenkinsxu.github.findthebug.model.File;
import io.jenkinsxu.github.findthebug.model.FileManager;

public class GameActivity extends AppCompatActivity {

    private static int NUM_ROWS = 4;
    private static int NUM_COLS = 6;
    private static int NUM_BUGS = 6;
    private FileManager fileManager;

    FloatingActionButton buttons[][];
    Integer buttonsClickCount[][];
    private int totalBugs;
    private int totalScans = 0;

    TextView bugCount;
    TextView scanCount;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        this.getWindow().setFlags(
                WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setContentView(R.layout.activity_game);

        setGridSize();
        setBugsNumber();
        populateButtons();
        bugCount = (TextView)findViewById(R.id.bugCount);
        scanCount = (TextView)findViewById(R.id.scanCount);
        bugCount.setText(totalBugs + "/" + NUM_BUGS);
        scanCount.setText(formatTwoDigit(totalScans));
    }

    private void setBugsNumber() {
        NUM_BUGS = OptionActivity.getBugsNumber(this);
        totalBugs = NUM_BUGS;
    }

    private void setGridSize() {
        // Refresh bug count
        int size = OptionActivity.getGridSize(this);
        switch (size) {
            case 24:
                NUM_ROWS = 4;
                NUM_COLS = 6;
                break;
            case 40:
                NUM_ROWS = 5;
                NUM_COLS = 8;
                break;
            case 66:
                NUM_ROWS = 6;
                NUM_COLS = 11;
                break;
            default:
        }
    }

    private void populateButtons() {
        fileManager = new FileManager(NUM_COLS, NUM_ROWS, NUM_BUGS);
        buttons = new FloatingActionButton[NUM_ROWS][NUM_COLS];
        buttonsClickCount = new Integer[NUM_ROWS][NUM_COLS];
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

                buttonsClickCount[row][col] = 0;

                FloatingActionButton button = new FloatingActionButton(this);
                TableRow.LayoutParams buttonLayoutParams = new TableRow.LayoutParams(
                        TableRow.LayoutParams.MATCH_PARENT,
                        TableRow.LayoutParams.MATCH_PARENT,
                        1.0f);
                int marginSize = getButtonMargin();
                buttonLayoutParams.setMargins(marginSize, marginSize, marginSize, marginSize);
                button.setLayoutParams(buttonLayoutParams);
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
        buttonsClickCount[row][col]++;
        int currentButtonClickCount = buttonsClickCount[row][col];
        Log.e("Update click count", row + ", " + col + " to " + currentButtonClickCount);

        if (fileManager.containsBugAt(row, col)) {

            if (currentButtonClickCount == 1) {
                button.setBackgroundTintList(ColorStateList.valueOf(Color.parseColor(
                        "#f0518e")));
                button.setImageDrawable(
                        ContextCompat.getDrawable(this, R.drawable.ic_adb));
                Toast.makeText(
                        this,
                        "You handled a " + BugNameGenerator.getRandomBugName() + "!",
                        Toast.LENGTH_SHORT).show();
                fileManager.debug(row, col);
                reduceBugCount();

                // update number
                for (int rowIndex = 0; rowIndex < NUM_ROWS; rowIndex++) {
                    for (int columnIndex = 0; columnIndex < NUM_COLS; columnIndex++) {
                        if (fileManager.hasBeenInvestigatedAt(rowIndex, columnIndex)) {
                            buttons[rowIndex][columnIndex].setImageBitmap(
                                    textAsBitmap("" + fileManager.numberOfBugsInTotal(rowIndex, columnIndex),
                                            70, Color.WHITE));
                        }
                    }
                }
            }
        } else {
            if (currentButtonClickCount == 1) {
                button.setBackgroundTintList(ColorStateList.valueOf(Color.parseColor(
                        "#7a41fa")));
                increaseScanCount();
                fileManager.markInvestigated(row, col);
                button.setImageBitmap(
                        textAsBitmap("" + fileManager.numberOfBugsInTotal(row, col),
                                70, Color.WHITE));
            } else if (currentButtonClickCount == 2) {
                if (!fileManager.hasBeenInvestigatedAt(row, col)) {
                    increaseScanCount();
                    fileManager.markInvestigated(row, col);
                    button.setImageBitmap(
                            textAsBitmap("" + fileManager.numberOfBugsInTotal(row, col),
                                    70, Color.WHITE));
                }
            }
            animateScan(row, col);
        }
    }

    // For displaying text in a FAB
    // Citation: https://stackoverflow.com/a/39965170
    public static Bitmap textAsBitmap(String text, float textSize, int textColor) {
        Paint paint = new Paint(Paint.ANTI_ALIAS_FLAG);
        paint.setTextSize(textSize);
        paint.setColor(textColor);
        paint.setTextAlign(Paint.Align.LEFT);
        float baseline = -paint.ascent(); // ascent() is negative
        int width = (int) (paint.measureText(text) + 0.0f); // round
        int height = (int) (baseline + paint.descent() + 0.0f);
        Bitmap image = Bitmap.createBitmap(width, height, Bitmap.Config.ARGB_8888);

        Canvas canvas = new Canvas(image);
        canvas.drawText(text, 0, baseline, paint);
        return image;
    }

    private void reduceBugCount() {
        totalBugs--;
        bugCount.setText(this.totalBugs + "/" + NUM_BUGS);
    }

    private void increaseScanCount() {
        totalScans++;
        scanCount.setText(formatTwoDigit(this.totalScans));
    }

    private void startButtonAnimation(int row, int col) {
        FloatingActionButton button = buttons[row][col];

        ObjectAnimator animation = ObjectAnimator.ofFloat(button, "alpha", 1f, 0.3f);
        animation.setRepeatCount(1);
        animation.setRepeatMode(ObjectAnimator.REVERSE);
        animation.setDuration(300);
        animation.start();
    }

    private void animateScan(int row, int col) {
        animateRow(row, col);
        animateColumn(row, col);
    }

    private void animateRow(int row, int excludeColumn) {
        for (int columnIndex = 0; columnIndex < NUM_COLS; columnIndex ++) {
            if (columnIndex != excludeColumn) {
                startButtonAnimation(row, columnIndex);
            }
        }
    }

    private void animateColumn(int excludeRow, int column) {
        for (int rowIndex = 0; rowIndex < NUM_ROWS; rowIndex ++) {
            if (rowIndex != excludeRow) {
                startButtonAnimation(rowIndex, column);
            }
        }
    }

    private String formatTwoDigit(int number) {
        DecimalFormat df = new DecimalFormat("00");
        return df.format(number);
    }

    // for testing only
    private int getButtonMargin() {
        switch (OptionActivity.getGridSize(this)) {
            case 40:
                return 15;
            case 66:
                return 18;
            default:
                return 30;
        }
    }

    @Override
    protected void onResume() {
        super.onResume();
        setGridSize();
    }

    public static Intent makeIntent(Context context) {
        return new Intent(context, GameActivity.class);
    }
}