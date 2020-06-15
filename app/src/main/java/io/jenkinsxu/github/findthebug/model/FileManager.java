package io.jenkinsxu.github.findthebug.model;

import android.util.Log;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class FileManager {
    private List<File> fileList = new ArrayList<>();
    private int numberOfFilesInRow = 0;
    private int numberOfFilesInColumn = 0;

    public FileManager(int numberOfFilesInRow, int numberOfFilesInColumn, int numberOfBugsInFiles) {
        this.numberOfFilesInRow = numberOfFilesInRow;
        this.numberOfFilesInColumn = numberOfFilesInColumn;

        int numberOfFiles = numberOfFilesInRow * numberOfFilesInColumn;
        List<Integer> indexesOfBuggedFiles = getRandomIndexes(numberOfBugsInFiles, numberOfFiles);
        for (int i = 0; i < numberOfFiles; i++) {
            fileList.add(new File(indexesOfBuggedFiles.contains(i)));
        }
    }

    private List<Integer> getRandomIndexes(int numberOfIndexes, int exclusiveUpperBound) {
        Random random = new Random();
        List<Integer> indexes = new ArrayList<>();
        for (int i = 0; i < numberOfIndexes; i++) {
            int index = random.nextInt(exclusiveUpperBound);
            if (indexes.contains(index)) {
                i--;
            } else {
                indexes.add(index);
            }
        }
        return indexes;
    }

    public int numberOfBugsInTotal(int row, int column) {
        File file = this.fileList.get(row * numberOfFilesInRow + column);
        if (file.containsBug()) {
            Log.e("File contains bug being checked", row + ", " + column);
            return numberOfBugsInRow(row) + numberOfBugsInColumn(column) - 1;
        } else {
            return numberOfBugsInRow(row) + numberOfBugsInColumn(column);
        }
    }

    public int numberOfBugsInRow(int row) {
        int bugCount = 0;
        for (int columnIndex = 0; columnIndex < numberOfFilesInRow; columnIndex ++) {
            File file = fileList.get(row * numberOfFilesInRow + columnIndex);
            if (file.containsBug()) {
                bugCount ++;
            }
        }
        return bugCount;
    }

    public int numberOfBugsInColumn(int column) {
        int bugCount = 0;
        for (int rowIndex = 0; rowIndex < numberOfFilesInColumn; rowIndex ++) {
            File file = fileList.get(column + rowIndex * numberOfFilesInRow);
            if (file.containsBug()) {
                bugCount++;
            }
        }
        return bugCount;
    }

    public void debug(int row, int column) {
        this.fileList.get(row * numberOfFilesInRow + column).debug();
    }

    public Boolean containsBugAt(int row, int column) {
        return this.fileList.get(row * numberOfFilesInRow + column).containsBug();
    }

    public void markInvestigated(int row, int column) {
        this.fileList.get(row * numberOfFilesInRow + column).gotInvestigated();
    }

    public Boolean hasBeenInvestigatedAt(int row, int column) {
        return this.fileList.get(row * numberOfFilesInRow + column).isInvestigated();
    }
}
