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
 * @modified 04/20/16
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
    public int rowSize = 5;
    public int colSize = 5;
    public final int EASY = 1;
    public final int MED = 2;
    public final int HARD = 3;
    public int mines = 8;


    // constructor
    public MineField(int diff) {
        assert diff >= 1 : diff <= 3;
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

        if (!fieldMarked[row][col]) {
            for (int i = row - 1; i <= row + 1; i++) {
                for (int j = col - 1; j <= col + 1; j++) {
                    if (mineCheck(i, j)) mineCount++;
                }
            }
            minefield[row][col] = mineCount;
            fieldExposed[row][col] = true;
        }
        return mineCount;
    }

    private boolean mineCheck(int row, int col) {
        // if the cell is valid and is a bomb, return 1 , else return 0
        if (row >= 0 && row < rowSize && col >= 0 && col < colSize && minefield[row][col] == -1) {
            return true;
        }
        return false;
    }

    //@TODO: 4/14/16
    public int isExposed(int row, int col) {
        // computes and returns how many cells are exposed
        int count = 0;
        for (int i = 0; i < rowSize; i++) {
            for (int j = 0; j < colSize; j++) {
                if (fieldExposed[i][j]) count++;
            }
        }
        return count;
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


    public static void main(String[] args) {
        MineField mf = new MineField(1);
        mf.placeMines();
        for (int i = 0;  i < mf.rowSize; i++) {
            for (int j = 0; j < mf.colSize; j++) {
                //mf.expose(i, j);
                System.out.println("minefield[" + i + "][" + j + "] = " + mf.minefield[i][j]);
            }
        }
    }

}
