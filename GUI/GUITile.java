package GUI;

import javafx.scene.Group;
import javafx.scene.canvas.Canvas;
import javafx.scene.control.Label;
import javafx.scene.layout.GridPane;

public class GUITile {

    private int tileID;

    // Java FX Assets
    private Group tileGroup;
    private GridPane contents;
    private GridPane tokens;
    private Canvas colour;
    private Label propertyName;
    private Label propertyValue;

    public GUITile(int ID) {
        this.tileID = ID;

    }

    private void getPropertyName() {

    }


}
