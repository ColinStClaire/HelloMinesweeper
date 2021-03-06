package files;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;
import java.util.Random;

/**
 * @filename MineField.java
 * @author Colin St. Claire
 * @created 04/14/16
 * @modified 04/21/16
 */

/**
 * This class is the "model" in my Minesweeper Game. Its duties include:
 * 1. Keeping track of which cells are marked, exposed, and hide bombs
 * 2. Store or computer # of bombs adjacent to a given cell
 * 3. Implement the following methods:
 *      -boolean mark(int col, int row);
 *      -int expose(int col, int row);
 *      -int isExposed(int col, int row);
 *      -public int unexposedCount()
 * 4. Ignore calls to expose a cell if it is marked
 * 5. calling unexposedCount() should return # of cells that can be exposed
 *    without setting off a bomb
 * 6. expose() should return:
 *      # 0 if a cell was safely exposed and no bombs are adjacent to it.
 *          In this case, the all adjacent cells with 0 adjacent bombs should
 *          also be exposed. These newly revealed neighbors s can be revealed
 *          by call(s) to isExposed
 *      # 1-8 if a cell was safely exposed and 1 or more bombs is adjacent to it
 *      # -1 If a bomb was exposed at the game is over
 */


public class MineField {
    public int[][] minefield;
    public boolean[][] fieldMarked;
    public boolean[][] fieldExposed;
    public int rowSize = 7;
    public int colSize = 7;
    public int mines = 8;


    // constructor
    public MineField(int diff) {
        rowSize *= diff;
        colSize *= diff;
        mines *= diff;
        minefield = new int[rowSize][colSize];
        fieldMarked = new boolean[rowSize][colSize];
        fieldExposed = new boolean[rowSize][colSize];
    }


    public void placeMines() {
        // places mines (cell value of -1 equals a mine)
        Random rand = new Random();
        int i = 0;
        while (i < mines) {
            int randRow = rand.nextInt(rowSize);
            int randCol = rand.nextInt(colSize);
            if (minefield[randRow][randCol] != -1) {
                minefield[randRow][randCol] = -1;
                i++;
            }
        }
    }


    public boolean mark(int row, int col) {
        // toggles the mark status and returns the new value of true or false
        fieldMarked[row][col] = !fieldMarked[row][col];
        return fieldMarked[row][col];
    }


    public int expose(int row, int col) {
        // given a cell, return number of bombs adjacent to it (0-8) or -1 if a bomb
        if (minefield[row][col] == -1) return -1;

        int mineCount = 0;

        if (!fieldMarked[row][col] && !fieldExposed[row][col]) {
            for (int i = row - 1; i <= row + 1; i++) {
                for (int j = col - 1; j <= col + 1; j++) {
                    if (mineCheck(i, j) && minefield[i][j] == -1) mineCount++;
                    fieldExposed[row][col] = true;
                }
            }
            minefield[row][col] = mineCount;

            if (mineCount == 0) { // if no bombs, expose adj. cells and check each cell for adj mines
                for (int i = row - 1; i <= row + 1; i++) {
                    for (int j = col - 1; j <= col + 1; j++) {
                        if (mineCheck(i, j) && !fieldExposed[i][j]) {
                            System.out.println("Inside expose(): cells[" + i + "][" + j + "]");
                            expose(i, j);
                        }
                    }
                }
            }
        }

        return mineCount;
    }


    private boolean mineCheck(int row, int col) {
        // return whether or not the cell is a valid index
        if (row >= 0 && row < rowSize && col >= 0 && col < colSize) {
            return true;
        }
        return false;
    }


    public int unexposedCount() {
        // compute and return how many cells can be exposed without setting off a bomb
        int count = 0;

        for (int i = 0; i < rowSize; i++) {
            for (int j = 0; j < colSize; j++) {
                if (minefield[i][j] != -1 && !fieldExposed[i][j]) count++;
            }
        }
        return count;
    }

    public boolean winCheck() {
        int count = 0;
        for (int i = 0; i < rowSize; i++) {
            for (int j = 0; j < colSize; j++) {
                if (!fieldExposed[i][j]) count++;
            }
        }
        return mines == count;
    }

    public static void main(String[] args) {
        // simple test
        MineField mf = new MineField(5);
        mf.placeMines();
        for (int i = 0;  i < mf.rowSize; i++) {
            for (int j = 0; j < mf.colSize; j++) {
                mf.expose(i, j);
                System.out.println("minefield[" + i + "][" + j + "] = " + mf.minefield[i][j]);
            }
        }
    }

}
