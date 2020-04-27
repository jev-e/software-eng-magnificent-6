package GUI;

import ClassStructure.*;
import javafx.application.Application;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Group;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.ColumnConstraints;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.RowConstraints;
import javafx.scene.paint.Color;
import javafx.stage.Stage;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.ArrayDeque;
import java.util.Collections;
import java.util.Deque;


public class Main extends Application {

    @FXML private ImageView jailTile = new ImageView();    // The imageview for the JailTile
    @FXML private ImageView goTile = new ImageView();
    @FXML private ImageView goToJailTile = new ImageView();
    @FXML private ImageView freeParkingTile = new ImageView();

    @FXML private Canvas tile1Colour = new Canvas();

    @FXML public GridPane topRowGP = new GridPane();      // The 9 x 1 Grid Pane Representing the Top Row

    private Image bootTokenPNG;
    private Image catTokenPNG;
    private Image gobletTokenPNG;
    private Image hatstandTokenPNG;
    private Image smartphoneTokenPNG;
    private Image spoonTokenPNG;

    @Override
    public void start(Stage primaryStage) throws Exception{
        // Set to the main-menu screen (start, rule and quit button)
        Parent root = FXMLLoader.load(getClass().getResource("mainMenu.fxml"));

        loadGameAssets();
        displayTopRow();

        GraphicsContext gc = tile1Colour.getGraphicsContext2D();
        gc.setFill(Color.BLUE);
        gc.fillRect(0,0,100,15);

        primaryStage.setTitle("Property Tycoon");
        primaryStage.setScene(new Scene(root,300,275));
        primaryStage.show();
    }

    public static void main(String[] args) { launch(args); }

    public void displayTopRow() {
        System.out.println("Display Top Row Called.");
        for (int i = 0; i < 9; i++) {
            if ((i != 1) && (i != 4) && (i != 7)) {
                addGroupToRowGP(topRowGP, i);
            }
        }
    }

    private void addGroupToRowGP(GridPane rowGP, int i) {
        javafx.scene.Group group = new Group();
        GridPane contents = new GridPane();
        GridPane tokens = new GridPane();
        Canvas canvas = new Canvas();
        Label propName = new Label("Property Name");
        Label propVal = new Label("Property Value");

        // Canvas Setup
        canvas.setHeight(15);
        GraphicsContext gc = canvas.getGraphicsContext2D();
        gc.setFill(Color.BLUE);
        gc.fillRect(0, 0, 100, 100);

        // Token Grid Setup

        ImageView tokenTL = new ImageView(bootTokenPNG);
        ImageView tokenTM = new ImageView(catTokenPNG);
        ImageView tokenTR = new ImageView(gobletTokenPNG);
        ImageView tokenBL = new ImageView(hatstandTokenPNG);
        ImageView tokenBM = new ImageView(smartphoneTokenPNG);
        ImageView tokenBR = new ImageView(spoonTokenPNG);

        formatTestTokens(tokenTL, tokenTM, tokenTR, tokenBL, tokenBM, tokenBR, 20);

        tokens.setConstraints(tokenTL, 0, 0);
        tokens.setConstraints(tokenTM, 0, 1);
        tokens.setConstraints(tokenTR, 1, 0);
        tokens.setConstraints(tokenBL, 1, 1);
        tokens.setConstraints(tokenBM, 2, 0);
        tokens.setConstraints(tokenBR, 2, 1);

        ColumnConstraints col1 = new ColumnConstraints();
        col1.setPercentWidth(33);
        ColumnConstraints col2 = new ColumnConstraints();
        col2.setPercentWidth(33);
        ColumnConstraints col3 = new ColumnConstraints();
        col3.setPercentWidth(33);

        RowConstraints row1 = new RowConstraints();
        row1.setMinHeight(30);
        RowConstraints row2 = new RowConstraints();
        row2.setMinHeight(30);

        tokens.getColumnConstraints().addAll(col1, col2, col3);
        tokens.getRowConstraints().addAll(row1, row2);

        tokens.getChildren().addAll(tokenTL, tokenTM, tokenTR, tokenBL, tokenBM, tokenBR);

        // Set Location of desired contents
        contents.setConstraints(canvas, 0, 0);
        contents.setConstraints(propName, 0, 1);
        contents.setConstraints(tokens, 0, 2);
        contents.setConstraints(propVal, 0, 3);

        // Add contents to the gridPane
        contents.getChildren().addAll(canvas, propName, tokens, propVal);

        // Add the grid to the group
        group.getChildren().add((contents));

        group.setRotate(-180);

        // Add the group to the row / column of gameboard
        rowGP.setConstraints(group, i, 0);
        rowGP.getChildren().add(group);

    }

    private void formatTestTokens(ImageView tl, ImageView tm, ImageView tr, ImageView bl, ImageView bm, ImageView br, int size) {
        tl.setPreserveRatio(true);
        tl.setFitHeight(size);
        tl.setFitHeight(size);
        //tl.setVisible(false);

        tm.setPreserveRatio(true);
        tm.setFitHeight(size);
        tm.setFitHeight(size);
        //tm.setVisible(false);

        tr.setPreserveRatio(true);
        tr.setFitHeight(size);
        tr.setFitHeight(size);
        //tr.setVisible(false);

        bl.setPreserveRatio(true);
        bl.setFitHeight(size);
        bl.setFitHeight(size);
        //bl.setVisible(false);

        bm.setPreserveRatio(true);
        bm.setFitHeight(size);
        bm.setFitHeight(size);
        //bm.setVisible(false);

        br.setPreserveRatio(true);
        br.setFitHeight(size);
        br.setFitHeight(size);
        //br.setVisible(false);
    }

    public void loadGameAssets() {

        System.out.println("Load Game Assets Called.");

        try {
            // Load Tile Images
            Image freeParkingPNG = new Image(new FileInputStream("Lib/TilesDesign/freeParking64bit.png"));
            Image jailPNG = new Image(new FileInputStream("Lib/TilesDesign/visiting64bit.png"));
            Image goToJailPNG = new Image(new FileInputStream("Lib/TilesDesign/goJail64bit.png"));
            Image goPNG = new Image(new FileInputStream("Lib/TilesDesign/goWithColour64bit.png"));

            // Load Token Images
            bootTokenPNG = new Image(new FileInputStream("Lib/Tokens/BootToken.png"));
            catTokenPNG = new Image(new FileInputStream("Lib/Tokens/CatToken.png"));
            gobletTokenPNG = new Image(new FileInputStream("Lib/Tokens/GobletToken.png"));
            hatstandTokenPNG = new Image(new FileInputStream("Lib/Tokens/HatstandToken.png"));
            smartphoneTokenPNG = new Image(new FileInputStream("Lib/Tokens/SmartphoneToken.png"));
            spoonTokenPNG = new Image(new FileInputStream("Lib/Tokens/SpoonToken.png"));

            // Assign images to the 4 corner tiles
            freeParkingTile.setImage(freeParkingPNG);
            jailTile.setImage((jailPNG));
            goToJailTile.setImage(goToJailPNG);
            goTile.setImage(goPNG);
        } catch (FileNotFoundException e) {

        }
    }

}
