package files;

import javafx.application.Platform;
import javafx.beans.binding.StringBinding;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.ChoiceBox;
import javafx.scene.control.Label;
import javafx.scene.input.MouseButton;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.StackPane;

/**
 * @filename Controller.java
 * @author Colin St. Claire
 * @created 04/20/16
 * @modified 04/29/16
 */

public class Controller {
    @FXML
    private GridPane board;
    @FXML
    private Label greeting = new Label("Welcome to Minesweeper! Please select a difficulty and press Start.");
    @FXML
    private Label playAgain;
    @FXML
    private Button start = new Button("Start");
    @FXML
    private Button quit = new Button("Quit");
    @FXML
    private Button[][] cells;
    @FXML
    private StackPane greetBox;
    @FXML
    private ChoiceBox<String> difficulty = new ChoiceBox();
    @FXML
    private Label safeCellCount;
    @FXML
    private void initialize() {
        difficulty.getItems().addAll("Easy", "Medium", "Hard");
        difficulty.setValue("Easy");
        start.setOnAction(new EventHandler<ActionEvent>() {
            // this is what happens when diff is selected, and start is pressed
            @Override
            public void handle(ActionEvent event) {
                greetBox.toBack();
                greeting.setText("");
                int diff = 1;
                String d = difficulty.getValue();
                System.out.println(d);
                if (d.equals("Easy")) diff = 1;
                else if (d.equals("Medium")) diff = 2;
                else diff = 3;
                startGame(diff);
            }
        });

        quit.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent event) {
                quit();
            }
        });

    }


    /* Methods! */

    private void startGame(int diff) {
        MineField mf = new MineField(diff);
        cells = new Button[mf.rowSize][mf.colSize];
        // add buttons
        for (int i = 0 ; i < mf.rowSize; i++) {
            for (int j = 0; j < mf.colSize; j++) {
                cells[i][j] = new Button();
                cells[i][j].setMaxSize(35, 35);
                cells[i][j].setMinSize(35, 35);
                board.add(cells[i][j], i, j);
            }
        }
        // place mines
        mf.placeMines();
        // update safe cell counter
        int initialUnExposed = mf.unexposedCount();
        safeCellCount.setText(String.valueOf(initialUnExposed));

        // play state
        for (int i = 0; i < mf.rowSize; i++) {
            for (int j = 0; j < mf.colSize; j++) {
                int fI = i;
                int fJ = j;
                Button current = cells[i][j];
                // clicking...
                current.setOnMouseClicked(event -> {
                    // right click, marking
                    if (event.getButton() == MouseButton.SECONDARY) {
                        if (!mf.fieldExposed[fI][fJ]) {
                            if (!mf.fieldMarked[fI][fJ]) {
                                current.setText("X");
                            } else {
                                current.setText("");
                            }
                            mf.mark(fI, fJ);
                            int unexposed = mf.unexposedCount();
                            safeCellCount.setText(String.valueOf(unexposed));
                        }
                    // left click, select either a mine or a safe cell
                    // check for winning or losing conditions
                    } else {
                        System.out.println("cells[" + fI + "][" + fJ + "] marked val: " + mf.fieldMarked[fI][fJ]);
                        System.out.println("cells[" + fI + "][" + fJ + "] exposed val: " + mf.fieldExposed[fI][fJ]);

                        if (!mf.fieldMarked[fI][fJ] && !mf.fieldExposed[fI][fJ]) {
                            int cellVal = mf.expose(fI, fJ);
                            if (cellVal == 0) { // no adj. bombs, update field accordingly
                                current.setStyle("-fx-background-color: lightgrey");
                                current.setText("");
                                updateBoard(mf);
                            } else if (cellVal == -1) endGame(mf, false); // lose
                            else { // continue playing
                                System.out.println("cells[" + fI + "][" + fJ + "] = " + cellVal);
                                current.setStyle("-fx-background-color: lightgrey");
                                current.setText(String.valueOf(cellVal));
                            }

                            if (mf.winCheck()) endGame(mf, true); // win
                            int unexposed = mf.unexposedCount();
                            safeCellCount.setText(String.valueOf(unexposed));
                        }
                    }
                });
            }
        }
    }


    private void updateBoard(MineField mf) {
        Button current;
        for (int i = 0; i < mf.rowSize; i++) {
            for (int j = 0; j < mf.colSize; j++) {
                if (mf.fieldExposed[i][j] && !mf.fieldMarked[i][j]) {
                    current = cells[i][j];
                    if (mf.minefield[i][j] == 0) {
                        current.setText("");
                    } else {
                        current.setText(String.valueOf(mf.minefield[i][j]));

                    }
                    current.setStyle("-fx-background-color: lightgrey");
                }
            }
        }
        return;
    }


    private void quit() {
        Platform.exit();
    }


    private void endGame(MineField mf, boolean win) {
        Button current;
        for (int i = 0; i < mf.rowSize; i++) {
            for (int j = 0; j < mf.colSize; j++) {
                current = cells[i][j];
                mf.fieldExposed[i][j] = true;
                current.setText("");
                if (mf.minefield[i][j] == -1 && (!win)){
                    current.setStyle("-fx-background-color: red");
                } else {
                    current.setStyle("-fx-background-color: lightgrey");
                }
            }
        }
        
        if (win) {
            System.out.println("You win!");
            greetBox.toFront();
            greeting.setText("You Win! Play again or select Quit.");
        } else {
            System.out.println("You lose!");
            greetBox.toFront();
            greeting.setText("You Lose! Play again or select Quit.");
        }
    }


}
