package files;

import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.layout.GridPane;

/**
 * @filename Controller.java
 * @author Colin St. Claire
 * @created 04/20/16
 * @modified 04/20/16
 */

public class Controller {
    @FXML
    private GridPane gp;
    @FXML
    private Label title;
    @FXML
    private Button start = new Button("Start");
    @FXML
    private Button cell = new Button();
    @FXML
    private void initialize() {
        MineField mf = new MineField(1);
        for (int i = 0 ; i < mf.rowSize; i++) {
            for (int j = 0; j < mf.colSize; j++) {
                gp.add(cell, i, j);
            }
        }
    }

}
