package GUI;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;

import javafx.geometry.Pos;

import javafx.scene.control.Button;

public class Main extends Application {

    public int numberOfPlayer;
    public int numberOfAI;

    @Override
    public void start(Stage primaryStage) throws Exception{
        // Initialise how many players and Ai is in the game (default to 0 players and 0 ai)
        numberOfPlayer = 0;
        numberOfAI = 0;
        Parent root = FXMLLoader.load(getClass().getResource("mainMenu.fxml"));
        primaryStage.setTitle("Property Tycoon");
        primaryStage.setScene(new Scene(root,300,275));
        primaryStage.show();
    }

    public static void main(String[] args) {
        launch(args);
    }
}
