package GUI;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.scene.layout.Border;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.GridPane;
import javafx.stage.Stage;


public class Main extends Application {

    // Variables
    private Label[] tiles;

    // Scenes
    private Scene gameScene; // Shows gameBP

    // BorderPanes
    private BorderPane gameBP; // Holds entire game screen

    // GridPanes
    private GridPane boardGP;   // Main Board, Left side of screen
    private GridPane playersGP; // Player cards, Right side of Screen

    private GridPane topRowGP;   // Top 9 tiles container (Not the top two corners)
    private GridPane leftColGP;  // Left 9 tiles container (Not the Corners)
    private GridPane rightColGP; // Right 9 tiles container (Not the corners)
    private GridPane botRowGP;   // Bottom 9 tiles container (Not the corners)

    @Override
    public void start(Stage primaryStage) throws Exception{
        primaryStage.setTitle("Property Tycoon");

        displayGame();

        gameScene = new Scene(gameBP);
        primaryStage.setScene(gameScene);
        primaryStage.show();
    }

    public static void main(String[] args) {
        launch(args);
    }

    /**
     * Displays Board to primaryStage
     *
     * @author Callum Crawford
     */
    public void displayGame() {

        initVariables(); // Initalise Variables and Containers

        int shortSide = 60, longSide = 100;
        for (int i = 0; i < 40; i++) {

            String labelValue = String.valueOf(i);
            Label label = new Label("Tile: " + labelValue);

            // Go Tile
            if (i == 0) {
                boardGP.add(label, 2, 2);
            }

            // Bottom Row
            if (i > 0 && i < 10) {
                botRowGP.add(label, (9 - i), 0);
            }

            // Jail Tile
            if (i == 10) {
                boardGP.add(label, 0,2);
            }

            // Left Column
            if (i > 10 && i <20) {
                leftColGP.add(label, 0, (20 - i));
            }

            // Free Parking Tile
            if (i == 20) {
                boardGP.add(label, 0, 0);
            }

            // Top Row
            if (i > 20 && i < 30) {
                topRowGP.add(label, i, 0);
            }

            // Go To Jail Tile
            if (i == 30) {
                boardGP.add(label, 2,0);
            }

            // Right Column
            if (i > 30) {
                rightColGP.add(label, 0, i);
            }
        }
    }

    /**
     * Initialises all Panes and Variables for Game Scene
     *
     * @author Callum Crawford
     */
    public void initVariables() {
        // Initialise main Game containers
        gameBP = new BorderPane();
        boardGP = new GridPane();
        playersGP = new GridPane();

        // Add GridPanes to gameBP
        gameBP.setLeft(boardGP);
        gameBP.setRight(playersGP);

        // Initialise inner containers
        topRowGP = new GridPane();
        leftColGP = new GridPane();
        rightColGP = new GridPane();
        botRowGP = new GridPane();

        // Add inner containers to boardGP
        boardGP.add(topRowGP, 1, 0);
        boardGP.add(leftColGP, 0, 1);
        boardGP.add(rightColGP, 2,1);
        boardGP.add(botRowGP,1,2);

        // Initialise Variables
        tiles = new Label[40];
    }
}
